package jzoffer.chapter2;

import jzoffer.datastructure.BinaryTreeNode;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * 面试题7：重建二叉树
 * 题目：输入某二叉树的前序遍历和中序遍历的结果，请重建出该二叉树。假设输
 * 入的前序遍历和中序遍历的结果中都不含重复的数字。例如输入前序遍历序列{1,
 * 2, 4, 7, 3, 5, 6, 8}和中序遍历序列{4, 7, 2, 1, 5, 3, 8, 6}，则重建出
 * 图2.6所示的二叉树并输出它的头结点。
 * Created by xushuangquan on 2020/2/22.
 */
public class Q7_ConstructBinaryTree {

    /**
     * 1.preorder中的第一个值是root节点，先构建出根节点，
     * 2.inorder中，root节点的左边都是左子树的节点，右边都是右子树的节点
     * 3.root节点的左子节点在preorder中root节点左边第一个，root节点的右子节点在preorder中左边跳过所有左子树节点的第一个
     * 至此一个子树构建完毕，按照这种方式递归构建完整二叉树
     * <p>
     * 涉及的变量：
     * 1.preorder中第一个元素的位置
     * 2.inorder的开始位置和结束位置
     */
    private BinaryTreeNode constructBinaryTree(int[] preorder, int i, int[] inorder, int start, int end) {

        if(preorder == null || inorder == null || preorder.length != inorder.length || preorder.length == 0){
            throw new IllegalArgumentException();
        }
        if(start > end){
            return null;
        }
        int rootVal = preorder[i];
        BinaryTreeNode root = new BinaryTreeNode();
        root.value = rootVal;
        int indexOfInorder = findIndexOfInorder(rootVal, inorder);
        root.left = constructBinaryTree(preorder, i + 1, inorder, start, indexOfInorder - 1);
        root.right = constructBinaryTree(preorder, i + (indexOfInorder - start) + 1, inorder, indexOfInorder + 1, end);
        return root;
    }

    /**
     * 寻找任意值在inorder中的位置
     *
     * @param value
     * @param inorder
     * @return
     */
    private int findIndexOfInorder(int value, int[] inorder) {
        int i = 0;
        while (i < inorder.length) {
            if (value == inorder[i]) {
                return i;
            }
            i++;
        }
        return -1;
    }

    @Test
    public void test() {
        int[] preOrder = new int[] {1, 2, 4, 7, 3, 5, 6, 8};
        int[] inOrder = new int[] {4, 7, 2, 1, 5, 3, 8, 6};
        BinaryTreeNode node = constructBinaryTree(preOrder, 0, inOrder, 0, inOrder.length - 1);
        Assert.assertTrue(Arrays.equals(Arrays.stream(node.recursionPreOrder(new ArrayList<>())).mapToInt(i->i).toArray() , preOrder));
        Assert.assertTrue(Arrays.equals(Arrays.stream(node.recursionInOrder(new ArrayList<>())).mapToInt(i->i).toArray() , inOrder));
    }
}
