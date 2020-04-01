package jzoffer.chapter2;

import jzoffer.datastructure.BinaryTreeNode;
import org.junit.Assert;
import org.junit.Test;

/**
 * 面试题8：二叉树的下一个结点
 * 题目：给定一棵二叉树和其中的一个结点，如何找出中序遍历顺序的下一个结点？
 * 树中的结点除了有两个分别指向左右子结点的指针以外，还有一个指向父结点的指针。
 * Created by xushuangquan on 2020/2/23.
 */
public class Q8_NextNodeInBinaryTrees {

    /**
     * 1。如果给定的节点有右子树，则中序遍历的下一个节点为右子树的最左叶子节点
     * 2.如果给定的节点没有右子树，且该节点为其父节点的左子节点，则中序遍历的下一个节点为其父节点
     * 3.如果给定的节点没有右子树，且该节点为其父节点的右子节点，则中序遍历的下一个节点为：
     * 从该节点往上寻找，直到找到是其父节点的左子节点的节点，然后返回找到的节点的父节点
     * @param node
     * @return
     */
    private BinaryTreeNode nextNodeInBinaryTrees(BinaryTreeNode node){
        if(node == null){
            throw new IllegalArgumentException();
        }

        if(node.right != null){
            node = node.right;
            while (node.left != null){
                node = node.left;
            }
            return node;
        }

        while (node.father != null){
            if(node.father.left == node){
                return node.father;
            }
            node = node.father;
        }

        return null;
    }


    /**
     * 使用多维数组表示二叉树，然后构建出 java 对象
     */
    private BinaryTreeNode buildTree(Object[] tree) {
        BinaryTreeNode node = new BinaryTreeNode();
        node.value = (char) tree[0];

        if (tree.length == 1) {
            return node;
        }

        if (tree[1] != null) {
            node.left = buildTree((Object[]) tree[1]);
            node.left.father = node;
        }

        if (tree.length == 2) {
            return node;
        }

        if (tree[2] != null) {
            node.right = buildTree((Object[]) tree[2]);
            node.right.father = node;
        }

        return node;
    }

    @Test
    public void test() {
        Object[] tree = new Object[] {
                'a',
                new Object[] {
                        'b', new Object[] {'d'}, new Object[] {'e', new Object[] {'h'}, new Object[] {'i'}
                }
                },
                new Object[] {
                        'e',
                        new Object[] {'f'},
                        new Object[] {'g'}
                }
        };

        BinaryTreeNode node = buildTree(tree);
        Assert.assertTrue(nextNodeInBinaryTrees(node).right == null);
        Assert.assertTrue(nextNodeInBinaryTrees(node.left).value == 'h');
        Assert.assertTrue(nextNodeInBinaryTrees(node).value == 'f');
        Assert.assertTrue(nextNodeInBinaryTrees(node.left.right.right).value == 'a');
        Assert.assertTrue(nextNodeInBinaryTrees(node.right.right) == null);
    }

}
