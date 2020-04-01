package jzoffer.chapter2;

import org.junit.Assert;
import org.junit.Test;

import java.util.Stack;

/**
 * 面试题9：用两个栈实现队列
 * 题目：用两个栈实现一个队列。队列的声明如下，请实现它的两个函数appendTail
 * 和deleteHead，分别完成在队列尾部插入结点和在队列头部删除结点的功能。
 * Created by xushuangquan on 2020/2/23.
 */
public class Q9_QueueWithTwoStacks {

    /**
     * 添加元素：往同一个stack1中压入元素
     * 删除元素：从同一个stack2中删除元素，因为stack中元素都是从stack1中转移过来的，所以元素在stack2中的顺序已经是按照
     * 队列排序的了，直接从stack2的栈顶弹出即可，当stack2弹完了，再从stack1转移转移元素
     */
    private class StackQueue implements Queue{

        private Stack<Integer> stack1 = new Stack<>();
        private Stack<Integer> stack2 = new Stack<>();

        @Override
        public void appendTail(int i) {
            stack1.push(i);
        }

        @Override
        public int deleteHead() {

            if(stack1.isEmpty() && stack2.isEmpty()){
                return -1;
            }

            if(!stack2.isEmpty()){
                return stack2.pop();
            }

            while (!stack1.isEmpty()){
                stack2.push(stack1.pop());
            }
            return stack2.pop();
        }

        @Override
        public int[] toArray() {
            int[] array = new int[stack1.size() + stack2.size()];
            int i = 0;
            while (!stack1.isEmpty() || !stack2.isEmpty()){
                array[i] = deleteHead();
                i++;
            }
            return array;
        }


    }


    @Test
    public void test() {
        Queue queue = new StackQueue();
        queue.deleteHead();
        queue.appendTail(7);
        queue.appendTail(0);
        queue.appendTail(0);
        queue.appendTail(1);
        queue.appendTail(2);
        queue.appendTail(3);
        queue.appendTail(4);
        queue.appendTail(5);
        queue.appendTail(6);
        queue.deleteHead();
        queue.deleteHead();
        int[] elements = queue.toArray(); // 0, 1, 2, 3, 4, 5, 6

        for (int i = 0; i < elements.length; i++) {
            System.out.println(elements[i]);
            Assert.assertTrue(elements[i] == i);
        }
    }

    interface Queue {
        void appendTail(int i);
        int deleteHead();
        int[] toArray();
    }
}
