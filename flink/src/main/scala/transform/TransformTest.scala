package transform

import org.apache.flink.api.java.functions.KeySelector
import org.apache.flink.streaming.api.functions.ProcessFunction
import org.apache.flink.streaming.api.functions.co.CoMapFunction
import org.apache.flink.streaming.api.scala._
import org.apache.flink.util.Collector
import pojo.SensorReading

import scala.collection.immutable.HashSet.HashSet1
import scala.collection.mutable

object TransformTest {
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)

    val source = env.readTextFile("/Users/double/workspace/interview/flink/src/main/resources/tempdata")

    //基本转换算子
    val SensorReadingDStream = source.map(data => {
      val arr = data.split(",")
      SensorReading(arr(0), arr(1).toLong, arr(2).toDouble)
    })
    //      .filter(_.temperature > 65)


    //分组滚动聚合算子，DataStream没有聚合操作，所有的聚合操作都是基于KeyedDataStream
    //    SensorReadingDStream
    //      .keyBy(0)
    //      .keyBy("id")
    //      .keyBy(date => date.id)
    //      .keyBy(new KeySelector[SensorReading, String] {
    //        override def getKey(in: SensorReading): String = in.id
    //      })
    //      .maxBy("temperature")
    //      .print()

    //    SensorReadingDStream
    //      .keyBy(0)
    //      .reduce((currentRes, currentData) => {
    //        SensorReading(currentData.id, currentData.timestamp.max(currentRes.timestamp), currentData.temperature.max(currentRes.temperature))
    //      })
    //      .print()

    //多流操作算子
    val splitDataStream = SensorReadingDStream
      .split(data => {
        if (data.temperature > 65) Seq("high", "all") else Seq("low", "all")
      })
    val lowDataStream = splitDataStream
      .select("low")
    val highDataStream = splitDataStream
      .select("high")

    //processfunction替代split操作
    val mintorDs = SensorReadingDStream.process(new ProcessFunction[SensorReading, SensorReading] {
      val tag = new OutputTag[(String, Double, Long)]("low-temp");

      override def processElement(value: SensorReading, ctx: ProcessFunction[SensorReading, SensorReading]#Context, out: Collector[SensorReading]): Unit = {
        if (value.temperature > 50.0) {
          out.collect(value)
        } else {
          ctx.output(tag, (value.id, value.temperature, value.timestamp))
        }
      }
    })

    mintorDs.print("high-temp")

    mintorDs.getSideOutput(new OutputTag[(String,Double,Long)]("low-temp")).print("low-temp")


    //connect可以连接两条数据类型不同的流
    lowDataStream.map(data => (data.id, data.temperature))
      .connect(highDataStream)
      .map(new CoMapFunction[(String, Double), SensorReading, SensorReading] {
        val isExist = new mutable.HashSet[String]()

        override def map1(in1: (String, Double)): SensorReading = {
          isExist += in1._1
          null
        }

        override def map2(in2: SensorReading): SensorReading = {
          if (isExist.contains(in2.id)) in2 else null

        }
      })
      .filter(_ != null)
      .print("connect print")

    //union只能连接相同类型的多条流
    lowDataStream
      .union(highDataStream)
      .print("union print")


    //richfunction包含了生命周期方法，可以获取运行时上下文，在运行时上下文中可以对state做操作

    env.execute()
  }
}
