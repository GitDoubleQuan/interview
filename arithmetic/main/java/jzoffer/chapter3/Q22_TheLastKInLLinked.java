package jzoffer.chapter3;

import jzoffer.datastructure.SingleLinkedNode;

/**
 * 题目：输入一个链表，输出该链表中倒数第k个节点。为了符合大多
 * 数人的习惯，本题从1开始计数，即链表的尾节点是倒数第1个节点。例
 * 如，一个链表有6个节点，从头节点开始，它们的值依次是1、2、3、4、5、
 * 6。这个链表的倒数第3个节点是值为4的节点。链表节点定义如下：
 * <p>
 * Created by xushuangquan on 2020/4/20.
 */
public class Q22_TheLastKInLLinked {

    private int getLastKInLinked(SingleLinkedNode root, int k) {
        if (root == null) {
            throw new IllegalArgumentException();
        }
        SingleLinkedNode before = root;
        SingleLinkedNode after = root;
        for (int i = 1; i < k; i++) {
            if(after.next == null){
                throw new IllegalArgumentException("k不能超过链表的长度！");
            }
            after = after.next;
        }
        while (after.next != null) {
            after = after.next;
            before = before.next;
        }
        return before.value;
    }

}
