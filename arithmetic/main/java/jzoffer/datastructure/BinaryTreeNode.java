package jzoffer.datastructure;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Created by xushuangquan on 2020/2/22.
 */
public class BinaryTreeNode {
    public int value;
    public BinaryTreeNode left;
    public BinaryTreeNode right;
    public BinaryTreeNode father;

    /**
     * 中序遍历-stack
     * @return
     */
    public Integer[] loopInOrder(){
        BinaryTreeNode node = this;
        List<Integer> result = new ArrayList<>();
        Stack<BinaryTreeNode> stack = new Stack<>();
        stack.add(node);
        boolean addLeft = true;

        while (!stack.isEmpty()) {
            node = stack.peek();

            if (addLeft && node.left != null) {
                stack.add(node.left);
                continue;
            }

            result.add(stack.pop().value);

            if (addLeft = (node.right != null)) {
                stack.add(node.right);
            }
        }

        return result.toArray(new Integer[result.size()]);
    }


    /**
     * 中序遍历-递归
     * @param result
     * @return
     */
    public Integer[] recursionInOrder(List<Integer> result){
        BinaryTreeNode node = this;
        if(node.left != null){
            node.left.recursionInOrder(result);
        }
        result.add(node.value);
        if(node.right != null){
            node.right.recursionInOrder(result);
        }
        return result.toArray(new Integer[result.size()]);
    }


    /**
     * 前序遍历-递归
     * @param result
     * @return
     */
    public Integer[] recursionPreOrder(List<Integer> result){
        BinaryTreeNode node = this;
        result.add(node.value);
        if (node.left != null){
            node.left.recursionPreOrder(result);
        }
        if (node.right != null){
            node.right.recursionPreOrder(result);
        }
        return result.toArray(new Integer[result.size()]);
    }

    /**
     * 前序遍历-stack
     * @return
     */
    public Integer[] loopPreOrder(){
        BinaryTreeNode node = this;
        ArrayList<Integer> result = new ArrayList<>();
        Stack<BinaryTreeNode> stack = new Stack<>();
        stack.push(node);
        while (!stack.isEmpty()){
            BinaryTreeNode root = stack.pop();
            result.add(root.value);
            if(root.right != null){
                stack.push(root.right);
            }
            if(root.left != null){
                stack.push(root.left);
            }
        }
        return result.toArray(new Integer[result.size()]);
    }

}
