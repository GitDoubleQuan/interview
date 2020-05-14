package states

import org.apache.flink.api.common.functions.ReduceFunction
import org.apache.flink.api.common.state.{ReducingState, ReducingStateDescriptor}
import org.apache.flink.api.java.tuple.Tuple
import org.apache.flink.contrib.streaming.state.RocksDBStateBackend
import org.apache.flink.streaming.api.functions.KeyedProcessFunction
import org.apache.flink.streaming.api.scala._
import org.apache.flink.util.Collector
import pojo.SensorReading

object TestStates {
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment

    //使用rocksDB作为状态后端需要引入依赖
    env.setStateBackend(new RocksDBStateBackend(""))

    val source = env.readTextFile("/Users/double/workspace/interview/flink/src/main/resources/tempdata")

    //基本转换算子
    val SensorReadingDStream = source
      .map(data => {
        val arr = data.split(",")
        SensorReading(arr(0), arr(1).toLong, arr(2).toDouble)
      })
      .keyBy("id")
      .process(new KeyedProcessFunction[Tuple, SensorReading, String]() {

        //这里必须声明成懒加载，因为只有程序运行起来的时候才能获得运行时上下文
        lazy val myReduceStatus: ReducingState[SensorReading] = getRuntimeContext.getReducingState(
          new ReducingStateDescriptor[SensorReading](
            "myReducingStates",
            //每次调用add方法的时候都会触发这里的reduce方法
            new ReduceFunction[SensorReading] {
              override def reduce(value1: SensorReading, value2: SensorReading): SensorReading = {
                SensorReading(value2.id, value2.timestamp.max(value1.timestamp), value2.temperature.max(value1.temperature))
              }
            },
            classOf[SensorReading]))

        override def processElement(value: SensorReading, ctx: KeyedProcessFunction[Tuple, SensorReading, String]#Context, out: Collector[String]): Unit = {
          myReduceStatus.add(value)
          myReduceStatus.get()
        }
      })
  }
}
