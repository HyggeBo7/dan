package top.dearbo.util.tree;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by Dan on 2017/10/9.
 */
public class TreeNodeUtil {

    /**
     * 构建树形集合对象-默认根节点为null或者小于1
     *
     * @param dataList 查找的集合
     * @return 树结构
     */
    public static <T extends TreeNodeUtil.TreeNodeImpl> List<T> listBuildChildTreeNode(List<T> dataList) {
        List<T> rootTreeList = new ArrayList<>();
        for (T node : dataList) {
            if (node.getParentNodeId() == null || node.getParentNodeId() < 1) {
                //优化-可以把当前root删除掉
                node.setChildrenList(listBuildChildTreeNode(dataList, node.getNodeId()));
                rootTreeList.add(node);
            }
        }
        return rootTreeList;
    }

    /**
     * 构建树形集合对象-指定根节点
     *
     * @param allList  所有节点
     * @param parentId 父节点id
     * @date 2017-9-26
     * @author Dan
     */
    public static <T extends TreeNodeUtil.TreeNodeImpl> List<T> listBuildChildTreeNode(List<T> allList, Integer parentId) {
        List<T> listParentNode = new ArrayList<>();
        List<T> listNotParentNode = new ArrayList<>();
        // allList，找出所有的根节点和非根节点
        if (allList != null && allList.size() > 0) {
            for (T node : allList) {
                // 对比找出父节点
                if (Objects.equals(node.getParentNodeId(), parentId)) {
                    listParentNode.add(node);
                } else {
                    listNotParentNode.add(node);
                }
            }
        }
        // 查询子节点
        if (listParentNode.size() > 0) {
            for (T record : listParentNode) {
                // 递归查询子节点
                record.setChildrenList(listBuildChildTreeNode(listNotParentNode, record.getNodeId()));
            }
        }
        return listParentNode;
    }

    /**
     * 递归查找某个节点下面的所有子节点
     *
     * @param parentId 父节点
     * @param dataList 查找的集合
     * @return
     */
    public static <T extends TreeNodeUtil.TreeNodeImpl> List<T> getBuildChildFindNodes(List<T> dataList, Integer parentId) {
        List<T> newTreeList = new ArrayList<>();
        for (T node : dataList) {
            if (Objects.equals(node.getParentNodeId(), parentId)) {
                node.setChildrenList(getBuildChildFindNodes(dataList, node.getNodeId()));
                newTreeList.add(node);
            }
        }
        return newTreeList;
    }

    /**
     * 通过继承该类,实现设置当前节点和父节点
     *
     * @param <T> 泛型必须继承AbstractTreeNode或者实现AbstractTreeNodeImpl
     */
    public abstract static class AbstractTreeNode<T> implements Serializable, TreeNodeImpl<T> {

        /**
         * 子列表
         */
        private List<T> childrenList = new ArrayList<>();

        public List<T> getChildrenList() {
            return childrenList;
        }

        @Override
        public void setChildrenList(List<T> childrenList) {
            this.childrenList = childrenList;
        }

    }

    /**
     * 通过实现该接口,实现设置子列表,当前节点，父节点
     *
     * @param <T> 泛型必须实现TreeNodeImpl接口
     */
    public interface TreeNodeImpl<T> {

        /**
         * 子列表
         *
         * @param childrenList 子列表
         */
        void setChildrenList(List<T> childrenList);

        /**
         * 当前节点id
         *
         * @return 节点id
         */
        Integer getNodeId();

        /**
         * 当前节点父id
         *
         * @return 节点父id
         */
        Integer getParentNodeId();
    }

}
