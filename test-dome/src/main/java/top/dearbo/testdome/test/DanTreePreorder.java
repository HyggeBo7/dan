package top.dearbo.testdome.test;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @fileName: DanTreePreorder
 * @author: bo
 * @createDate: 2018-10-11 17:18.
 * @description:
 */
public class DanTreePreorder {

    public static void preOrder(DanTree root) {  //先根遍历
        if (root != null) {
            System.out.print(root.data + "-");
            preOrder(root.left);
            preOrder(root.right);
        }
    }

    public static void inOrder(DanTree root) {     //中根遍历

        if (root != null) {
            inOrder(root.left);
            System.out.print(root.data + "--");
            inOrder(root.right);
        }
    }

    public static void postOrder(DanTree root) {    //后根遍历

        if (root != null) {
            postOrder(root.left);
            postOrder(root.right);
            System.out.print(root.data + "---");
        }
    }

    public static void main(String[] args) {
        int[] array = {12, 76, 35, 22, 16, 48, 90, 46, 9, 40};
        //创建二叉树
        DanTree root = new DanTree(array[0]);
        for (int i = 1; i < array.length; i++) {
            //向二叉树中插入数据
            root.insert(root, array[i]);
        }
        System.out.println("先根遍历：");
        preOrder(root);
        System.out.println();
        System.out.println("中根遍历：");
        inOrder(root);
        System.out.println();
        System.out.println("后根遍历：");
        postOrder(root);
        ConcurrentHashMap<Integer, DanTree> webSocketMap = new ConcurrentHashMap();
        CopyOnWriteArraySet<DanTree> webSocketSet = new CopyOnWriteArraySet<>();
    }
}
