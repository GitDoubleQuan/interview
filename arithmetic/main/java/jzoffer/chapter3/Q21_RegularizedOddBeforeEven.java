package jzoffer.chapter3;

import org.junit.Assert;
import org.junit.Test;

/**
 * 题目：输入一个整数数组，实现一个函数来调整该数组中数字的顺序，
 * 使得所有奇数位于数组的前半部分，所有偶数位于数组的后半部分。
 * <p>
 * Created by xushuangquan on 2020/4/20.
 */
public class Q21_RegularizedOddBeforeEven {
    //    4 3 2 2 1 5 8 7
    //    7 3 2 2 1 5 8 4
    //    7 3 5 2 1 2 8 4
    //    7 3 5 1 2 2 8 4

    /**
     * 快排的思想解决该问题，两个指针相向而走，找到则奇偶对调，终止的标志是指针相遇
     * 时间复杂度On
     * @param origin
     * @return
     */
    private int[] regularizedOddBeforeEven(int[] origin) {
        if (origin == null || origin.length == 0) {
            throw new IllegalArgumentException();
        }
        int head = 0;
        int tail = origin.length - 1;
        while (tail > head) {
            while (head < origin.length && origin[head] % 2 != 0) {
                head++;
            }
            while (tail > 0 && origin[tail] % 2 == 0) {
                tail--;
            }
            if (tail > head) {
                int temp = origin[head];
                origin[head] = origin[tail];
                origin[tail] = temp;
            }
        }
        return origin;
    }

    private boolean check(int[] nums) {
        boolean even = false;

        for (int num : nums) {
            if ((num & 1) == 1) {
                if (even) {
                    return false;
                }
            } else {
                even = true;
            }
        }

        return true;
    }


    @Test
    public void test() {
        int[][] testCases = new int[][]{
                new int[]{1, 2, 3, 4, 5, 6, 7, 8},
                new int[]{1},
                new int[]{1, 1, 1, 1, 1},
                new int[]{2, 2, 2, 2, 2, 2, 2},
        };

        for (int[] testCase : testCases) {
            regularizedOddBeforeEven(testCase);
            Assert.assertTrue(check(testCase));
        }
    }
}
