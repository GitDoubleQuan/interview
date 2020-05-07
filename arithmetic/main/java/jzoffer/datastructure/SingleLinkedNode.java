package jzoffer.datastructure;

/**
 * Created by xushuangquan on 2020/2/21.
 */
public class SingleLinkedNode {
    public int value;
    public SingleLinkedNode next;


    /**
     * 构建链表, 格式
     * 1 > 2 > 3 > 4 > 5 > 6
     *
     * @param linked
     * @return
     */
    public static SingleLinkedNode build(String linked) {
        if (linked == null) {
            return null;
        }

        String[] nodes = linked.split(">");

        if (nodes.length == 0) {
            return null;
        }

        SingleLinkedNode head = new SingleLinkedNode();
        head.value = Integer.valueOf(nodes[0].trim());
        SingleLinkedNode node = head;

        for (int i = 1; i < nodes.length; i ++) {
            node.next = new SingleLinkedNode();
            node.next.value = Integer.valueOf(nodes[i].trim());
            node = node.next;
        }

        return head;
    }


    /**
     * 序列化链表， 输出格式：1 > 2 > 3 > 4 > 5 > 6
     */
    public String serialize() {
        SingleLinkedNode node = this;
        StringBuilder sb = new StringBuilder();

        while (node != null) {
            sb.append(node.value + ((node = node.next) != null ? " > " : ""));
        }

        return sb.toString();
    }
}
