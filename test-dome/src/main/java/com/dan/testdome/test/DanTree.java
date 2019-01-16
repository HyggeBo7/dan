package com.dan.testdome.test;

/**
 * @fileName: DanTree
 * @author: Dan
 * @createDate: 2018-10-11 17:03.
 * @description:
 */
public class DanTree {

    int data;

    /**
     * 左子树
     */
    DanTree left;
    /**
     * 右子树
     */
    DanTree right;

    /**
     * 实例化二叉树类
     *
     * @param data
     */
    public DanTree(int data) {
        this.data = data;
        this.left = null;
        this.right = null;
    }

    /**
     * 向二叉树中插入子节点
     */
    public void insert(DanTree root, int data) {
        //二叉树的左节点都比根节点小
        if (data > root.data) {
            if (root.right == null) {
                root.right = new DanTree(data);
            } else {
                this.insert(root.right, data);
            }
        } else {
            //二叉树的右节点都比根节点大
            if (root.left == null) {
                root.left = new DanTree(data);
            } else {
                this.insert(root.left, data);
            }
        }
    }
}
