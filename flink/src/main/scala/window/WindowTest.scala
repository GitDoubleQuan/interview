package window

import org.apache.flink.api.java.tuple.Tuple
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.scala.function.WindowFunction
import org.apache.flink.streaming.api.windowing.assigners.{EventTimeSessionWindows, GlobalWindows, TumblingProcessingTimeWindows}
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.windowing.windows.{GlobalWindow, TimeWindow}
import org.apache.flink.util.Collector
import pojo.SensorReading

object WindowTest {
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    //使用eventtime需要两步
    //1.指定使用事件时间，2.指定作为eventTime的字段
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)
    env.setParallelism(1)

    //nc -lk 7777
    val source = env.socketTextStream("localhost",7777)

    //基本转换算子
    val SensorReadingDStream = source.map(data => {
      val arr = data.split(",")
      SensorReading(arr(0), arr(1).toLong, arr(2).toDouble)
    })
      //指定eventTime字段，eventTime需要的单位是毫秒
      .assignAscendingTimestamps(_.timestamp)


    val result = SensorReadingDStream
      .keyBy("id")
      //滚动窗口
      .timeWindow(Time.seconds(5))
      //滑动窗口
      //      .timeWindow(Time.hours(1),Time.minutes(10))
      //会话窗口
      //      .window(EventTimeSessionWindows.withGap(Time.hours(1))



      //增量聚合函数
//      .reduce((result, data) => {
//        SensorReading(result.id, data.timestamp.max(result.timestamp), data.temperature.max(result.temperature))
//      })
      //全窗口函数
        .apply(new WindowFunction[SensorReading,(Long,Int),Tuple,TimeWindow] {
          override def apply(key: Tuple, window: TimeWindow, input: Iterable[SensorReading], out: Collector[(Long,Int)]): Unit = {
            out.collect((window.getEnd,input.size))
          }
        })

    result.print()

    env.execute()

  }
}
