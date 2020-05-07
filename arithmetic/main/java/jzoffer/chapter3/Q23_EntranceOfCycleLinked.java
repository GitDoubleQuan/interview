package jzoffer.chapter3;

import jzoffer.datastructure.SingleLinkedNode;
import org.junit.Assert;
import org.junit.Test;

/**
 * 题目：如果一个链表中包含环，如何找出环的入口节点？例如，在如
 * 图3．8所示的链表中，环的入口节点是节点 3.
 *
 * Created by xushuangquan on 2020/4/23.
 */
public class Q23_EntranceOfCycleLinked {

    /**
     * 1.通过快慢指针的方式判断链表中是否有环，快指针一次走两步，慢指针一次走一步，如果链表中有环的话
     * 那么一定会相遇在环中的某一点，
     * 2.此时快指针走了x步，慢指针走了y步，y - x = k * cl，还是不知道cl有多少个节点
     * 3.但是快慢指针相遇的点一定在环中，可以让指针从相遇的点开始再走一圈就能计数的到环的长度
     * 4.现在的到了环的长度，让两个指正间隔环的长度，以相同的速度往前走，前后指正相遇的点就是环的入口
     *
     * @param headNode
     * @return
     */
    private SingleLinkedNode entranceOfCycleLinked(SingleLinkedNode headNode){
        if(headNode == null){
            throw new IllegalArgumentException();
        }
        //此处要注意：快指正的起始位置要在慢指针前面，防止快指正在还未进入环的位置就追上了慢指针
        SingleLinkedNode fastIndex = headNode.next;
        SingleLinkedNode slowIndex = headNode;
        SingleLinkedNode indexOfLoop = null;

        while (fastIndex != null && slowIndex != null) {
            if(fastIndex == slowIndex){
                indexOfLoop = fastIndex;
                break;
            }

            fastIndex = fastIndex.next;
            if(fastIndex != null){
                fastIndex = fastIndex.next;
            }
            slowIndex = slowIndex.next;
        }

        if(indexOfLoop == null){
            return null;
        }

        int cl = 1;
        SingleLinkedNode countIndex = indexOfLoop.next;
        while (countIndex != indexOfLoop) {
            countIndex = countIndex.next;
            cl++;
        }

        SingleLinkedNode beforeIndex = headNode;
        SingleLinkedNode afterIndex = headNode;
        for(int i = 0; i < cl; i++){
            afterIndex = afterIndex.next;
        }

        while (beforeIndex != afterIndex) {
            beforeIndex = beforeIndex.next;
            afterIndex = afterIndex.next;
        }

        return beforeIndex;
    }


    @Test
    public void test() {
        SingleLinkedNode linked = SingleLinkedNode.build("1 > 2 > 3 > 4 > 5 > 6 > 7 > 8");
        SingleLinkedNode tail = linked;

        while (tail.next != null) {
            tail = tail.next;
        }

        for (int i = 1 ; i <= 8; i++) {
            SingleLinkedNode entrance = linked;
            int entranceIdx = i;

            while (entranceIdx-- > 1) {
                entrance = entrance.next;
            }

            tail.next = entrance;

            Assert.assertTrue(entranceOfCycleLinked(linked).value == i);
        }

        tail.next = null;
        Assert.assertTrue(entranceOfCycleLinked(linked) == null);
        linked.next = linked;
        Assert.assertTrue(entranceOfCycleLinked(linked) == linked);
        Assert.assertTrue(entranceOfCycleLinked(linked) == linked);
    }
}
