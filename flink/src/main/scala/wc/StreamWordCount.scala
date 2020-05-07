package wc

import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.api.scala._

object StreamWordCount {
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.readTextFile("/Users/double/workspace/interview/flink/src/main/resources/hello")
      .flatMap(_.split(" "))
      .map((_,1))
      .keyBy(0)
      .sum(1)
      .print()

    env.execute()

  }
}
