package jzoffer.chapter3;

import com.sun.xml.internal.ws.runtime.config.TubelineFeatureReader;
import org.junit.Assert;
import org.junit.Test;

/**
 * 题目：请实现一个函数用来判断字符串是否表示数值（包括整数和小
 * 数）。例如，字符串"+100"、"5e2"、
 * "-123", "31416"及"-1E-16"都表示数
 * "一123"、
 * 值，但"12e"、"1a3．14"、
 * "1.2．3"、”+一5"及"12e+5．4"都不是。
 * <p>
 * Created by xushuangquan on 2020/4/8.
 */
public class Q20_CheckNumFormat {

    /**
     * 总结下规律：
     * 1."e"|"E"把数值分成两部分，A和B，如果"e"|"E"存在，则A和B都必须存在
     * 2.A可以是小数，但是B只能是整数
     * 3.A和B前面都可以带"+"、"-"
     * 4.如果A是小数，则"."后面是无符号整数
     *(对于小数点前后可能不存在数据的情况未做处理，0.123 -> .123 , 123.0 -> 123.)
     * @param numStr
     * @return
     */
    private boolean checkNumFormat(String numStr) {
        if (numStr == null || numStr.length() == 0) {
            throw new IllegalArgumentException();
        }
        int index = -1;
        for (int i = 0; i < numStr.length(); i++) {
            if (numStr.charAt(i) == 'e' || numStr.charAt(i) == 'E') {
                index = i;
                break;
            }
        }
        if (index != -1) {
            String A = numStr.substring(0, index);
            String B = numStr.substring(index + 1);
            if(isIntOrFloat(A) && isInt(B)){
                return true;
            }
            return false;
        }
        return isIntOrFloat(numStr);
    }

    private boolean isIntOrFloat(String numStr) {
        if (numStr == null || numStr.length() == 0) {
            return false;
        }
        int index = -1;
        for (int i = 0; i < numStr.length(); i++) {
            if (numStr.charAt(i) == '.') {
                index = i;
                break;
            }
        }
        if (index != -1) {
            String C = numStr.substring(0, index);
            String D = numStr.substring(index + 1);
            if (isInt(C) && isIntWithoutSign(D)) {
                return true;
            }
            return false;
        }
        return isInt(numStr);
    }

    private boolean isInt(String numStr) {
        if (numStr == null || numStr.length() == 0) {
            return false;
        }

        if (numStr.charAt(0) == '+' || numStr.charAt(0) == '-') {
            return isIntWithoutSign(numStr.substring(1));
        }

        return isIntWithoutSign(numStr);
    }

    private boolean isIntWithoutSign(String numStr) {
        if (numStr == null || numStr.length() == 0) {
            return false;
        }

        for (int i = 0; i < numStr.length(); i++) {
            if (numStr.charAt(i) >= '0' && numStr.charAt(i) <= '9') {
                continue;
            }
            return false;
        }

        return true;
    }

    @Test
    public void test() {
        String[][] testCases = new String[][] {
                new String[] {"+100", "true"},
                new String[] {"+100.0", "true"},
                new String[] {"-100.01", "true"},
                new String[] {"-123", "true"},
                new String[] {"-123.123", "true"},
//                new String[] {"-.123", "true"},
//                new String[] {"+.123", "true"},
//                new String[] {".123", "true"},
                new String[] {"5e2", "true"},
//                new String[] {".5e2", "true"},
                new String[] {"0.5e2", "true"},
                new String[] {"-1.2e-34", "true"},
                new String[] {"-12e-0", "true"},
                new String[] {"-12e-123", "true"},
                new String[] {"-12e-1.23", "false"},
                new String[] {"0.5eE2", "false"},
                new String[] {"0.5e", "false"},
                new String[] {"0.5e1.2", "false"},
                new String[] {"0.5a", "false"},
                new String[] {"a1234", "false"},
                new String[] {"1.2.34", "false"},
                new String[] {"1.2e.34", "false"},
                new String[] {"1.2e.34", "false"}
        };

        for (String[] testCase : testCases) {
            boolean b = String.valueOf(checkNumFormat(testCase[0])).equals(testCase[1]);

            if (!b) {
                System.out.println(testCase[0] + "," + testCase[1]);
            }

            Assert.assertTrue(b);
        }
    }

}
