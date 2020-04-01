package jzoffer.chapter2;

import org.junit.Assert;
import org.junit.Test;

/**
 * 题目：给你一根长度为 n 的绳子，请把绳子剪成 m 段、m，n 都是整数，
 * n > 1 并且 m > 1），每段绳子的长度记为 k[0]，k[1]，..., k[m]。请问 k[0] * k[1] * ... * k[m]
 * 可能的最大乘积是多少？例如，当绳子的长度是8时，我们把它剪成长
 * 度分别为2、3、3的三段，此时得到的最大乘积是 18。
 * <p>
 * Created by xushuangquan on 2020/3/7.
 */
public class Q14_CutString {

    /**
     * 动态规划
     * 动态规划问题的特点
     * 1.求一个问题的最优解
     * 2.大问题可以分解成子问题，大问题的最优解依赖于子问题的最优解
     * 3.子问题之间有重叠的更小的子问题
     *
     * @param n
     * @return
     */
    private int cutString(int n) {
        if (n <= 1) {
            throw new IllegalArgumentException();
        }
        //当绳子剩余长度小于等于3的时候，绳子一刀不剪才是最大值，
        //但是当绳子的长度小于等于3的时候，又必须剪至少一刀
        if (n == 2) {
            return 1;
        }
        if (n == 3) {
            return 2;
        }
        int[] products = new int[n + 1];
        products[0] = 0;
        products[1] = 1;
        products[2] = 2;
        products[3] = 3;
        for (int i = 4; i <= n; i++) {
            int max = 0;
            for (int j = 1; j <= i / 2; j++) {
                max = Math.max(products[j] * products[i - j], max);
            }
            products[i] = max;
        }
        return products[n];
    }

    /**
     * 贪婪算法
     * 当绳子长度大于5的时候，每次尽可能多的截取长度为3的小段，
     * 当剩下的绳子长度为4的时候，截取两端长度为2的小段。
     *
     * @return
     */
    private int cutString1(int n) {
        if (n <= 1) {
            throw new IllegalArgumentException();
        }
        if (n == 2) {
            return 1;
        }
        if (n == 3) {
            return 2;
        }
        int sizeOf3 = n / 3;
        if (n % 3 == 1) {
            sizeOf3--;
        }
        return (int) Math.pow(3, sizeOf3) * (n - 3 * sizeOf3);
    }


    @Test
    public void test() {
        Assert.assertTrue(cutString(8) == 18);
        Assert.assertTrue(cutString1(8) == 18);
    }

}
