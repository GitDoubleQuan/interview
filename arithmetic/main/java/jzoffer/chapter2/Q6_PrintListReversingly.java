package jzoffer.chapter2;

import jzoffer.datastructure.SingleLinkedNode;
import org.junit.Test;

import java.util.Stack;

/**
 * 面试题6：从尾到头打印链表
 * 题目：输入一个链表的头结点，从尾到头反过来打印出每个结点的值。
 * Created by xushuangquan on 2020/2/21.
 */
public class Q6_PrintListReversingly {

    /**
     * 递归方式
     * @param rootNode
     */
    private void recursivePrintListReversingly(SingleLinkedNode rootNode){
        if(rootNode == null){
            return;
        }
        if(rootNode.next != null){
            recursivePrintListReversingly(rootNode.next);
        }
        System.out.println(rootNode.value);
    }

    /**
     * 循环的方式，栈替代递归
     * @param rootNode
     */
    private void loopPrintListReversingly(SingleLinkedNode rootNode){
        if(rootNode == null){
            return;
        }
        Stack stack = new Stack();
        while (rootNode != null){
            stack.push(rootNode);
            rootNode = rootNode.next;
        }
        while (!stack.isEmpty()){
            SingleLinkedNode pop = (SingleLinkedNode) stack.pop();
            System.out.println(pop.value);
        }
    }



    @Test
    public void test(){
        SingleLinkedNode singleLinkedNode = new SingleLinkedNode();
        singleLinkedNode.value = 0;
        SingleLinkedNode rootNode = singleLinkedNode;
        int length = 0;
        while (length++ < 10) {
            singleLinkedNode.next = new SingleLinkedNode();
            singleLinkedNode.next.value = length;
            singleLinkedNode = singleLinkedNode.next;
        }

        recursivePrintListReversingly(rootNode);

        loopPrintListReversingly(rootNode);

    }


}
