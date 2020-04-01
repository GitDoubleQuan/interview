package jzoffer.chapter2;

import org.junit.Assert;
import org.junit.Test;

/**
 * 题目：地上有一个m行列的方格。一个机器人从坐标（0，0）的格子开
 * 始移动，它每次可以向左、右、上、下移动一格，但不能进入行坐标和列
 * 坐标的数位之和大于々的格子。例如，当为18时，机器人能够进入方格
 * （35，37），、因为3+5+3+7=18。但它不能进入方格（35，38），因为3+5+3+8=19。
 * 请问该机器人能够到达多少个格子？
 * <p>
 * Created by xushuangquan on 2020/3/5.
 */
public class Q13_RobotMove {

    private int robotMove(int[][] matrix, int k) {
        boolean[][] isStep = new boolean[matrix.length][matrix[0].length];
        return moveCount(matrix, k, 0, 0, isStep);
    }

    private int moveCount(int[][] matrix, int k, int i, int j, boolean[][] isStep) {
        if (check(matrix, k, i, j, isStep)) {
            isStep[i][j] = true;
            return 1 + moveCount(matrix, k, i - 1, j, isStep)
                    + moveCount(matrix, k, i + 1, j, isStep)
                    + moveCount(matrix, k, i, j - 1, isStep)
                    + moveCount(matrix, k, i, j + 1, isStep);
        }
        return 0;
    }

    private boolean check(int[][] matrix, int k, int i, int j, boolean[][] isStep) {
        if (i < 0 || i > matrix.length - 1 || j < 0 || j > matrix[i].length - 1
                || isStep[i][j] || getPosSum(matrix[i][j]) > k) {
            return false;
        }
        return true;
    }

    private int getPosSum(int i) {
        int sum = 0;
        while (i > 0) {
            sum += i % 10;
            i = i / 10;
        }
        return sum;
    }

    @Test
    public void test() {
        int[][] matrix = new int[][]{
                {0, 1, 2, 3},
                {4, 5, 6, 7},
                {8, 9, 10, 12}
        };
        Assert.assertTrue(robotMove(matrix, 5) == 6);
        Assert.assertTrue(robotMove(matrix, 9) == 12);
    }


}
