package tableapi


import org.apache.flink.api.scala.ExecutionEnvironment
import org.apache.flink.streaming.api.scala._
import org.apache.flink.table.api.scala._
import org.apache.flink.table.api.{DataTypes, EnvironmentSettings, TableEnvironment}
import org.apache.flink.table.descriptors.{Csv, Elasticsearch, FileSystem, Json, Kafka, OldCsv, Schema}
import org.apache.flink.types.Row
import pojo.SensorReading

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
      .select('id, 'temperature)

    val table1 = blinkStreamTableEnv.sqlQuery(
      """
        |select id,count(temperature) as cnt
        |from inputTable
        |where id = 'sensor_1'
        |group by id
        |""".stripMargin)

    //4.table、view和dataStream之间的转换
    //4.1dataStream to table
    val source = env
      .readTextFile("/Users/double/workspace/interview/flink/src/main/resources/tempdata")
      .map(data => {
        val arr = data.split(",")
        SensorReading(arr(0), arr(1).toLong, arr(2).toDouble)
      })


    //4.2基于dataStream创建表
    //后面也可不跟schema，默认一一对应，如果跟了可以做字段的重命名  字段的位置调整  删除字段等操作
    val sourceTable = blinkStreamTableEnv.fromDataStream(source, 'id, 'temperature as 'temp, 'timestamp as 'ts)
    sourceTable.printSchema()
    //4.3基于dataStream创建view,跟创建表一样，schema可以不加，默认一一对应
    blinkStreamTableEnv.createTemporaryView("source_view", source, 'id, 'temperature as 'temp, 'timestamp as 'ts)
    //4.4基于table创建view
    blinkStreamTableEnv.createTemporaryView("source_view2", sourceTable)


    //5 table sink
    //5.1输出到kafka
    //kafka作为消息队列只支持append流
    blinkStreamTableEnv.connect(
      new Kafka()
        .version("0.11")
        .topic("sinkTest")
        .property("zookeeper.connect", "localhost:2181")
        .property("bootstrap.servers", "localhost:9092")
    )
      .withFormat(new Csv())
      .withSchema(new Schema()
        .field("id", DataTypes.STRING())
        .field("cnt", DataTypes.DOUBLE())
      )
      .createTemporaryTable("kafkaOutputTable")

    table.insertInto("kafkaOutputTable")


    //5.2输出到es es是支持upsert模式的
    blinkStreamTableEnv.connect(
      new Elasticsearch()
        .version("6")
        .host("localhost", 9200, "http")
        .index("sensor")
        .documentType("temp")
    )
      .inUpsertMode() // 指定是 Upsert 模式
      .withFormat(new Json())
      .withSchema(new Schema()
        .field("id", DataTypes.STRING())
        .field("count", DataTypes.BIGINT())
      )
      .createTemporaryTable("esOutputTable")

    table1.insertInto("esOutputTable")


    //5.3输出到mysql
    val sinkDDL: String =
      """
        |create table jdbcOutputTable (
        |  id varchar(20) not null,
        |  cnt bigint not null
        |) with (
        |  'connector.type' = 'jdbc',
        |  'connector.url' = 'jdbc:mysql://localhost:3306/test',
        |  'connector.table' = 'sensor_count',
        |  'connector.driver' = 'com.mysql.jdbc.Driver',
        |  'connector.username' = 'root',
        |  'connector.password' = '123456'
        |)
  """.stripMargin

    blinkStreamTableEnv.sqlUpdate(sinkDDL)
    table1.insertInto("jdbcOutputTable")


    blinkStreamTableEnv.toRetractStream[(String, Double)](table).print()
    env.execute()


  }
}
