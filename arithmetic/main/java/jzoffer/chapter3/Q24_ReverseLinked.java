package jzoffer.chapter3;

import jzoffer.datastructure.SingleLinkedNode;
import org.junit.Test;

/**
 * 题目：定义一个函数，输入一个链表的头节点，反转该链表并输出反
 * 转后链表的头节点。链表节点定义如下：
 *
 * Created by xushuangquan on 2020/4/25.
 */
public class Q24_ReverseLinked {

    private SingleLinkedNode reverseLinked(SingleLinkedNode head) {
        if (head == null) {
            throw new IllegalArgumentException();
        }

        SingleLinkedNode beforeIndex = head;
        SingleLinkedNode middleIndex = null;
        SingleLinkedNode afterIndex = null;
        if(head.next != null){
            middleIndex = head.next;
        }
        if(middleIndex == null){
            return beforeIndex;
        }
        if(middleIndex.next != null){
            afterIndex = middleIndex.next;
        }

        middleIndex.next = beforeIndex;
        beforeIndex.next = null;

        while(afterIndex != null){
            beforeIndex = middleIndex;
            middleIndex = afterIndex;
            afterIndex = afterIndex.next;
            middleIndex.next = beforeIndex;
        }

        return middleIndex;
    }

    @Test
    public void test(){
        SingleLinkedNode headNode = SingleLinkedNode.build("1 > 2 > 3 > 4 > 5 > 6");
        System.out.println(reverseLinked(headNode).serialize());
        SingleLinkedNode headNode1 = SingleLinkedNode.build("1");
        System.out.println(reverseLinked(headNode1).serialize());
    }
}
