package tableapi


import org.apache.flink.api.scala.ExecutionEnvironment
import org.apache.flink.streaming.api.scala._
import org.apache.flink.table.api.scala._
import org.apache.flink.table.api.{DataTypes, EnvironmentSettings, TableEnvironment}
import org.apache.flink.table.descriptors.{Csv, FileSystem, Kafka, OldCsv, Schema}
import org.apache.flink.types.Row

object TableApiTest {
  def main(args: Array[String]): Unit = {
    //1.创建table执行环境
    //老版本的stram table env
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    val oldStreamSetting = EnvironmentSettings.newInstance().useOldPlanner().inStreamingMode().build()
    val oldStreamTableEnv = StreamTableEnvironment.create(env, oldStreamSetting)

    //老版本的batch table env
    val benv = ExecutionEnvironment.getExecutionEnvironment
    val oldBatchTableEnv = BatchTableEnvironment.create(benv)

    //blink stream table env
    val blinkStreamSetting = EnvironmentSettings.newInstance().useBlinkPlanner().inStreamingMode().build()
    val blinkStreamTableEnv = StreamTableEnvironment.create(env, blinkStreamSetting)

    //blink batch table env
    val blinkBatchSetting = EnvironmentSettings.newInstance().useBlinkPlanner().inBatchMode().build()
    val blinkBatchTableEnv = TableEnvironment.create(blinkBatchSetting)


    //2.从外部系统读取数据
    //2.1读取文件
    blinkStreamTableEnv.connect(new FileSystem().path("/Users/double/workspace/interview/flink/src/main/resources/TableApiTestData"))
      //如果想解析其他的数据格式需要引入对应的依赖
      .withFormat(new Csv)
      .withSchema(new Schema()
        .field("id", DataTypes.STRING())
        .field("timestamp", DataTypes.BIGINT())
        .field("temperature", DataTypes.DOUBLE()))
      .createTemporaryTable("inputTable")


    //2.2读取kafka
    blinkStreamTableEnv.connect(
      new Kafka()
        .version("0.11") // 定义kafka的版本
        .topic("sensor") // 定义主题
        .property("zookeeper.connect", "localhost:2181")
        .property("bootstrap.servers", "localhost:9092")
    )
      .withFormat(new Csv())
      .withSchema(new Schema()
        .field("id", DataTypes.STRING())
        .field("timestamp", DataTypes.BIGINT())
        .field("temperature", DataTypes.DOUBLE())
      )
      .createTemporaryTable("kafkaInputTable")

    //3查询操作
    //3.1简单聚合操作
    val table = blinkStreamTableEnv.from("inputTable")
      .where('id === "sensor_1")
      .groupBy('id)
      .select('id, 'temperature.count as 'cnt)

    val table1 = blinkStreamTableEnv.sqlQuery(
      """
        |select id,count(temperature) as cnt
        |from inputTable
        |where id = 'sensor_1'
        |group by id
        |""".stripMargin)

    blinkStreamTableEnv.toRetractStream[(String, Double)](table).print()
    env.execute()


  }
}
