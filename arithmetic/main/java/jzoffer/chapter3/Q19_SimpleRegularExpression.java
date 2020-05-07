package jzoffer.chapter3;

import org.junit.Assert;
import org.junit.Test;

/**
 * 题目：请实现一个函数用来匹配包含'.'和'*' 的正则表达式。模式中的字
 * 符 '.' 表示任意一个字符，而 * 表示它前面的字符可以出现任意次（含0次）。
 * 在本题中，匹配是指字符串的所有字符匹配整个模式。例如，字符串"aaa"
 * 与模式"a.a"和"ab*ac*a"匹配，但与"aa．a"和"ab*a"均不匹配。
 * <p>
 * Created by xushuangquan on 2020/4/3.
 */
public class Q19_SimpleRegularExpression {

    private boolean regularMatch(String str, String pattern) {
        if (str == null || pattern == null) {
            throw new IllegalArgumentException();
        }
        return matchCore(str, pattern, 0, 0);
    }

    private boolean matchCore(String str, String pattern, int index1, int index2) {
        if (index1 == str.length() - 1 && index2 == pattern.length() - 1) {
            return true;
        }
        if (index1 < str.length() - 1 && index2 == pattern.length() - 1) {
            return false;
        }
        //todo:漏洞在于str已经到达末尾，但是pattern还没有到达末尾

        boolean isSameChar = false;
        try {
            isSameChar = str.charAt(index1) == pattern.charAt(index2) || pattern.charAt(index2) == '.';
        }catch (Exception e){
            throw new RuntimeException(str + " " + pattern);
        }

        if (pattern.charAt(index2 + 1) == '*') {
            if (isSameChar) {
                return matchCore(str, pattern, index1 + 1, index2)
                        || matchCore(str, pattern, index1 + 1, index2 + 2)
                        || matchCore(str, pattern, index1, index2 + 2);
            } else {
                return matchCore(str, pattern, index1, index2 + 2);
            }
        }
        if (isSameChar) {
            return matchCore(str, pattern, index1 + 1, index2 + 1);
        } else {
            return false;
        }
    }

    @Test
    public void test() {
        String[][] testCases = new String[][]{
//                new String[]{"aaa", "aaa", "true"},
                new String[]{"ab", ".*..", "true"},
//                new String[]{"aaa", "a*", "true"},
//                new String[]{"aaa", "a*b*", "true"},
//                new String[]{"aaa", "a*c*b*d*", "true"},
//                new String[]{"aaac", "a*c*b*d*", "true"},
//                new String[]{"aaacbd", "a*c*b*d*", "true"},
//                new String[]{"aaabd", "a*c*b*d*", "true"},
//                new String[]{"aaad", "a*c*b*d*", "true"},
//                new String[]{"cccbd", "a*c*b*d*", "true"},
//                new String[]{"ddddd", "a*c*b*d*", "true"},
//                new String[]{"cccccd", "a*c*b*d*", "true"},
//                new String[]{"cccccbd", "a*c*b*d*", "true"},
//                new String[]{"abcd", "abcd", "true"},
//                new String[]{"abcdddd", "abcd*", "true"},
//                new String[]{"", "a*", "true"},
//                new String[]{"", "a*", "true"},
//                new String[]{"", ".", "false"},
//                new String[]{"aaa", "a*b", "false"},
//                new String[]{"aaa", "aaab", "false"},
//                new String[]{"aaa", "a*b", "false"},
//                new String[]{"aaac", "a*b*cd", "false"},
//                new String[]{"aaac", "a*b*c*d", "false"},
//                new String[]{"bbbccc", "a*b*c*d", "false"},
//                new String[]{"cdd", "a*b*c*d", "false"},
//                new String[]{"aba", "a*b*", "false"},
//                new String[]{"abac", "a*b*a*cd", "false"},
        };

        for (String[] testCase : testCases) {
            boolean b = String.valueOf(regularMatch(testCase[0], testCase[1])) == testCase[2];

            if (!b) {
                System.out.println(testCase[0] + ", " + testCase[1] + ", " + testCase[2]);
            }

            Assert.assertTrue(b);
        }
    }

}
