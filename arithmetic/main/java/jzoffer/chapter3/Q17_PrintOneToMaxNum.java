package jzoffer.chapter3;

import org.junit.Test;

/**
 * 题目：输入数字 n，按顺序打印出从1到最大的 n 位十进制数。比如输
 * 入3，则打印出1、2、3一直到最大的3位数999。
 * <p>
 * Created by xushuangquan on 2020/3/11.
 */
public class Q17_PrintOneToMaxNum {

    /**
     * 递归实现全排列的方式
     *
     * @param n
     */
    private void printOneToMaxNum(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException();
        }
        char[] number = new char[n];
        for (int i = 0; i < 10; i++) {
            number[0] = intToChar(i);
            corePrintOneToMaxNum(number, n, 1);
        }
    }


    private void corePrintOneToMaxNum(char[] number, int length, int index) {
        if (index == length) {
            printNum(number);
            return;
        }
        for (int i = 0; i < 10; i++) {
            number[index] = intToChar(i);
            corePrintOneToMaxNum(number, length, index + 1);
        }
    }

    /**
     * 从第一个不为0的位置开始打印
     *
     * @param number
     */
    private void printNum(char[] number) {
        boolean flag = false;
        for (int i = 0; i < number.length; i++) {
            if (number[i] != '0') {
                flag = true;
            }
            if (flag || i == number.length - 1) {
                System.out.print(number[i]);
            }
        }
        System.out.println('\t');
    }


    //没做参数校验
    private int charToInt(char x) {
        return x - '0';
    }

    private char intToChar(int x) {
        return (char) (x + '0');
    }

    @Test
    public void test() {
        printOneToMaxNum(3);
    }
}
