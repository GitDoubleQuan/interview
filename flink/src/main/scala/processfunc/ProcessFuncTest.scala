package processfunc

import org.apache.flink.api.common.state.ValueStateDescriptor
import org.apache.flink.api.java.tuple.Tuple
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.functions.KeyedProcessFunction
import org.apache.flink.streaming.api.scala._
import org.apache.flink.util.Collector
import pojo.SensorReading


/**
 * 需求：连续10s钟温度持续上升，触发报警
 */
object ProcessFuncTest {

  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)

    //nc -lk 7777
    val source = env.socketTextStream("localhost", 7777)

    source.map(data => {
      val arr = data.split(",")
      SensorReading(arr(0), arr(1).toLong, arr(2).toDouble)
    })
      .keyBy("id")
      .process(new KeyedProcessFunction[Tuple, SensorReading, String] {

        //有状态的编程
        //最近的一次的温度
        lazy val lastTemper = getRuntimeContext.getState(new ValueStateDescriptor[Double]("last_temper", classOf[Double]));
        //报警状态
        lazy val ts = getRuntimeContext.getState(new ValueStateDescriptor[Long]("ts", classOf[Long]))

        override def processElement(value: SensorReading, ctx: KeyedProcessFunction[Tuple, SensorReading, String]#Context, out: Collector[String]): Unit = {

          val lt = lastTemper.value()
          val alarmTime = ts.value()

          lastTemper.update(value.temperature)


          if (value.temperature >= lt && alarmTime == 0) {
            //可以得到当前的处理时间
            val time = ctx.timerService().currentProcessingTime() + 10000
            //可以注册定时器
            ctx.timerService().registerProcessingTimeTimer(time)
            ts.update(time)
          }

          if (value.temperature < lt && alarmTime != 0) {
            ctx.timerService().deleteProcessingTimeTimer(alarmTime)
            ts.clear()
          }
        }

        //定时器触发时回调
        override def onTimer(timestamp: Long, ctx: KeyedProcessFunction[Tuple, SensorReading, String]#OnTimerContext, out: Collector[String]): Unit = {
          out.collect("温度持续升高10s！")
          ts.clear()
        }
      })


  }

}
