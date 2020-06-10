package tableapi

//只要是api.scala后面都要加_,以引入隐式转换

import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.table.api._
import org.apache.flink.table.api.scala._
import org.apache.flink.types.Row
import pojo.SensorReading

object TimeAndWindowTest {
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    val blinkStreamSetting = EnvironmentSettings.newInstance().useBlinkPlanner().inStreamingMode().build()
    val blinkStreamTableEnv = StreamTableEnvironment.create(env, blinkStreamSetting)
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)
    env.setParallelism(1)

    val source = env
      .readTextFile("/Users/double/workspace/interview/flink/src/main/resources/tempdata")
      .map(data => {
        val arr = data.split(",")
        SensorReading(arr(0), arr(1).toLong, arr(2).toDouble)
      })

    //1.使用处理时间，并且指定处理时间字段
    //对于pt这种扩展的字段只能定义在最后
    //    val sourceTable = blinkStreamTableEnv.fromDataStream(source, 'id, 'temperature as 'temp, 'timestamp as 'ts, 'pt.proctime)
    //2.使用事件时间，并且指定事件时间字段
    //首先提取事件时间
    val sourceWithEventTime = source
      .assignTimestampsAndWatermarks(new BoundedOutOfOrdernessTimestampExtractor[SensorReading](Time.minutes(1)) {
        override def extractTimestamp(element: SensorReading): Long = element.timestamp + 2000
      })
    //指定事件时间字段,et的值是从extractTimestamp（）中指定的
    //指定事件时间为扩展字段
    //    val sourceTable = blinkStreamTableEnv.fromDataStream(sourceWithEventTime, 'id, 'temperature as 'temp, 'timestamp as 'ts, 'et.rowtime)
    //指定事件时间为已有字段
    //extractTimestamp()中提取的事件时间会覆盖已有的字段
    val sourceTable = blinkStreamTableEnv.fromDataStream(sourceWithEventTime, 'id, 'temperature as 'temp, 'timestamp.rowtime as 'ts)


    sourceTable.printSchema()
    sourceTable.toAppendStream[Row].print()

    env.execute()

  }
}
