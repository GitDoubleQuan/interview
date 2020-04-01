package sort;

import org.junit.Test;

/**
 * Created by xushuangquan on 2020/2/24.
 */
public class QuickSort {

    private int[] quickSort(int[] arr, int start, int end) {
        if (arr == null || arr.length == 0 || start > end) {
            throw new IllegalArgumentException();
        }

        int l = start;
        int r = end;

        int mark = arr[(l + r) / 2];

        while (l < r) {
            while (arr[l] < mark) {
                l++;
            }
            while (arr[r] > mark) {
                r--;
            }
            if(l >= r){
                break;
            }
            int temp = arr[l];
            arr[l] = arr[r];
            arr[r] = temp;
            //为了防止数组中有重复元素，并且重复元素被选为基准的情况
            if (arr[l] == mark) {
                r--;
            }
            if (arr[r] == mark) {
                l++;
            }
        }
        if (l == r) {
            l++;
            r--;
        }
        if (r > start) {
            quickSort(arr, start, r);
        }
        if (end > l) {
            quickSort(arr, l, end);
        }
        return arr;
    }


    @Test
    public void test() {
        int[] arr = {1, 4, 3, 0, 9, 7, 5, 4};
        int[] quickSort = quickSort(arr, 0, arr.length - 1);
        for (int i : quickSort) {
            System.out.println(i);
        }
    }
}
