package jzoffer.chapter2;

import org.junit.Assert;
import org.junit.Test;

/**
 * 找出数组中重复的数字。
 * 在一个长度为”的数组里的所有数字都在0～“]的范围内数组中某
 * 些数字是重复的，但不知道有几个数字重复了，也不知道每个数字重复了
 * 几次9请找出数组中任意一个重复的数字。例如，如果输入长度为7的数
 * 组{2，3，1，0，2，5，3}，那么对应的输出是重复的数字2或者3。
 * <p>
 * Created by xushuangquan on 2019-12-31.
 */
public class Q3_RepeatNumInArray {

    /**
     * 时间复杂度O(n),空间复杂度O(1)
     *
     * @param array
     * @return
     */
    private int approach(int[] array) {

        if (array == null || array.length == 0) {
            throw new IllegalArgumentException("param is invalid");
        }

        //并没有必要开辟一个临时变量，直接交换是更加高效的方式
        int tmp = -1;
        for (int i = 0; i < array.length; i++) {

            if (array[i] == i) {
                continue;
            }

            tmp = array[i];
            array[i] = -1;

            while (tmp != -1) {
                if (tmp == array[tmp]) {
                    return tmp;
                }

                if (array[tmp] == -1) {
                    array[tmp] = tmp;
                    tmp = -1;
                } else {
                    int x = tmp;
                    tmp = array[x];
                    array[x] = x;
                }
            }
        }
        return -1;
    }


    /**
     * 不用每次从数组中取出来的值都举着了，直接以交换的方式也可实现同样的效果
     *
     * @param array
     * @return
     */
    private int approach1(int[] array) {
        if (array == null || array.length == 0) {
            throw new IllegalArgumentException("param is invalid");
        }

        for (int i = 0; i < array.length; i++) {
            if (i == array[i]) {
                continue;
            }
            while (i != array[i]) {
                if (array[i] == array[array[i]]) {
                    return array[i];
                }
                int tmp = array[i];
                array[i] = array[tmp];
                array[tmp] = tmp;
            }

        }

        throw new IllegalArgumentException("param is invalid");
    }






    /**
     * 数组长度为m,元素的取值范围是1~m-1，所以一定会存在重复元素，
     * 不改变数组，找出其中任意一个重复元素。
     *
     * 不开辟O（n）的额外空间，时间换空间，借助二分查找的思路
     */
    class Q3_1_RepeatNumInArray{
        private int approach2(int[] array) {
            if (array == null || array.length == 0) {
                throw new IllegalArgumentException("param is invalid");
            }
            int length = array.length;
            int start = 1;
            int end = length - 1;
            while (start <= end) {
                int middle = (end - start) / 2 + start;
                int count = countRange(array, length, start, middle);


                if (start == end) {
                    return count > 1 ? start : -1;
                }

                if (middle - start + 1 < count) {
                    end = middle;
                } else {
                    start = middle + 1;
                }

            }
            return -1;
        }

        private int countRange(int[] array, int length, int start, int end) {
            if (array == null || array.length == 0) {
                throw new IllegalArgumentException("param is invalid");
            }
            int count = 0;
            for (int i = 0; i < length; i++) {
                if (array[i] >= start && array[i] <= end) {
                    count++;
                }
            }
            return count;
        }

        public void test(){
            int[] array2 = new int[]{2, 3, 1, 6, 2, 5, 3};
            int repeat2 = approach2(array2);
            System.out.println(repeat2);
            Assert.assertTrue(repeat2 == 2 || repeat2 == 3);
        }
    }

    @Test
    public void test() {
        int[] array = new int[]{2, 3, 1, 0, 2, 5, 3};
        int repeat = approach(array);
        System.out.println(repeat);
        Assert.assertTrue(repeat == 2 || repeat == 3);
        int repeat1 = approach1(array);
        System.out.println(repeat1);
        Assert.assertTrue(repeat1 == 2 || repeat1 == 3);

        Q3_1_RepeatNumInArray q3_1_repeatNumInArray = new Q3_1_RepeatNumInArray();
        q3_1_repeatNumInArray.test();
    }
}
