package jzoffer.chapter2;

import org.junit.Assert;
import org.junit.Test;

/**
 * 题目：把一个数组最开始的若干个元素搬到数组的末尾，我们称之为
 * 数组的旋转。输入一个递增排序的数组的一个旋转，输出旋转数组的最小
 * 元素。例如，数组{3，4，5，1，2}为{1，2，3，4，5}的一个旋转，该数组的最小
 * 值为1。
 * <p>
 * Created by xushuangquan on 2020/2/27.
 */
public class Q11_FindMinimumInReversedArray {

    private int findMinimumInReversedArray(int[] arr) {
        if (arr == null || arr.length == 0) {
            throw new IllegalArgumentException();
        }

        if (arr[0] < arr[arr.length - 1]) {
            return arr[0];
        }

        int l = 0;
        int r = arr.length - 1;
        while (r - l > 1) {
            int mid = (l + r) / 2;
            if (arr[l] == arr[r] && arr[l] == arr[mid]) {
                int i = l + 1;
                int result = arr[l];
                while (i <= r) {
                    if (arr[i] <= result) {
                        result = arr[i];
                    }
                    i++;
                }
                return result;
            }
            if (arr[mid] >= arr[l]) {
                l = mid;
            }
            if (arr[mid] <= arr[r]) {
                r = mid;
            }
        }
        return arr[r];
    }

    @Test
    public void test() {
        int[] reserved = {3, 4, 5, 1, 2};
        Assert.assertTrue(findMinimumInReversedArray(reserved) == 1);
        reserved = new int[]{1, 2, 3, 4, 5};
        Assert.assertTrue(findMinimumInReversedArray(reserved) == 1);
        reserved = new int[]{1, 1, 1, 1, 1, 1, 1};
        Assert.assertTrue(findMinimumInReversedArray(reserved) == 1);
    }

}
