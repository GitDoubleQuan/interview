package jzoffer.chapter2;

import org.junit.Assert;
import org.junit.Test;

import java.util.Objects;

/**
 * 面试题5：替换空格
 * 题目：请实现一个函数，把字符串中的每个空格替换成"%20"。例如输入“We are happy.”，
 * 则输出“We%20are%20happy.”。
 * Created by xushuangquan on 2020/2/20.
 */
public class Q5_ReplaceBlank {
    private String replaceBlank(String str) {
        if (str == null || str.length() == 0) {
            return str;
        }
        int originalStrLength = str.length();
        int blankNum = 0;
        for (int i = 0; i < originalStrLength; i++) {
            if (' ' == str.charAt(i)) {
                blankNum++;
            }
        }
        int newStrLength = originalStrLength + blankNum * 2;

        char[] newStr = new char[newStrLength];

        int originalPoint = originalStrLength - 1;
        int newPoint = newStrLength - 1;
        while (originalPoint >= 0) {
            if (str.charAt(originalPoint) == ' ') {
                newStr[newPoint--] = '0';
                newStr[newPoint--] = '2';
                newStr[newPoint--] = '%';
            } else {
                newStr[newPoint--] = str.charAt(originalPoint);
            }
            originalPoint--;
        }
        return new String(newStr);
    }


    @Test
    public void test() {
        Assert.assertTrue(Objects.equals(replaceBlank(""), ""));
        Assert.assertTrue(Objects.equals(replaceBlank(null), null));
        Assert.assertTrue(Objects.equals(replaceBlank("Hello world"), "Hello%20world"));
        Assert.assertTrue(Objects.equals(replaceBlank(" Hello world "), "%20Hello%20world%20"));
        Assert.assertTrue(Objects.equals(replaceBlank("Helloworld"), "Helloworld"));
    }
}
