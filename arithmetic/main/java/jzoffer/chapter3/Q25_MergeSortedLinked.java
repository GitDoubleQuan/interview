package jzoffer.chapter3;

import jzoffer.datastructure.SingleLinkedNode;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by xushuangquan on 2020/4/25.
 */
public class Q25_MergeSortedLinked {

    /**
     * 插入法的方式合并两个链表
     *
     * @param link1
     * @param link2
     * @return
     */
    private SingleLinkedNode mergeSortedLinked(SingleLinkedNode link1, SingleLinkedNode link2) {
        if (link1 == null || link2 == null) {
            return link1 == null ? link2 : link1;
        }

        SingleLinkedNode mergeLinked = link1.value <= link2.value ? link1 : link2;
        SingleLinkedNode beforeIndex = null;
        SingleLinkedNode afterIndex = link2;

        while (link1 != null) {
            SingleLinkedNode insertNode = link1;
            link1 = link1.next;
            while (afterIndex != null && insertNode.value > afterIndex.value) {
                afterIndex = afterIndex.next;
                beforeIndex = beforeIndex == null ? link2 : beforeIndex.next;
            }
            if (afterIndex != null) {
                insertNode.next = afterIndex;
                if (beforeIndex != null) {
                    beforeIndex.next = insertNode;
                }
                beforeIndex = insertNode;
            } else {
                beforeIndex.next = insertNode;
                break;
            }
        }
        return mergeLinked;
    }

    /**
     * 两条链表往一条新链表上合并
     *
     * @param link1
     * @param link2
     * @return
     */
    private SingleLinkedNode mergeSortedLinked1(SingleLinkedNode link1, SingleLinkedNode link2) {
        if (link1 == null || link2 == null) {
            return link1 == null ? link2 : link1;
        }
        SingleLinkedNode mergeLink = new SingleLinkedNode();
        mergeCore(mergeLink, link1, link2);
        return mergeLink.next;
    }

    private void mergeCore(SingleLinkedNode mergeLink, SingleLinkedNode link1, SingleLinkedNode link2) {
        if (link1 == null || link2 == null) {
            mergeLink.next = link1 == null ? link2 : link1;
            return;
        }
        if (link1.value <= link2.value) {
            mergeLink.next = link1;
            mergeCore(mergeLink.next, link1.next, link2);
        } else {
            mergeLink.next = link2;
            mergeCore(mergeLink.next, link1, link2.next);
        }
    }

    @Test
    public void test() {
        SingleLinkedNode linkedNode1 = SingleLinkedNode.build("1 > 2 > 4 > 5 > 7 > 8 > 9");
        SingleLinkedNode linkedNode2 = SingleLinkedNode.build("3 > 5 > 6");
//        System.out.println(mergeSortedLinked1(linkedNode1, linkedNode2).serialize());
        Assert.assertTrue(mergeSortedLinked1(linkedNode1, linkedNode2).serialize().equals("1 > 2 > 3 > 4 > 5 > 5 > 6 > 7 > 8 > 9"));
//        SingleLinkedNode linkedNode1 = SingleLinkedNode.build("1 > 1 > 2 > 4 > 5 > 7 > 8 > 9");
//        SingleLinkedNode linkedNode2 = SingleLinkedNode.build("3 > 5 > 6 > 10");
////        Assert.assertTrue(mergeSortedLinked(linkedNode1, linkedNode2).serialize().equals("1 > 1 > 2 > 3 > 4 > 5 > 5 > 6 > 7 > 8 > 9 > 10"));
//        SingleLinkedNode linkedNode1 = SingleLinkedNode.build("1");
//        SingleLinkedNode linkedNode2 = SingleLinkedNode.build("1");
//        Assert.assertTrue(mergeSortedLinked(linkedNode1, linkedNode2).serialize().equals("1 > 1"));
//        SingleLinkedNode linkedNode1 = SingleLinkedNode.build("1");
//        Assert.assertTrue(mergeSortedLinked(linkedNode1, null).serialize().equals("1"));
//        Assert.assertTrue(mergeSortedLinked(null, linkedNode1).serialize().equals("1"));
    }
}
