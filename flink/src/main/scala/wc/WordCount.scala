package wc

//scala的隐式转换都需要引入这个包
import org.apache.flink.api.scala._

object WordCount {

  def main(args: Array[String]): Unit = {
    val env = ExecutionEnvironment.getExecutionEnvironment
    val source: DataSet[String] = env.readTextFile("/Users/double/workspace/interview/flink/src/main/resources/hello")
    source.flatMap(_.split(" "))
      .map((_,1))
      .groupBy(0)
      .sum(1)
      .print()

  }
}
