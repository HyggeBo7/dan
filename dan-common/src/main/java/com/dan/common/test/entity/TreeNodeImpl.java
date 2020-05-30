package com.dan.common.test.entity;

import com.dan.util.tree.TreeNodeUtil;

import java.util.Date;
import java.util.List;

/**
 * @author Dan
 * @date 2017/10/9
 */
public class TreeNodeImpl implements TreeNodeUtil.TreeNodeImpl<TreeNodeImpl> {
    /**
     * 节点id
     */
    private Integer nodeId;
    /**
     * 父节点id
     */
    private Integer parentNodeId;

    private String nodeName;

    private Date createDate;

    private List<TreeNodeImpl> childrenList;

    public void setNodeId(Integer nodeId) {
        this.nodeId = nodeId;
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

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public List<TreeNodeImpl> getChildrenList() {
        return childrenList;
    }

    public TreeNodeImpl(Integer nodeId, Integer parentNodeId, String nodeName, Date createDate) {
        this.nodeId = nodeId;
        this.parentNodeId = parentNodeId;
        this.nodeName = nodeName;
        this.createDate = createDate;
    }

    public TreeNodeImpl() {
    }

    @Override
    public void setChildrenList(List<TreeNodeImpl> childrenList) {
        this.childrenList = childrenList;
    }

    @Override
    public Integer getNodeId() {
        return this.nodeId;
    }

    @Override
    public Integer getParentNodeId() {
        return this.parentNodeId;
    }
}
