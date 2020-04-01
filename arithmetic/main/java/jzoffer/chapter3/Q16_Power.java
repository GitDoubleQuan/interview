package jzoffer.chapter3;

import org.junit.Assert;
import org.junit.Test;

/**
 * 题目：实现函数doublePower(doublebase，intexponent)，求base的
 * exponent次方。、不得使用库函数，同时不需要考虑大数问题。
 * <p>
 * Created by xushuangquan on 2020/3/8.
 */
public class Q16_Power {
    /**
     * 效率O(n)的解法
     *
     * @param base
     * @param exponent
     * @return
     */
    private double power(double base, int exponent) {
        if (base == 0) {
            throw new IllegalArgumentException("base is 0!");
        }
        if (exponent == 0) {
            return 0;
        }
        int absExponent = exponent;
        if (exponent < 0) {
            absExponent = -exponent;
        }
        double result = base;
        while (absExponent > 1) {
            result *= base;
            absExponent--;
        }
        if (exponent < 0) {
            result = 1 / result;
        }
        return result;
    }

    /**
     * 效率O(logn)的解法
     *
     * @param base
     * @param exponent
     * @return
     */
    private double power1(double base, int exponent) {
        if (base == 0) {
            throw new IllegalArgumentException("base is 0!");
        }
        if (exponent == 0) {
            return 1;
        }
        if (exponent == 1) {
            return base;
        }
        if (exponent == -1) {
            return 1 / base;
        }
        double result = power1(base, exponent >> 1);
        return (exponent & 1) == 1 ? result * result * base : result * result;
    }


    @Test
    public void test(){
        Assert.assertTrue(power(2,3) == 8);
        Assert.assertTrue(power1(2,3) == 8);
        Assert.assertTrue(power(-12,-3) == Math.pow(-12,-3));
        Assert.assertTrue(power1(-2,-3) == Math.pow(-2,-3));
        Assert.assertTrue(power1(2, 0) == Math.pow(2,0));
        Assert.assertTrue(power1(2,0) == Math.pow(2,0));
    }
}
