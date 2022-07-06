package top.dearbo.frame.common.test.entity;

import top.dearbo.util.tree.TreeNodeUtil;

import java.util.Date;

/**
 * @author: bo
 * @date 2017/10/9
 */
public class TreeNode extends TreeNodeUtil.AbstractTreeNode<Integer, TreeNode> {
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

	public TreeNode(Integer nodeId, Integer parentNodeId, String nodeName, Date createDate) {
		this.nodeId = nodeId;
		this.parentNodeId = parentNodeId;
		this.nodeName = nodeName;
		this.createDate = createDate;
	}

	public TreeNode() {
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
