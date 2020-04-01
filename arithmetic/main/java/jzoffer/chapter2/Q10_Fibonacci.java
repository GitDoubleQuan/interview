package jzoffer.chapter2;

import org.junit.Assert;
import org.junit.Test;

/**
 * 斐波那契数列题目
 * f(n) = n == 0 ? 0 : n == 1 ? 1 : f(n-1) + f(n-2)
 * Created by xushuangquan on 2020/2/23.
 */
public class Q10_Fibonacci {


    /**
     * 求斐波那契数列的第 n 项
     * 从上往下递归的方式，因为存在大量的重复计算，性能很差，时间复杂度为n的指数
     *
     * @param n
     * @return
     */
    private int recursionFibonacci(int n) {
        return n == 0 ? 0 : n == 1 ? 1 : recursionFibonacci(n - 1) + recursionFibonacci(n - 2);
    }

    /**
     * 求斐波那契数列的第 n 项
     * 自下而上以循环的方式计算，没有重复计算的问题，效率高，时间复杂度为n
     *
     * @param n
     * @return
     */
    private int loopFibonacci(int n) {
        if (n == 0 || n == 1) {
            return n;
        }
        int prevOfPrev = 0;
        int prev = 1;
        int fn = 0;
        for (int i = 2; i <= n; i++) {
            fn = prevOfPrev + prev;
            prevOfPrev = prev;
            prev = fn;
        }
        return fn;
    }


    /**
     * 青蛙跳台、方格覆盖这类题目本质上都是斐波那契
     * 还有一些变种
     * f(n) = f(n - 1) + f(n - 3)，
     * f(n) = max(f(n - 1), f(n - 2)), f(n) = min(f(n - 1), f(n - 2))等等
     */


    @Test
    public void test() {
        Assert.assertTrue(recursionFibonacci(10) == loopFibonacci(10));
        Assert.assertTrue(recursionFibonacci(0) == loopFibonacci(0));
        Assert.assertTrue(recursionFibonacci(15) == loopFibonacci(15));
    }

}
