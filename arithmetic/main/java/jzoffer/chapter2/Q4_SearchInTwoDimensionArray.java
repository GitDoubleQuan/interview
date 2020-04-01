package jzoffer.chapter2;

import org.junit.Assert;
import org.junit.Test;

/**
 * 在一个二维数组中，每一行的元素都从左往右递增，
 * 每一列的元素都从上往下递增，输入一个这样的二维数组和
 * 一个指定的值，查找该二维数组中是否存在该值。
 * <p>
 * 解题关键：从数组的右上角或者左下角开始查找，这样才能每次排除
 * 一列或者一行，缩小查找范围。
 * <p>
 * Created by xushuangquan on 2020-01-02.
 */
public class Q4_SearchInTwoDimensionArray {

    private boolean approach(int[][] array, int num) {
        if (array == null || array.length == 0) {
            throw new IllegalArgumentException("param is invalid");
        }

        int row = array.length;
        int columns = array[0].length;

        int i = 0;
        int j = columns - 1;
        while (i <= row - 1 && j >= 0) {
            if (array[i][j] == num) {
                return true;
            }
            if (array[i][j] > num) {
                j--;
            } else {
                i++;
            }
        }
        return false;
    }

    @Test
    public void test() {
        int[][] array = new int[][]{
                {1, 2, 8, 9},
                {2, 4, 9, 12},
                {4, 7, 10, 13},
                {6, 8, 11, 15}
        };

        Assert.assertTrue(approach(array, 8));
    }
}
