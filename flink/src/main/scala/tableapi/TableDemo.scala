package tableapi

import org.apache.flink.streaming.api.scala._
import org.apache.flink.table.api.scala._
import pojo.SensorReading

object TableDemo {
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    val tableEnv = StreamTableEnvironment.create(env)

    val source = env.socketTextStream("localhost", 7777)

    val ds = source.map(data => {
      val arr = data.split(",")
      SensorReading(arr(0), arr(1).toLong, arr(2).toDouble)
    })

    val table = tableEnv.fromDataStream(ds)

    val resultTable = table
      .select("id, temperature")
      .filter("id == 'sensor_10'")

    val resultTable2 = tableEnv.sqlQuery("select id,temperature from " + table + " where id = 'sensor_10'")

    resultTable2.toAppendStream[(String, Double)].print()
    resultTable2.printSchema()


    env.execute()
  }
}
