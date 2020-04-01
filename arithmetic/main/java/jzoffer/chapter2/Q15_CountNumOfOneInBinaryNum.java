package jzoffer.chapter2;

import org.junit.Assert;
import org.junit.Test;

/**
 * 题目：请实现一个函数，输入一个整数，输出该数二进制表示中1 的
 * 个数。例如，把9表示成二进制是1001，有2位是1。因此，如果输入9，
 * 则该函数输出2。
 *
 * Created by xushuangquan on 2020/3/7.
 */
public class Q15_CountNumOfOneInBinaryNum {


    /**
     * 普通方法
     * @param num
     * @return
     */
    private int countNumOfOneInBinaryNum1(int num){
        int count = 0;
        int flag = 1;
        int j = num;
        while (j != 0){
            if((num & flag) > 0){
                count++;
            }
            j = j << 1;
            flag = flag << 1;
        }
        return count;
    }

    /**
     * 整数跟整数减一做与运算，会把整数右边的1变成0，记住这个技巧，非常适合求一个二进制数有多少位1
     * @param num
     * @return
     */
    private int countNumOfOneInBinaryNum(int num){
        int count = 0;
        while(num != 0){
            count++;
            num = num & (num-1);
        }
        return count;
    }

    @Test
    public void test(){
        Assert.assertTrue(countNumOfOneInBinaryNum(9) == 2);
        Assert.assertTrue(countNumOfOneInBinaryNum1(0) == 0);
    }
}
