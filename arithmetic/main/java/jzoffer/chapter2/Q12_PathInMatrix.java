package jzoffer.chapter2;

import org.junit.Assert;
import org.junit.Test;

/**
 * 题目：请设计一个函数，、用来判断在一个矩阵中是否存在一条包含某
 * 字符串所有字符的路径。路径可以从矩阵中的任意一格开始，每一步可以
 * 在矩阵中向左、右、上、下移动一格。如果一条路径经过了矩阵的某一格，
 * 那么该路径不能再次进入该格子。例如，在下面的3×4的矩阵中包含一条
 * 字符串"bfce”的路径（路径中的字母用下画线标出但矩阵中不包含字
 * 符串"abfb”的路径，因为字符串的第一个字符b占据了矩阵中的第一行第
 * 二个格子之后，路径不能再次进入这个格子。
 * {'a', 'b', 't', 'g'},
 * {'c', 'f', 'c', 's'},
 * {'j', 'd', 'e', 'h'}
 * <p>
 * Created by xushuangquan on 2020/3/3.
 */
public class Q12_PathInMatrix {

    private boolean pathInMatrix(char[][] matrix, String path) {
        boolean[][] isStep = new boolean[matrix.length][matrix[0].length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                return hasPath(matrix, path, i, j, 1, isStep);
            }
        }
        return false;
    }

    private boolean hasPath(char[][] matrix, String path, int i, int j, int step, boolean[][] isStep) {
        if (step > path.length()) {
            return true;
        }

        if (i < 0 || i > matrix.length - 1 || j < 0 || j > matrix[i].length - 1 || isStep[i][j]) {
            return false;
        }

        boolean hasPath = false;
        if (matrix[i][j] == path.charAt(step - 1) && !isStep[i][j]) {
            step++;
            isStep[i][j] = true;
            hasPath = hasPath(matrix, path, i - 1, j, step, isStep)
                    || hasPath(matrix, path, i + 1, j, step, isStep)
                    || hasPath(matrix, path, i, j - 1, step, isStep)
                    || hasPath(matrix, path, i, j + 1, step, isStep);
            if (!hasPath) {
                step--;
                isStep[i][j] = false;
            }

        }
        return hasPath;
    }


    @Test
    public void test() {
        char[][] matrix = new char[][]{
                {'a', 'b', 't', 'g'},
                {'c', 'f', 'c', 's'},
                {'j', 'd', 'e', 'h'}
        };

        Assert.assertTrue(pathInMatrix(matrix, "abtg"));
        Assert.assertFalse(pathInMatrix(matrix, "abfb"));
    }

}
