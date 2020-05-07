package source

import java.util.Properties

import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.streaming.api.functions.source.SourceFunction
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer011
import pojo.SensorReading

import scala.util.Random

object SourceTest {
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    //    env.setParallelism(1)

    //kafkaSource
    val kafkaConf = new Properties()
    kafkaConf.setProperty("bootstrap.servers", "localhost:9092")
    kafkaConf.setProperty("group.id", "consumer-group")
    kafkaConf.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    kafkaConf.setProperty("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    kafkaConf.setProperty("auto.offset.reset", "latest")

    env.addSource(new FlinkKafkaConsumer011("kafkaTopic", new SimpleStringSchema(), kafkaConf)).setParallelism(10)

    //自定义source
    env.addSource(new Mysource).print()

    env.execute()
  }

}


//自定义source
class Mysource extends SourceFunction[SensorReading]{
  private var flag = true

  override def run(sourceContext: SourceFunction.SourceContext[SensorReading]): Unit = {
    val rand = new Random()

    val curTemp = 1.to(10).map(
      i => ( "sensor_" + i, 65 + rand.nextGaussian() * 20 + rand.nextGaussian())
    )

    while (flag) {
      val currentTime = System.currentTimeMillis()
      curTemp.foreach(t => sourceContext.collect(SensorReading(t._1,currentTime,t._2)))
    }

    Thread.sleep(1000)
  }

  override def cancel(): Unit = {
    flag = false
  }
}
