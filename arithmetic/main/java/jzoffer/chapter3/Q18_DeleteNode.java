package jzoffer.chapter3;

import jzoffer.datastructure.SingleLinkedNode;

/**
 * 题目一：在o(l)时间内删除链表节点。
 * 给定单向链表的头指针和一个节点指针，定义一个函数在o(1)时间内删除该节点。
 * <p>
 * Created by xushuangquan on 2020/3/15.
 */
public class Q18_DeleteNode {


    /**
     * 把要删除的节点的值替换成下一个节点的值，然后删除下一节点即可
     * 注意如果要删除的节点是最后一个节点，依然只能从头往后遍历
     *
     * @param head
     * @param deleteNode
     */
    private void deleteNode(SingleLinkedNode head, SingleLinkedNode deleteNode) {
        if (head == null || deleteNode == null) {
            throw new IllegalArgumentException();
        }
        //链表只有一个节点
        if (head.next == null && head == deleteNode) {
            head = null;
            return;
        }
        //删除的是尾结点
        if (deleteNode.next == null) {
            SingleLinkedNode preNode = head;
            while (preNode.next != deleteNode) {
                preNode = preNode.next;
            }
            preNode.next = null;
            return;
        }
        deleteNode.value = deleteNode.next.value;
        deleteNode.next = deleteNode.next.next;

    }

}
