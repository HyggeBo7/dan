package com.dan.utils.tree;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dan on 2017/10/9.
 */
public class TreeNodeUtil {

    /**
     * 递归查找某个节点下面的所有子节点
     *
     * @param parentId 父节点
     * @param dataList 查找的集合
     * @return
     */
    public static List<TreeNode> getBuildChildRecordNode(List<TreeNode> dataList, Integer parentId) {
        List<TreeNode> newTreeList = new ArrayList<>();
        for (TreeNode node : dataList) {
            if (node.getParentNodeId() == null) {
                continue;
            }
            if (node.getParentNodeId().equals(parentId)) {
                node.setChildrenList(getBuildChildRecordNode(dataList, node.getNodeId()));
                newTreeList.add(node);
            }
        }
        return newTreeList;
    }

    /**
     * 构建树形集合对象
     *
     * @param dataList 查找的集合
     * @return
     */
    public static List<TreeNode> getTreeChildRecordNode(List<TreeNode> dataList) {
        List<TreeNode> newTreeList = new ArrayList<>();
        for (TreeNode node : dataList) {
            if (node.getParentNodeId() == null || node.getParentNodeId() < 1) {
                node.setChildrenList(getBuildChildRecordNode(node.getNodeId(), dataList));
                newTreeList.add(node);
            }
        }
        return newTreeList;
    }

    /**
     * 说明方法描述：递归查询子节点
     *
     * @param allList  所有节点
     * @param parentId 父节点id
     * @time 2017-9-26
     * @author Dan
     */
    public static List<TreeNode> getBuildChildRecordNode(Integer parentId, List<TreeNode> allList) {
        List<TreeNode> listParentRecord = new ArrayList<>();
        List<TreeNode> listNotParentRecord = new ArrayList<>();
        // allList，找出所有的根节点和非根节点
        if (allList != null && allList.size() > 0) {
            for (TreeNode record : allList) {
                // 对比找出父节点
                if (record.getParentNodeId() != null && record.getParentNodeId().equals(parentId)) {
                    listParentRecord.add(record);
                } else {
                    listNotParentRecord.add(record);
                }
            }
        }
        // 查询子节点
        if (listParentRecord.size() > 0) {
            for (TreeNode record : listParentRecord) {
                // 递归查询子节点
                record.setChildrenList(getBuildChildRecordNode(record.getNodeId(), listNotParentRecord));
            }
        }
        return listParentRecord;
    }


}
