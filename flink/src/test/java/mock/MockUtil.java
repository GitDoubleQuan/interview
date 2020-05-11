package mock;

import org.apache.commons.compress.utils.Lists;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by xushuangquan on 2020/5/8.
 */
public class MockUtil {

    public static List<Long> mockEventTime(String startTime, int interval, int count) throws ParseException {
        ArrayList<Long> result = Lists.newArrayList();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        long startTimeStamp = df.parse(startTime).getTime();
        for (int i = 0; i < count; i++) {
            startTimeStamp = startTimeStamp + interval * 1000;
            result.add(startTimeStamp);
        }
        return result;
    }


    public static void main(String[] args) throws Exception {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<Long> result = mockEventTime("2020-05-08 10:10", 5, 10);
        for (Long time : result) {
            System.out.println(time + "-" + df.format(new Date(time)));
        }
    }
}
