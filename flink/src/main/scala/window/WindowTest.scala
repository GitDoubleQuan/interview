package window

import org.apache.flink.api.java.tuple.{Tuple, Tuple1}
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.functions.{AssignerWithPeriodicWatermarks, AssignerWithPunctuatedWatermarks}
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.scala.function.{ProcessWindowFunction, RichWindowFunction, WindowFunction}
import org.apache.flink.streaming.api.watermark.Watermark
import org.apache.flink.streaming.api.windowing.assigners.{EventTimeSessionWindows, GlobalWindows, TumblingProcessingTimeWindows}
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.windowing.windows.{GlobalWindow, TimeWindow}
import org.apache.flink.util.Collector
import pojo.SensorReading

object WindowTest {
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    //使用eventtime需要两步
    //1.指定使用事件时间，2.指定作为eventTime的字段
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)
    env.setParallelism(1)
    //设置周期生成watermark的时间间隔，默认是200ms
//    env.getConfig.setAutoWatermarkInterval(500)

    //nc -lk 7777
    val source = env.socketTextStream("localhost", 7777)

    //基本转换算子
    val SensorReadingDStream = source.map(data => {
      val arr = data.split(",")
      SensorReading(arr(0), arr(1).toLong, arr(2).toDouble)
    })
      //指定eventTime字段，eventTime需要的单位是毫秒
      //对于流里的元素天然有序的情况，可以直接提取eventTime作为watermark，可以用下面的方式简写
      //      .assignAscendingTimestamps(_.timestamp)

      //watermark并不区分key，watermark在所有的key之间共享，（对应的视频教程在Day5-2_复习测试watermark和window - 19:54开始，比较清楚的说明了watermark的传递机制）
      //周期的发送watermark，周期性的触发getCurrentWatermark方法，默认的时间周期是200ms
//      .assignTimestampsAndWatermarks(new AssignerWithPeriodicWatermarks[SensorReading] {
//        var maxTimeStamp: Long = _
//
//        override def getCurrentWatermark: Watermark = {
//          new Watermark(maxTimeStamp)
//        }
//
//        override def extractTimestamp(element: SensorReading, previousElementTimestamp: Long): Long = {
//          maxTimeStamp = maxTimeStamp.max(element.timestamp)
//          element.timestamp
//        }
//      })

      //断点式的触发watermark,每来一条数据都会触发checkAndGetNextWatermark方法
      .assignTimestampsAndWatermarks(new AssignerWithPunctuatedWatermarks[SensorReading] {
        var maxTimeStamp:Long = _
        override def checkAndGetNextWatermark(lastElement: SensorReading, extractedTimestamp: Long): Watermark = {
          if("sensor_1".equals(lastElement.id)){
            new Watermark(maxTimeStamp)
          }else{
            null
          }
        }

        override def extractTimestamp(element: SensorReading, previousElementTimestamp: Long): Long = {
          maxTimeStamp = maxTimeStamp.max(element.timestamp)
          element.timestamp
        }
      })

      //指定最大乱序程度，周期性的发送单调递增的watermark
//      .assignTimestampsAndWatermarks(new BoundedOutOfOrdernessTimestampExtractor[SensorReading](Time.minutes(1)) {
//        override def extractTimestamp(element: SensorReading): Long = element.timestamp
//      })


    val result = SensorReadingDStream
      //在发射watermark和窗口操作之间依然可以加入其它算子
      //eventTime已经没了，这样的话元素还能正确分配到对应的窗口中吗？是的，依然可以正确的分配到对应窗口中
      .map(x => (x.id, x.temperature))
      .keyBy(0)
      //滚动窗口
      .timeWindow(Time.seconds(20))
      //滑动窗口
      //      .timeWindow(Time.hours(1),Time.minutes(10))
      //会话窗口
      //      .window(EventTimeSessionWindows.withGap(Time.hours(1))

      //可选api
      //在watermark到达窗口的结束边界时，只输出结果不关闭窗口，当watermark超过窗口的结束边界+延迟时间的时候在关闭窗口
      //在允许延迟的这段时间内，每到达一个元素都会输出一个结果
      .allowedLateness(Time.minutes(1))
      //如果设置了窗口允许延迟之后，依然还有迟到的元素，还可以把迟到的元素放在侧输出流里
      .sideOutputLateData(new OutputTag[(String, Double)]("late"))
      //flink对于处理数据的乱序或者说是数据的延迟，一共有3重保障机制
      //1.watermark 2.窗口延迟关闭 3.侧输出流


      //增量聚合函数
      //      .reduce((result, data) => {
      //        SensorReading(result.id, data.timestamp.max(result.timestamp), data.temperature.max(result.temperature))
      //      })
      //全窗口函数
      //        .apply(new WindowFunction[SensorReading,(Long,Int),Tuple,TimeWindow] {
      //          override def apply(key: Tuple, window: TimeWindow, input: Iterable[SensorReading], out: Collector[(Long,Int)]): Unit = {
      //            out.collect((window.getEnd,input.size))
      //          }
      //        })
      .apply(new WindowFunction[(String, Double), String, Tuple, TimeWindow] {
        override def apply(key: Tuple, window: TimeWindow, input: Iterable[(String, Double)], out: Collector[String]): Unit = {
          val k = key.asInstanceOf[Tuple1[String]].f0
          val tuple = input.reduce((a, b) => (a._1, b._2.max(a._2)))
          out.collect(k + "->" + window.getStart + "-" + window.getEnd + ": " + tuple)
        }
      })

    result.print()

    result.getSideOutput(new OutputTag[(String,Double)]("late"))

    env.execute()

  }
}
