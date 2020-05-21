package checkpoint

import java.util.Properties

import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.streaming.api.CheckpointingMode
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer011
import org.apache.flink.streaming.connectors.kafka.internals.KeyedSerializationSchemaWrapper
import org.apache.flink.streaming.util.serialization.KeyedSerializationSchema
import pojo.SensorReading

object CheckpointTest {
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment

    //checkpoint默认是不开启的，需要显示开启，指点两次触发checkpoint之间的时间间隔
    env.enableCheckpointing(1000)
    //checkpoint持久化的地址
    //默认就是精确一次
    env.getCheckpointConfig.setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE)
    //一次checkpoint如果超过设置时间还没有执行完就算失败
    env.getCheckpointConfig.setCheckpointTimeout(30000)
    //允许多个checkpoint同时执行，发生在前面的checkpoint还没执行完，后面的checkpoint已经开始的情况
    env.getCheckpointConfig.setMaxConcurrentCheckpoints(2)
    //两次checkpoint之间至少间隔多长时间，为了保证在两次checkpoint之间有足够的时间来处理数据，而不是把时间都用在执行checkpoint上
    env.getCheckpointConfig.setMinPauseBetweenCheckpoints(500)
    //当有savepoint的时候，是否使用checkpoint作为故障恢复
    env.getCheckpointConfig.setPreferCheckpointForRecovery(false)

    val source = env.socketTextStream("localhost", 7777)

    val result = source.map(data => {
      val arr = data.split(",")
      SensorReading(arr(0), arr(1).toLong, arr(2).toDouble).toString
    })


    //kafka producer继承了两阶段提交，可以以事务的方式向kafka发送数据，使用的时候指定语义为exactly_once
    result.addSink(new FlinkKafkaProducer011[String](
      "test_kafka_exactly_once",
      new KeyedSerializationSchemaWrapper(new SimpleStringSchema()),
      new Properties(),
      FlinkKafkaProducer011.Semantic.EXACTLY_ONCE))




  }
}
