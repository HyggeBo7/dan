package com.dan.utils.tree;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dan on 2017/10/9.
 */
public class TreeNode<T> {
    /**
     * 节点id
     */
    private Integer nodeId;
    /**
     * 父节点id
     */
    private Integer parentNodeId;
    /**
     * 节点名称
     */
    private String nodeName;
    /**
     * 节点对象
     */
    private T treeNode;
    /**
     * 子列表
     */
    private List<T> childrenList = new ArrayList<>();

    public Integer getNodeId() {
        return nodeId;
    }

    public void setNodeId(Integer nodeId) {
        this.nodeId = nodeId;
    }

    public Integer getParentNodeId() {
        return parentNodeId;
    }

    public void setParentNodeId(Integer parentNodeId) {
        this.parentNodeId = parentNodeId;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public T getTreeNode() {
        return treeNode;
    }

    public void setTreeNode(T treeNode) {
        this.treeNode = treeNode;
    }

    public List<T> getChildrenList() {
        return childrenList;
    }

    public void setChildrenList(List<T> childrenList) {
        this.childrenList = childrenList;
    }
}
