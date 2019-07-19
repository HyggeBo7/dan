package com.dan.common.test;

import com.alibaba.fastjson.JSONObject;
import com.dan.common.test.entity.TreeNodeImpl;
import com.dan.common.test.entity.UserInfo;
import com.dan.common.util.excel.ExcelWriteUtils;
import com.dan.common.util.pdf.FreemarkerUtils;
import com.dan.utils.JsonUtil;
import com.dan.utils.entity.AjaxResult;
import com.dan.utils.file.FileUtil;
import com.dan.utils.lang.DateUtil;
import com.dan.utils.network.HttpUtils;
import com.dan.common.test.entity.TreeNode;
import com.dan.utils.tree.TreeNodeUtil;
import com.dan.utils.xt.MapperUtil;
import com.dan.utils.xt.Pagination;
import com.dan.utils.xt.PinYinUtil;
import org.joda.time.DateTime;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @fileName: Test
 * @author: Dan
 * @createDate: 2018-12-20 10:13.
 * @description:
 */
public class DomeTest {

    @Test
    public void testListToTreeNodeImpl() {
        //打乱顺序
        String json = "[{\"nodeId\":21,\"parentNodeId\":13,\"nodeName\":\"名称:13-21\",\"createDate\":\"2019-07-19 13:22:15\",\"childrenList\":[]},{\"nodeId\":11,\"parentNodeId\":9,\"nodeName\":\"名称:5-9-11\",\"createDate\":\"2019-07-19 13:22:15\",\"childrenList\":[]},{\"nodeId\":2,\"parentNodeId\":1,\"nodeName\":\"名称:1-2\",\"createDate\":\"2019-07-19 13:22:55\",\"childrenList\":[]},{\"nodeId\":3,\"parentNodeId\":2,\"nodeName\":\"名称:1-2-3\",\"createDate\":\"2019-07-19 13:22:45\",\"childrenList\":[]},{\"nodeId\":5,\"parentNodeId\":0,\"nodeName\":\"名称:0-5\",\"createDate\":\"2019-07-19 13:22:55\",\"childrenList\":[]},{\"nodeId\":6,\"parentNodeId\":5,\"nodeName\":\"名称:5-6\",\"createDate\":\"2019-07-19 13:22:45\",\"childrenList\":[]},{\"nodeId\":4,\"parentNodeId\":2,\"nodeName\":\"名称:1-2-4\",\"createDate\":\"2019-07-19 13:22:35\",\"childrenList\":[]},{\"nodeId\":7,\"parentNodeId\":6,\"nodeName\":\"名称:5-6-7\",\"createDate\":\"2019-07-19 13:22:35\",\"childrenList\":[]},{\"nodeId\":9,\"parentNodeId\":5,\"nodeName\":\"名称:5-9\",\"createDate\":\"2019-07-19 13:22:35\",\"childrenList\":[]},{\"nodeId\":8,\"parentNodeId\":6,\"nodeName\":\"名称:5-6-8\",\"createDate\":\"2019-07-19 13:22:25\",\"childrenList\":[]},{\"nodeId\":10,\"parentNodeId\":9,\"nodeName\":\"名称:5-9-10\",\"createDate\":\"2019-07-19 13:22:25\",\"childrenList\":[]},{\"nodeId\":12,\"parentNodeId\":9,\"nodeName\":\"名称:5-9-12\",\"createDate\":\"2019-07-19 13:22:05\",\"childrenList\":[]},{\"nodeId\":13,\"parentNodeId\":0,\"nodeName\":\"名称:0-13\",\"createDate\":\"2019-07-19 13:22:45\",\"childrenList\":[]},{\"nodeId\":14,\"parentNodeId\":13,\"nodeName\":\"名称:13-14\",\"createDate\":\"2019-07-19 13:22:35\",\"childrenList\":[]},{\"nodeId\":15,\"parentNodeId\":14,\"nodeName\":\"名称:13-14-15\",\"createDate\":\"2019-07-19 13:22:25\",\"childrenList\":[]},{\"nodeId\":16,\"parentNodeId\":14,\"nodeName\":\"名称:13-14-16\",\"createDate\":\"2019-07-19 13:22:15\",\"childrenList\":[]},{\"nodeId\":17,\"parentNodeId\":13,\"nodeName\":\"名称:13-17\",\"createDate\":\"2019-07-19 13:22:25\",\"childrenList\":[]},{\"nodeId\":18,\"parentNodeId\":17,\"nodeName\":\"名称:13-17-18\",\"createDate\":\"2019-07-19 13:22:15\",\"childrenList\":[]},{\"nodeId\":1,\"parentNodeId\":0,\"nodeName\":\"名称:0-1\",\"createDate\":\"2019-07-19 13:23:05\",\"childrenList\":[]},{\"nodeId\":19,\"parentNodeId\":17,\"nodeName\":\"名称:13-17-19\",\"createDate\":\"2019-07-19 13:22:05\",\"childrenList\":[]},{\"nodeId\":20,\"parentNodeId\":17,\"nodeName\":\"名称:13-17-20\",\"createDate\":\"2019-07-19 13:21:55\",\"childrenList\":[]},{\"nodeId\":22,\"parentNodeId\":21,\"nodeName\":\"名称:13-21-22\",\"createDate\":\"2019-07-19 13:22:05\",\"childrenList\":[]},{\"nodeId\":23,\"parentNodeId\":21,\"nodeName\":\"名称:13-21-23\",\"createDate\":\"2019-07-19 13:21:55\",\"childrenList\":[]},{\"nodeId\":24,\"parentNodeId\":21,\"nodeName\":\"名称:13-21-24\",\"createDate\":\"2019-07-19 13:21:45\",\"childrenList\":[]},{\"nodeId\":25,\"parentNodeId\":21,\"nodeName\":\"名称:13-21-25\",\"createDate\":\"2019-07-19 13:21:35\",\"childrenList\":[]}]";
        List<TreeNodeImpl> treeNodeList = JsonUtil.fromListJson(json, TreeNodeImpl.class);
        List<TreeNodeImpl> listBuildChildTreeNode = TreeNodeUtil.listBuildChildTreeNode(treeNodeList);
        System.out.println("Impl-listBuildChildTreeNode:" + JsonUtil.toJson(listBuildChildTreeNode));
        System.out.println("Impl-listBuildChildTreeNode.size():" + listBuildChildTreeNode.size());
    }

    @Test
    public void testListToTreeNode() {
        /*List<TreeNode> treeNodeList = new ArrayList<>();
        TreeNode treeNode;
        int idAll = 0;
        for (int i = 1; i <= 3; i++) {
            idAll++;
            treeNode = new TreeNode(idAll, 0, "名称:0-" + idAll, DateUtil.getDate(DateUtil.getTime() - (i * 10000)));
            treeNodeList.add(treeNode);
            int iId = idAll;
            for (int j = 1; j < (i + 1); j++) {
                idAll++;
                treeNode = new TreeNode(idAll, iId, "名称:" + iId + "-" + idAll, DateUtil.getDate(DateUtil.getTime() - ((i + j) * 10000)));
                treeNodeList.add(treeNode);
                int jId = idAll;
                for (int z = 1; z < (j + 2); z++) {
                    idAll++;
                    treeNode = new TreeNode(idAll, jId, "名称:" + iId + "-" + jId + "-" + idAll, DateUtil.getDate(DateUtil.getTime() - ((i + j + z) * 10000)));
                    treeNodeList.add(treeNode);
                }
            }
        }*/
        //打乱顺序
        String json = "[{\"nodeId\":21,\"parentNodeId\":13,\"nodeName\":\"名称:13-21\",\"createDate\":\"2019-07-19 13:22:15\",\"childrenList\":[]},{\"nodeId\":11,\"parentNodeId\":9,\"nodeName\":\"名称:5-9-11\",\"createDate\":\"2019-07-19 13:22:15\",\"childrenList\":[]},{\"nodeId\":2,\"parentNodeId\":1,\"nodeName\":\"名称:1-2\",\"createDate\":\"2019-07-19 13:22:55\",\"childrenList\":[]},{\"nodeId\":3,\"parentNodeId\":2,\"nodeName\":\"名称:1-2-3\",\"createDate\":\"2019-07-19 13:22:45\",\"childrenList\":[]},{\"nodeId\":5,\"parentNodeId\":0,\"nodeName\":\"名称:0-5\",\"createDate\":\"2019-07-19 13:22:55\",\"childrenList\":[]},{\"nodeId\":6,\"parentNodeId\":5,\"nodeName\":\"名称:5-6\",\"createDate\":\"2019-07-19 13:22:45\",\"childrenList\":[]},{\"nodeId\":4,\"parentNodeId\":2,\"nodeName\":\"名称:1-2-4\",\"createDate\":\"2019-07-19 13:22:35\",\"childrenList\":[]},{\"nodeId\":7,\"parentNodeId\":6,\"nodeName\":\"名称:5-6-7\",\"createDate\":\"2019-07-19 13:22:35\",\"childrenList\":[]},{\"nodeId\":9,\"parentNodeId\":5,\"nodeName\":\"名称:5-9\",\"createDate\":\"2019-07-19 13:22:35\",\"childrenList\":[]},{\"nodeId\":8,\"parentNodeId\":6,\"nodeName\":\"名称:5-6-8\",\"createDate\":\"2019-07-19 13:22:25\",\"childrenList\":[]},{\"nodeId\":10,\"parentNodeId\":9,\"nodeName\":\"名称:5-9-10\",\"createDate\":\"2019-07-19 13:22:25\",\"childrenList\":[]},{\"nodeId\":12,\"parentNodeId\":9,\"nodeName\":\"名称:5-9-12\",\"createDate\":\"2019-07-19 13:22:05\",\"childrenList\":[]},{\"nodeId\":13,\"parentNodeId\":0,\"nodeName\":\"名称:0-13\",\"createDate\":\"2019-07-19 13:22:45\",\"childrenList\":[]},{\"nodeId\":14,\"parentNodeId\":13,\"nodeName\":\"名称:13-14\",\"createDate\":\"2019-07-19 13:22:35\",\"childrenList\":[]},{\"nodeId\":15,\"parentNodeId\":14,\"nodeName\":\"名称:13-14-15\",\"createDate\":\"2019-07-19 13:22:25\",\"childrenList\":[]},{\"nodeId\":16,\"parentNodeId\":14,\"nodeName\":\"名称:13-14-16\",\"createDate\":\"2019-07-19 13:22:15\",\"childrenList\":[]},{\"nodeId\":17,\"parentNodeId\":13,\"nodeName\":\"名称:13-17\",\"createDate\":\"2019-07-19 13:22:25\",\"childrenList\":[]},{\"nodeId\":18,\"parentNodeId\":17,\"nodeName\":\"名称:13-17-18\",\"createDate\":\"2019-07-19 13:22:15\",\"childrenList\":[]},{\"nodeId\":1,\"parentNodeId\":0,\"nodeName\":\"名称:0-1\",\"createDate\":\"2019-07-19 13:23:05\",\"childrenList\":[]},{\"nodeId\":19,\"parentNodeId\":17,\"nodeName\":\"名称:13-17-19\",\"createDate\":\"2019-07-19 13:22:05\",\"childrenList\":[]},{\"nodeId\":20,\"parentNodeId\":17,\"nodeName\":\"名称:13-17-20\",\"createDate\":\"2019-07-19 13:21:55\",\"childrenList\":[]},{\"nodeId\":22,\"parentNodeId\":21,\"nodeName\":\"名称:13-21-22\",\"createDate\":\"2019-07-19 13:22:05\",\"childrenList\":[]},{\"nodeId\":23,\"parentNodeId\":21,\"nodeName\":\"名称:13-21-23\",\"createDate\":\"2019-07-19 13:21:55\",\"childrenList\":[]},{\"nodeId\":24,\"parentNodeId\":21,\"nodeName\":\"名称:13-21-24\",\"createDate\":\"2019-07-19 13:21:45\",\"childrenList\":[]},{\"nodeId\":25,\"parentNodeId\":21,\"nodeName\":\"名称:13-21-25\",\"createDate\":\"2019-07-19 13:21:35\",\"childrenList\":[]}]";
        List<TreeNode> treeNodeList = JsonUtil.fromListJson(json, TreeNode.class);
        System.out.println("treeNodeList:" + JsonUtil.toJson(treeNodeList));
        System.out.println("treeNodeList.size():" + treeNodeList.size());
        List<TreeNode> listBuildChildTreeNode = TreeNodeUtil.listBuildChildTreeNode(treeNodeList);
        System.out.println("listBuildChildTreeNode:" + JsonUtil.toJson(listBuildChildTreeNode));
        System.out.println("listBuildChildTreeNode.size():" + listBuildChildTreeNode.size());

        List<TreeNode> treeNodeList1 = TreeNodeUtil.listBuildChildTreeNode(treeNodeList, 0);
        System.out.println("treeNodeList1:" + JsonUtil.toJson(listBuildChildTreeNode));
        System.out.println("treeNodeList1.size():" + listBuildChildTreeNode.size());

        List<TreeNode> buildChildFindNodes = TreeNodeUtil.getBuildChildFindNodes(treeNodeList, 5);
        System.out.println("buildChildFindNodes:" + JsonUtil.toJson(buildChildFindNodes));
        System.out.println("buildChildFindNodes.size():" + buildChildFindNodes.size());

    }

    @Test
    public void testListMapFilter() {
        List<Map<String, Object>> mapList = new ArrayList<>();
        Map<String, Object> map;
        for (int i = 1; i <= 20; i++) {
            map = new LinkedHashMap<>();
            map.put("id", i);
            map.put("name", "名称name:" + i);
            mapList.add(map);
        }
        for (int i = 1; i <= 20; i++) {
            map = new LinkedHashMap<>();
            map.put("id", i);
            map.put("name", "名称name:" + i);
            mapList.add(map);
        }
        //筛选,去重,获取指定值
        List<Object> objects = mapList.stream().filter(d -> d.get("id").equals(1)).distinct().map(d -> d.get("id")).collect(Collectors.toList());
        for (Object object : objects) {
            System.out.println("object:" + object);
        }
    }

    @Test
    public void testFile() {
        File file = new File("P:\\books");
        System.out.println("==========");
    }

    @Test
    public void testHtml() {
        String dir = "P:/file/baobiao";
        String htmlDir = dir + "/html/";
        String name = "增值税减免税申报明细表1.html";
        int size = 80;
        String str = "F00";
        Map<String, Object> dataMap = new HashMap<>();
        for (int i = 1; i <= size; i++) {
            if (i < 11) {
                dataMap.put(str + "0" + i, "value:" + i);
            } else {
                dataMap.put(str + i, "value:" + i);
            }
        }
        FreemarkerUtils.loadHtml(dir, name, dataMap, htmlDir + UUID.randomUUID() + ".html");
        System.out.println("======================");
    }

    @Test
    public void testDoule() {
        double num = 0.5569834;

        System.out.println(String.format("%.3f", num));

    }

    @Test
    public void testPinYin() {
        PinYinUtil pinYinUtil = new PinYinUtil();
        String str = pinYinUtil.getPinYinFirstChar("啦啦啦", "#");
        System.out.println("str:" + str);
    }

    @Test
    public void test01() {
        String dataJson = "[{\"sys_group_id_field\":1,\"payment_id\":13764,\"Custname\":\"飒拉商业（上海）有限公司广州汉溪大道分公司\",\"LL_Name\":\"广州番禺万达广场商业物业管理有限公司\",\"Pmt_Rate\":0.16,\"DescriptionKey\":\"elec\",\"Paid_Date\":\"2019-01-01\",\"Amount\":48716.2,\"inv_id\":20847,\"Custname_R\":\"飒拉商业（上海）有限公司广州汉溪大道分公司\",\"ClientName\":\"广州番禺万达广场商业物业管理有限公司\",\"Taxrate\":0.16,\"DescriptionKey_R\":\"elec\",\"Invdate\":\"2019-01-31\",\"TaxAmount\":48716.2,\"sys_match_type_field\":0,\"sys_match_comment_field\":null},{\"sys_group_id_field\":2,\"payment_id\":13844,\"Custname\":\"波丝可商业（北京）有限公司重庆时代天街分公司\",\"LL_Name\":\"重庆龙湖成恒地产开发有限公司\",\"Pmt_Rate\":0.05,\"DescriptionKey\":\"rental\",\"Paid_Date\":\"2019-01-15\",\"Amount\":34026.76,\"inv_id\":20764,\"Custname_R\":\"波丝可商业（北京）有限公司重庆时代天街分公司\",\"ClientName\":\"重庆龙湖成恒地产开发有限公司\",\"Taxrate\":0.05,\"DescriptionKey_R\":\"rental\",\"Invdate\":\"2019-01-21\",\"TaxAmount\":34026.76,\"sys_match_type_field\":0,\"sys_match_comment_field\":null},{\"sys_group_id_field\":3,\"payment_id\":13911,\"Custname\":\"普安倍尔商业（北京）有限公司重庆时代天街分公司\",\"LL_Name\":\"重庆龙湖成恒地产开发有限公司\",\"Pmt_Rate\":0.05,\"DescriptionKey\":\"rental\",\"Paid_Date\":\"2019-01-15\",\"Amount\":42724.11,\"inv_id\":20763,\"Custname_R\":\"普安倍尔商业（北京）有限公司重庆时代天街分公司\",\"ClientName\":\"重庆龙湖成恒地产开发有限公司\",\"Taxrate\":0.05,\"DescriptionKey_R\":\"rental\",\"Invdate\":\"2019-01-21\",\"TaxAmount\":42724.11,\"sys_match_type_field\":0,\"sys_match_comment_field\":null},{\"sys_group_id_field\":4,\"payment_id\":13948,\"Custname\":\"飒拉商业（北京）有限公司哈尔滨中兴大道第二分公司\",\"LL_Name\":\"哈尔滨哈西万达广场有限公司\",\"Pmt_Rate\":0.05,\"DescriptionKey\":\"rental\",\"Paid_Date\":\"2019-01-15\",\"Amount\":66325.56,\"inv_id\":20818,\"Custname_R\":\"飒拉商业（北京）有限公司哈尔滨中兴大道第二分公司\",\"ClientName\":\"哈尔滨哈西万达广场有限公司\",\"Taxrate\":0.05,\"DescriptionKey_R\":\"rental\",\"Invdate\":\"2019-01-22\",\"TaxAmount\":66325.56,\"sys_match_type_field\":0,\"sys_match_comment_field\":null},{\"sys_group_id_field\":5,\"payment_id\":13994,\"Custname\":\"飒拉商业（上海）有限公司武汉唐家墩路分公司\",\"LL_Name\":\"武汉万达广场投资有限公司\",\"Pmt_Rate\":0.05,\"DescriptionKey\":\"rental\",\"Paid_Date\":\"2019-01-15\",\"Amount\":91516.08,\"inv_id\":20832,\"Custname_R\":\"飒拉商业（上海）有限公司武汉唐家墩路分公司\",\"ClientName\":\"武汉万达广场投资有限公司\",\"Taxrate\":0.05,\"DescriptionKey_R\":\"rental\",\"Invdate\":\"2019-01-16\",\"TaxAmount\":91516.08,\"sys_match_type_field\":0,\"sys_match_comment_field\":null},{\"sys_group_id_field\":6,\"payment_id\":14035,\"Custname\":\"普安倍尔商业（北京）有限公司重庆时代天街分公司\",\"LL_Name\":\"重庆龙湖成恒地产开发有限公司\",\"Pmt_Rate\":0.16,\"DescriptionKey\":\"elec\",\"Paid_Date\":\"2019-01-15\",\"Amount\":7739.58,\"inv_id\":20766,\"Custname_R\":\"普安倍尔商业（北京）有限公司重庆时代天街分公司\",\"ClientName\":\"重庆龙湖成恒地产开发有限公司\",\"Taxrate\":0.16,\"DescriptionKey_R\":\"elec\",\"Invdate\":\"2019-01-21\",\"TaxAmount\":7739.58,\"sys_match_type_field\":0,\"sys_match_comment_field\":null},{\"sys_group_id_field\":7,\"payment_id\":14368,\"Custname\":\"麦西姆杜特商业（上海）有限公司常州通江中路分公司\",\"LL_Name\":\"常州万达广场商业管理有限公司\",\"Pmt_Rate\":0.16,\"DescriptionKey\":\"elec\",\"Paid_Date\":\"2019-02-01\",\"Amount\":9411.49,\"inv_id\":20828,\"Custname_R\":\"麦西姆杜特商业（上海）有限公司常州通江中路分公司\",\"ClientName\":\"常州万达广场商业管理有限公司\",\"Taxrate\":0.16,\"DescriptionKey_R\":\"elec\",\"Invdate\":\"2019-02-02\",\"TaxAmount\":9411.49,\"sys_match_type_field\":0,\"sys_match_comment_field\":null},{\"sys_group_id_field\":8,\"payment_id\":14434,\"Custname\":\"奥依修商贸（上海）有限公司常州通江中路分公司\",\"LL_Name\":\"常州万达广场商业管理有限公司\",\"Pmt_Rate\":0.16,\"DescriptionKey\":\"elec\",\"Paid_Date\":\"2019-02-01\",\"Amount\":5687.83,\"inv_id\":20825,\"Custname_R\":\"奥依修商贸（上海）有限公司常州通江中路分公司\",\"ClientName\":\"常州万达广场商业管理有限公司\",\"Taxrate\":0.16,\"DescriptionKey_R\":\"elec\",\"Invdate\":\"2019-02-02\",\"TaxAmount\":5687.83,\"sys_match_type_field\":0,\"sys_match_comment_field\":null},{\"sys_group_id_field\":9,\"payment_id\":14585,\"Custname\":\"飒拉家居商贸（上海）有限公司北京朝阳北路分公司\",\"LL_Name\":\"中粮地产集团深圳物业管理有限公司北京朝阳分公司\",\"Pmt_Rate\":0.16,\"DescriptionKey\":\"elec\",\"Paid_Date\":\"2019-02-01\",\"Amount\":14374.8,\"inv_id\":20912,\"Custname_R\":\"飒拉家居商贸（上海）有限公司北京朝阳北路分公司\",\"ClientName\":\"中粮地产集团深圳物业管理有限公司北京朝阳分公司\",\"Taxrate\":0.16,\"DescriptionKey_R\":\"elec\",\"Invdate\":\"2019-02-11\",\"TaxAmount\":14374.8,\"sys_match_type_field\":0,\"sys_match_comment_field\":null},{\"sys_group_id_field\":10,\"payment_id\":14635,\"Custname\":\"飒拉商业（上海）有限公司武汉唐家墩路分公司\",\"LL_Name\":\"武汉万达广场商业管理有限公司\",\"Pmt_Rate\":0.16,\"DescriptionKey\":\"elec\",\"Paid_Date\":\"2019-02-01\",\"Amount\":29312,\"inv_id\":20864,\"Custname_R\":\"飒拉商业（上海）有限公司武汉唐家墩路分公司\",\"ClientName\":\"武汉万达广场商业管理有限公司\",\"Taxrate\":0.16,\"DescriptionKey_R\":\"elec\",\"Invdate\":\"2019-02-01\",\"TaxAmount\":29312,\"sys_match_type_field\":0,\"sys_match_comment_field\":null},{\"sys_group_id_field\":11,\"payment_id\":14647,\"Custname\":\"飒拉商业（上海）有限公司常州通江中路分公司\",\"LL_Name\":\"常州万达广场商业管理有限公司\",\"Pmt_Rate\":0.16,\"DescriptionKey\":\"elec\",\"Paid_Date\":\"2019-02-01\",\"Amount\":39136.99,\"inv_id\":20831,\"Custname_R\":\"飒拉商业（上海）有限公司常州通江中路分公司\",\"ClientName\":\"常州万达广场商业管理有限公司\",\"Taxrate\":0.16,\"DescriptionKey_R\":\"elec\",\"Invdate\":\"2019-02-02\",\"TaxAmount\":39136.99,\"sys_match_type_field\":0,\"sys_match_comment_field\":null},{\"sys_group_id_field\":12,\"payment_id\":14736,\"Custname\":\"波丝可商业（北京）有限公司重庆时代天街分公司\",\"LL_Name\":\"重庆龙湖成恒地产开发有限公司\",\"Pmt_Rate\":0.05,\"DescriptionKey\":\"rental\",\"Paid_Date\":\"2019-02-15\",\"Amount\":44566.21,\"inv_id\":20771,\"Custname_R\":\"波丝可商业（北京）有限公司重庆时代天街分公司\",\"ClientName\":\"重庆龙湖成恒地产开发有限公司\",\"Taxrate\":0.05,\"DescriptionKey_R\":\"rental\",\"Invdate\":\"2019-02-13\",\"TaxAmount\":44566.21,\"sys_match_type_field\":0,\"sys_match_comment_field\":null},{\"sys_group_id_field\":13,\"payment_id\":14771,\"Custname\":\"波丝可商业（北京）有限公司长沙枫林三路分公司\",\"LL_Name\":\"步步高商业连锁股份有限公司梅溪湖分公司\",\"Pmt_Rate\":0.05,\"DescriptionKey\":\"rental\",\"Paid_Date\":\"2019-02-15\",\"Amount\":27891.6,\"inv_id\":20785,\"Custname_R\":\"波丝可商业（北京）有限公司长沙枫林三路分公司\",\"ClientName\":\"步步高商业连锁股份有限公司梅溪湖分公司\",\"Taxrate\":0.05,\"DescriptionKey_R\":\"rental\",\"Invdate\":\"2019-02-16\",\"TaxAmount\":27891.6,\"sys_match_type_field\":0,\"sys_match_comment_field\":null},{\"sys_group_id_field\":14,\"payment_id\":14775,\"Custname\":\"波丝可商业（北京）有限公司深圳滨河大道分公司\",\"LL_Name\":\"深圳市京基百纳商业管理有限公司京基百纳时代店\",\"Pmt_Rate\":0.05,\"DescriptionKey\":\"rental\",\"Paid_Date\":\"2019-02-15\",\"Amount\":50189.88,\"inv_id\":20888,\"Custname_R\":\"波丝可商业（北京）有限公司深圳滨河大道分公司\",\"ClientName\":\"深圳市京基百纳商业管理有限公司京基百纳时代店\",\"Taxrate\":0.05,\"DescriptionKey_R\":\"rental\",\"Invdate\":\"2019-02-12\",\"TaxAmount\":50189.88,\"sys_match_type_field\":0,\"sys_match_comment_field\":null},{\"sys_group_id_field\":15,\"payment_id\":14804,\"Custname\":\"麦西姆杜特商业（上海）有限公司扬州京华城路分公司\",\"LL_Name\":\"扬州京国实业有限公司\",\"Pmt_Rate\":0.06,\"DescriptionKey\":\"Mgt\",\"Paid_Date\":\"2019-02-15\",\"Amount\":14895.8,\"inv_id\":20917,\"Custname_R\":\"麦西姆杜特商业（上海）有限公司扬州京华城路分公司\",\"ClientName\":\"扬州京国实业有限公司\",\"Taxrate\":0.06,\"DescriptionKey_R\":\"Mgt\",\"Invdate\":\"2019-02-15\",\"TaxAmount\":14895.8,\"sys_match_type_field\":0,\"sys_match_comment_field\":null},{\"sys_group_id_field\":16,\"payment_id\":14857,\"Custname\":\"麦西姆杜特商业（上海）有限公司沈阳北二中路分公司\",\"LL_Name\":\"星摩尔（沈阳）商业有限公司\",\"Pmt_Rate\":0.05,\"DescriptionKey\":\"rental\",\"Paid_Date\":\"2019-02-15\",\"Amount\":44508.6,\"inv_id\":20837,\"Custname_R\":\"麦西姆杜特商业（上海）有限公司沈阳北二中路分公司\",\"ClientName\":\"星摩尔（沈阳）商业有限公司\",\"Taxrate\":0.05,\"DescriptionKey_R\":\"rental\",\"Invdate\":\"2019-02-15\",\"TaxAmount\":44508.6,\"sys_match_type_field\":0,\"sys_match_comment_field\":null},{\"sys_group_id_field\":17,\"payment_id\":14863,\"Custname\":\"麦西姆杜特商业（上海）有限公司常州通江中路分公司\",\"LL_Name\":\"常州新北万达广场投资有限公司\",\"Pmt_Rate\":0.05,\"DescriptionKey\":\"rental\",\"Paid_Date\":\"2019-02-15\",\"Amount\":57553.15,\"inv_id\":20884,\"Custname_R\":\"麦西姆杜特商业（上海）有限公司常州通江中路分公司\",\"ClientName\":\"常州新北万达广场投资有限公司\",\"Taxrate\":0.05,\"DescriptionKey_R\":\"rental\",\"Invdate\":\"2019-02-15\",\"TaxAmount\":57553.15,\"sys_match_type_field\":0,\"sys_match_comment_field\":null},{\"sys_group_id_field\":18,\"payment_id\":14874,\"Custname\":\"麦西姆杜特商业（上海）有限公司扬州京华城路分公司\",\"LL_Name\":\"扬州京国实业有限公司\",\"Pmt_Rate\":0.05,\"DescriptionKey\":\"rental\",\"Paid_Date\":\"2019-02-15\",\"Amount\":53912.16,\"inv_id\":20916,\"Custname_R\":\"麦西姆杜特商业（上海）有限公司扬州京华城路分公司\",\"ClientName\":\"扬州京国实业有限公司\",\"Taxrate\":0.05,\"DescriptionKey_R\":\"rental\",\"Invdate\":\"2019-02-15\",\"TaxAmount\":53912.16,\"sys_match_type_field\":0,\"sys_match_comment_field\":null},{\"sys_group_id_field\":19,\"payment_id\":14958,\"Custname\":\"奥依修商贸（上海）有限公司深圳华富路分公司\",\"LL_Name\":\"深圳市中航城置业发展有限公司\",\"Pmt_Rate\":0.05,\"DescriptionKey\":\"rental\",\"Paid_Date\":\"2019-02-15\",\"Amount\":1186.5,\"inv_id\":20844,\"Custname_R\":\"奥依修商贸（上海）有限公司深圳华富路分公司\",\"ClientName\":\"深圳市中航城置业发展有限公司\",\"Taxrate\":0.05,\"DescriptionKey_R\":\"rental\",\"Invdate\":\"2019-02-13\",\"TaxAmount\":1186.5,\"sys_match_type_field\":0,\"sys_match_comment_field\":null},{\"sys_group_id_field\":20,\"payment_id\":15003,\"Custname\":\"奥依修商贸（上海）有限公司常州通江中路分公司\",\"LL_Name\":\"常州新北万达广场投资有限公司\",\"Pmt_Rate\":0.05,\"DescriptionKey\":\"rental\",\"Paid_Date\":\"2019-02-15\",\"Amount\":49499.5,\"inv_id\":20883,\"Custname_R\":\"奥依修商贸（上海）有限公司常州通江中路分公司\",\"ClientName\":\"常州新北万达广场投资有限公司\",\"Taxrate\":0.05,\"DescriptionKey_R\":\"rental\",\"Invdate\":\"2019-02-15\",\"TaxAmount\":49499.5,\"sys_match_type_field\":0,\"sys_match_comment_field\":null},{\"sys_group_id_field\":21,\"payment_id\":15027,\"Custname\":\"奥依修商贸（上海）有限公司重庆时代天街分公司\",\"LL_Name\":\"重庆龙湖成恒地产开发有限公司\",\"Pmt_Rate\":0.05,\"DescriptionKey\":\"rental\",\"Paid_Date\":\"2019-02-15\",\"Amount\":32397.86,\"inv_id\":20772,\"Custname_R\":\"奥依修商贸（上海）有限公司重庆时代天街分公司\",\"ClientName\":\"重庆龙湖成恒地产开发有限公司\",\"Taxrate\":0.05,\"DescriptionKey_R\":\"rental\",\"Invdate\":\"2019-02-13\",\"TaxAmount\":32397.86,\"sys_match_type_field\":0,\"sys_match_comment_field\":null},{\"sys_group_id_field\":22,\"payment_id\":15087,\"Custname\":\"普安倍尔商业（北京）有限公司沈阳北二中路分公司\",\"LL_Name\":\"星摩尔（沈阳）商业有限公司\",\"Pmt_Rate\":0.05,\"DescriptionKey\":\"rental\",\"Paid_Date\":\"2019-02-15\",\"Amount\":35394.01,\"inv_id\":20838,\"Custname_R\":\"普安倍尔商业（北京）有限公司沈阳北二中路分公司\",\"ClientName\":\"星摩尔（沈阳）商业有限公司\",\"Taxrate\":0.05,\"DescriptionKey_R\":\"rental\",\"Invdate\":\"2019-02-15\",\"TaxAmount\":35394.01,\"sys_match_type_field\":0,\"sys_match_comment_field\":null},{\"sys_group_id_field\":23,\"payment_id\":15120,\"Custname\":\"普安倍尔商业（北京）有限公司重庆时代天街分公司\",\"LL_Name\":\"重庆龙湖成恒地产开发有限公司\",\"Pmt_Rate\":0.05,\"DescriptionKey\":\"rental\",\"Paid_Date\":\"2019-02-15\",\"Amount\":53788.98,\"inv_id\":20769,\"Custname_R\":\"普安倍尔商业（北京）有限公司重庆时代天街分公司\",\"ClientName\":\"重庆龙湖成恒地产开发有限公司\",\"Taxrate\":0.05,\"DescriptionKey_R\":\"rental\",\"Invdate\":\"2019-02-13\",\"TaxAmount\":53788.98,\"sys_match_type_field\":0,\"sys_match_comment_field\":null},{\"sys_group_id_field\":24,\"payment_id\":15123,\"Custname\":\"普安倍尔商业（北京）有限公司宁波天童南路分公司\",\"LL_Name\":\"银泰百货宁波鄞州有限公司环球城分公司\",\"Pmt_Rate\":0.1,\"DescriptionKey\":\"rental\",\"Paid_Date\":\"2019-02-15\",\"Amount\":34336.95,\"inv_id\":20810,\"Custname_R\":\"普安倍尔商业（北京）有限公司宁波天童南路分公司\",\"ClientName\":\"银泰百货宁波鄞州有限公司环球城分公司\",\"Taxrate\":0.1,\"DescriptionKey_R\":\"rental\",\"Invdate\":\"2019-02-15\",\"TaxAmount\":34336.95,\"sys_match_type_field\":0,\"sys_match_comment_field\":null},{\"sys_group_id_field\":25,\"payment_id\":15202,\"Custname\":\"斯特拉迪瓦里斯商业（上海）有限公司深圳滨河大道分公司\",\"LL_Name\":\"深圳市京基百纳商业管理有限公司京基百纳时代店\",\"Pmt_Rate\":0.05,\"DescriptionKey\":\"rental\",\"Paid_Date\":\"2019-02-15\",\"Amount\":21071.27,\"inv_id\":20885,\"Custname_R\":\"斯特拉迪瓦里斯商业（上海）有限公司深圳滨河大道分公司\",\"ClientName\":\"深圳市京基百纳商业管理有限公司京基百纳时代店\",\"Taxrate\":0.05,\"DescriptionKey_R\":\"rental\",\"Invdate\":\"2019-02-12\",\"TaxAmount\":21071.27,\"sys_match_type_field\":0,\"sys_match_comment_field\":null},{\"sys_group_id_field\":26,\"payment_id\":15373,\"Custname\":\"飒拉商业（上海）有限公司扬州京华城路分公司\",\"LL_Name\":\"扬州京国实业有限公司\",\"Pmt_Rate\":0.06,\"DescriptionKey\":\"Mgt\",\"Paid_Date\":\"2019-02-15\",\"Amount\":76538.75,\"inv_id\":20919,\"Custname_R\":\"飒拉商业（上海）有限公司扬州京华城路分公司\",\"ClientName\":\"扬州京国实业有限公司\",\"Taxrate\":0.06,\"DescriptionKey_R\":\"Mgt\",\"Invdate\":\"2019-02-15\",\"TaxAmount\":76538.75,\"sys_match_type_field\":0,\"sys_match_comment_field\":null},{\"sys_group_id_field\":27,\"payment_id\":15467,\"Custname\":\"飒拉商业（上海）有限公司扬州京华城路分公司\",\"LL_Name\":\"扬州京国实业有限公司\",\"Pmt_Rate\":0.05,\"DescriptionKey\":\"rental\",\"Paid_Date\":\"2019-02-15\",\"Amount\":145446.67,\"inv_id\":20918,\"Custname_R\":\"飒拉商业（上海）有限公司扬州京华城路分公司\",\"ClientName\":\"扬州京国实业有限公司\",\"Taxrate\":0.05,\"DescriptionKey_R\":\"rental\",\"Invdate\":\"2019-02-15\",\"TaxAmount\":145446.67,\"sys_match_type_field\":0,\"sys_match_comment_field\":null},{\"sys_group_id_field\":28,\"payment_id\":15479,\"Custname\":\"飒拉商业（上海）有限公司宁波天童南路分公司\",\"LL_Name\":\"银泰百货宁波鄞州有限公司环球城分公司\",\"Pmt_Rate\":0.1,\"DescriptionKey\":\"rental\",\"Paid_Date\":\"2019-02-15\",\"Amount\":116810.59,\"inv_id\":20809,\"Custname_R\":\"飒拉商业（上海）有限公司宁波天童南路分公司\",\"ClientName\":\"银泰百货宁波鄞州有限公司环球城分公司\",\"Taxrate\":0.1,\"DescriptionKey_R\":\"rental\",\"Invdate\":\"2019-02-15\",\"TaxAmount\":116810.59,\"sys_match_type_field\":0,\"sys_match_comment_field\":null},{\"sys_group_id_field\":29,\"payment_id\":15484,\"Custname\":\"飒拉商业（上海）有限公司重庆时代天街分公司\",\"LL_Name\":\"重庆龙湖成恒地产开发有限公司\",\"Pmt_Rate\":0.05,\"DescriptionKey\":\"rental\",\"Paid_Date\":\"2019-02-15\",\"Amount\":196900.68,\"inv_id\":20770,\"Custname_R\":\"飒拉商业（上海）有限公司重庆时代天街分公司\",\"ClientName\":\"重庆龙湖成恒地产开发有限公司\",\"Taxrate\":0.05,\"DescriptionKey_R\":\"rental\",\"Invdate\":\"2019-02-13\",\"TaxAmount\":196900.68,\"sys_match_type_field\":0,\"sys_match_comment_field\":null},{\"sys_group_id_field\":30,\"payment_id\":15524,\"Custname\":\"飒拉商业（上海）有限公司常州通江中路分公司\",\"LL_Name\":\"常州新北万达广场投资有限公司\",\"Pmt_Rate\":0.05,\"DescriptionKey\":\"rental\",\"Paid_Date\":\"2019-02-15\",\"Amount\":167292.39,\"inv_id\":20882,\"Custname_R\":\"飒拉商业（上海）有限公司常州通江中路分公司\",\"ClientName\":\"常州新北万达广场投资有限公司\",\"Taxrate\":0.05,\"DescriptionKey_R\":\"rental\",\"Invdate\":\"2019-02-15\",\"TaxAmount\":167292.39,\"sys_match_type_field\":0,\"sys_match_comment_field\":null},{\"sys_group_id_field\":31,\"payment_id\":15543,\"Custname\":\"飒拉商业（上海）有限公司长沙枫林三路分公司\",\"LL_Name\":\"步步高商业连锁股份有限公司梅溪湖分公司\",\"Pmt_Rate\":0.05,\"DescriptionKey\":\"rental\",\"Paid_Date\":\"2019-02-15\",\"Amount\":97037.67,\"inv_id\":20786,\"Custname_R\":\"飒拉商业（上海）有限公司长沙枫林三路分公司\",\"ClientName\":\"步步高商业连锁股份有限公司梅溪湖分公司\",\"Taxrate\":0.05,\"DescriptionKey_R\":\"rental\",\"Invdate\":\"2019-02-16\",\"TaxAmount\":97037.67,\"sys_match_type_field\":0,\"sys_match_comment_field\":null},{\"sys_group_id_field\":32,\"payment_id\":15594,\"Custname\":\"波丝可商业（北京）有限公司深圳滨河大道分公司\",\"LL_Name\":\"深圳市京基百纳商业管理有限公司京基百纳时代店\",\"Pmt_Rate\":0.16,\"DescriptionKey\":\"elec\",\"Paid_Date\":\"2019-02-15\",\"Amount\":11257.5,\"inv_id\":20889,\"Custname_R\":\"波丝可商业（北京）有限公司深圳滨河大道分公司\",\"ClientName\":\"深圳市京基百纳商业管理有限公司京基百纳时代店\",\"Taxrate\":0.16,\"DescriptionKey_R\":\"elec\",\"Invdate\":\"2019-02-12\",\"TaxAmount\":11257.5,\"sys_match_type_field\":0,\"sys_match_comment_field\":null},{\"sys_group_id_field\":33,\"payment_id\":15679,\"Custname\":\"奥依修商贸（上海）有限公司重庆时代天街分公司\",\"LL_Name\":\"重庆龙湖成恒地产开发有限公司\",\"Pmt_Rate\":0.16,\"DescriptionKey\":\"elec\",\"Paid_Date\":\"2019-02-15\",\"Amount\":81.8,\"inv_id\":20777,\"Custname_R\":\"奥依修商贸（上海）有限公司重庆时代天街分公司\",\"ClientName\":\"重庆龙湖成恒地产开发有限公司\",\"Taxrate\":0.16,\"DescriptionKey_R\":\"elec\",\"Invdate\":\"2019-02-13\",\"TaxAmount\":81.8,\"sys_match_type_field\":0,\"sys_match_comment_field\":null},{\"sys_group_id_field\":34,\"payment_id\":15723,\"Custname\":\"普安倍尔商业（北京）有限公司重庆时代天街分公司\",\"LL_Name\":\"重庆龙湖成恒地产开发有限公司\",\"Pmt_Rate\":0.16,\"DescriptionKey\":\"elec\",\"Paid_Date\":\"2019-02-15\",\"Amount\":9923.85,\"inv_id\":20773,\"Custname_R\":\"普安倍尔商业（北京）有限公司重庆时代天街分公司\",\"ClientName\":\"重庆龙湖成恒地产开发有限公司\",\"Taxrate\":0.16,\"DescriptionKey_R\":\"elec\",\"Invdate\":\"2019-02-13\",\"TaxAmount\":9923.85,\"sys_match_type_field\":0,\"sys_match_comment_field\":null},{\"sys_group_id_field\":35,\"payment_id\":15726,\"Custname\":\"普安倍尔商业（北京）有限公司宁波天童南路分公司\",\"LL_Name\":\"银泰百货宁波鄞州有限公司环球城分公司\",\"Pmt_Rate\":0.16,\"DescriptionKey\":\"elec\",\"Paid_Date\":\"2019-02-15\",\"Amount\":16984.91,\"inv_id\":20866,\"Custname_R\":\"普安倍尔商业（北京）有限公司宁波天童南路分公司\",\"ClientName\":\"银泰百货宁波鄞州有限公司环球城分公司\",\"Taxrate\":0.16,\"DescriptionKey_R\":\"elec\",\"Invdate\":\"2019-02-02\",\"TaxAmount\":16984.91,\"sys_match_type_field\":0,\"sys_match_comment_field\":null},{\"sys_group_id_field\":36,\"payment_id\":15847,\"Custname\":\"飒拉商业（上海）有限公司宁波天童南路分公司\",\"LL_Name\":\"银泰百货宁波鄞州有限公司环球城分公司\",\"Pmt_Rate\":0.16,\"DescriptionKey\":\"elec\",\"Paid_Date\":\"2019-02-15\",\"Amount\":52019.76,\"inv_id\":20867,\"Custname_R\":\"飒拉商业（上海）有限公司宁波天童南路分公司\",\"ClientName\":\"银泰百货宁波鄞州有限公司环球城分公司\",\"Taxrate\":0.16,\"DescriptionKey_R\":\"elec\",\"Invdate\":\"2019-02-02\",\"TaxAmount\":52019.76,\"sys_match_type_field\":0,\"sys_match_comment_field\":null}]";

        List<LinkedHashMap> hashMaps = JsonUtil.fromListJson(dataJson, LinkedHashMap.class);
        System.out.println("hashMaps:" + hashMaps.size());
        for (LinkedHashMap hashMap : hashMaps) {
            for (Object o : hashMap.entrySet()) {
                System.out.println(o);
            }
        }
    }

    @Test
    public void testHttpListUser() {
        String url = "http://119.37.194.4:5555/xtp-api/user/listUserByRoleId?roleId=12&oauth=whosyourdaddy";
        HttpUtils.ResultResponse resultResponse = HttpUtils.createRequest().doGet(url);
        String data = resultResponse.getData();
        //System.out.println("value:" + data);
        AjaxResult ajaxResult = JsonUtil.fromJson(data, AjaxResult.class);
        Pagination<UserInfo> pagination = JsonUtil.fromGenericJson(JsonUtil.toJson(ajaxResult.getData()), Pagination.class, UserInfo.class);
        List<UserInfo> userInfoList = pagination.getData();
        System.out.println("userInfoList:" + userInfoList);
    }

    @Test
    public void testHttp() {
        String url = "http://www.wubo777.top/web-socket/dan/testJson";
        //String url = "https://mpapi.xtits.cn/ihaj-api/housing/listHousing";
        //String url = "https://api.weixin.qq.com/sns/jscode2session";
        String data = "{\"grant_type\":\"authorization_code\",\"appid\":\"wxc6f17db215ae62a0\",\"secret\":\"25dd7249f0675bfbf18ee06ff797c087\",\"js_code\":\"061TqdSc1rzeKx0eJYVc1j2YRc1TqdSI\"}";
        Map<String, Object> dataMap = JsonUtil.fromJson(data, LinkedHashMap.class);
        String header = "{\"content-type\":\"application/json\"}";
        Map<String, String> headerMap = JsonUtil.fromJson(header, LinkedHashMap.class);

        HttpUtils.ResultResponse resultResponse = HttpUtils.createRequest().doGet(url, dataMap, headerMap);
        System.out.println("value:" + resultResponse.toString());
    }

    @Test
    public void testListToInteger() {
        //list实体类,取出xx属性,转成一个List集合
        List<UserInfo> userInfoList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            UserInfo userInfo = new UserInfo();
            userInfo.setId(i);
            userInfo.setAccount("account-" + i);
            userInfo.setPassword("password" + i);
            userInfo.setName("名称名称名称名称名称名称名称" + i);
            userInfoList.add(userInfo);
        }
        for (int i = 0; i < 10; i++) {
            UserInfo userInfo = new UserInfo();
            userInfo.setId(i);
            userInfo.setAccount("account-" + i);
            userInfo.setPassword("password" + i);
            userInfo.setName("名称名称名称名称名称名称名称" + i);
            userInfoList.add(userInfo);
        }
        UserInfo userInfo = new UserInfo();
        userInfo.setId(null);
        userInfoList.add(userInfo);
        //取出所有id,并且不为null,去除重复【distinct】
        List<Integer> userIdList = userInfoList.stream().map(UserInfo::getId).filter(Objects::nonNull).distinct().collect(Collectors.toList());

        System.out.println("userIdList:" + userIdList);
    }

    @Test
    public void testJsons() {
        String str1 = "{\"name\":\"张三\",\"id\":\"aaaQQQ123\"}";
        String str2 = "{\"name\":\"是好人\",\"id\":\"hahaha\",\"age\":20}";
        JSONObject jsonObject1 = JSONObject.parseObject(str1);
        JSONObject jsonObject2 = JSONObject.parseObject(str2);
        for (Map.Entry<String, Object> jsonEntry : jsonObject2.entrySet()) {
            //如果第一个json字符串包含第二个json字符串,就把第二个json追加到第一个
            if (jsonObject1.containsKey(jsonEntry.getKey())) {
                String value = jsonObject1.getString(jsonEntry.getKey());
                String value2 = jsonEntry.getValue() == null ? "" : String.valueOf(jsonEntry.getValue());
                jsonObject1.put(jsonEntry.getKey(), value + value2);
            } else {
                jsonObject1.put(jsonEntry.getKey(), jsonEntry.getValue());
            }
        }
        System.out.println("jsonObject1:" + jsonObject1.toString());

    }

    @Test
    public void testObjToMap() {
        UserInfo userInfo = new UserInfo();
        userInfo.setName("测试名称");
        userInfo.setTestLong(10L);
        userInfo.setPassword("密码");
        userInfo.setQq("123456789");
        List<UserInfo> objects = new ArrayList<>();
        objects.add(new UserInfo("账号1", "密码1"));
        objects.add(new UserInfo("账号2", "密码2"));
        userInfo.setUserInfoList(objects);
        Map<String, Object> objectMap = MapperUtil.beanToMap(userInfo);
        System.out.println("objectMap:" + JsonUtil.toJson(objectMap));
        UserInfo userInfo1 = MapperUtil.mapToBean(objectMap, UserInfo.class);
        System.out.println("userInfo1:" + userInfo1.toString());
    }

    @Test
    public void importExcel() throws IOException {
        //导入
        ExcelWriteUtils excelWriteUtils = ExcelWriteUtils.importExcel("A:/ax1201-31.xlsx", 1, 1);
        //一行数据为null，或者""，不添加
        excelWriteUtils.setEmptyRowFlag(false);
        //显示读取行号
        excelWriteUtils.setDebugNumFlag(true);
        //设置排除行:一.下标以逗号分隔，二：传int集合
        //excelWriteUtils.setExcludeLineList("1,2");
        //excelWriteUtils.setExcludeLineList(Arrays.asList(1, 2));
        List<List<Map<String, String>>> listList = excelWriteUtils.readExcelWithoutTitle(true, 1, false);
        for (List<Map<String, String>> listMap : listList) {
            for (Map<String, String> map : listMap) {

                for (Map.Entry<String, String> entry : map.entrySet()) {
                    System.out.print(entry.getKey() + "=========" + entry.getValue() + ",");
                }
                System.out.println("\n++++++++++++++++++++++++++++++++++++++++++++++++++\n");
            }
        }
    }

    @Test
    public void test02() throws IOException {
        //导出
        List<UserInfo> userInfoList = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            UserInfo userInfo = new UserInfo();
            userInfo.setAccount("account-" + i);
            userInfo.setPassword("password" + i);
            userInfo.setName("名称名称名称名称名称名称名称" + i);
            userInfo.setMail("邮箱邮箱邮箱邮箱邮箱邮箱邮箱邮箱邮箱邮箱邮箱邮箱邮箱邮箱邮箱sssss得得得得得得得得得得得得得得得得得得得得得得得得" + i);
            userInfo.setPhone("电话:123655" + i);
            userInfo.setQq("qq=" + i);
            userInfo.setBirthDay(new Date());
            userInfo.setTestBigDecimal(BigDecimal.valueOf(77777.77707));
            userInfo.setTestDouble(66666.66);
            userInfo.setTestFloat(55.55505F);
            userInfo.setTestLong(1000L);
            userInfoList.add(userInfo);
        }
        ExcelWriteUtils excelWriteUtils = ExcelWriteUtils.createWriteExcel();
        //Double/Float 保留两位格式化:true 格式化,false:不格式(默认true),注：为true时DecimalStyle不会生效
        excelWriteUtils.setDecimalFormatFlag(false);

        //格式化值为指定类型显示,值类型不改变(默认：false),  注：为true时生效,但DecimalFormatFlag必须为false
        excelWriteUtils.setDecimalStyleFlag(true);
        //设置double类型格式化为金额格式(默认：格式化成两位小数)
        excelWriteUtils.setDoubleDecimal("#,##0.00");
        excelWriteUtils.setBigDecimalDecimal("0");
        //标题颜色
        //excelWriteUtils.setTitleBackColor(null);
        //时间格式
        excelWriteUtils.setExportDataFormat(DateUtil.FORMAT_LONG);
        //设置excel导出标题
        //excelWriteUtils.setDefaultWriteTitle("哈哈哈哈哈哈");
        //设置内容样式-默认false
        excelWriteUtils.setDefaultColumnStyleFlag(true);

        Integer[] titleSize = {50, 20, 10, 50, 50, 50, 50, 50, 50, 50};

        //输出到本地文件url
        String excelFileUrl = FileUtil.createMkdirMulti("P:/", true, "file/excel/");
        excelFileUrl = excelFileUrl + DateTime.now().toString("yyyy-MM-dd_") + UUID.randomUUID() + ".xls";

        //1.导出本地-头部使用数组方式
        String[] head = {"account", "password", "name", "mail", "qq", "phone", "testBigDecimal", "testDouble", "testFloat", "testLong"};
        ByteArrayOutputStream outputStream = excelWriteUtils.writeExcel("sheet1", head, userInfoList, null);
        outputStream.writeTo(new FileOutputStream(new File(excelFileUrl)));

        Map<String, String> linkedHashMap = new LinkedHashMap<>();
        linkedHashMap.put("account", "账号");
        linkedHashMap.put("password", "密码");
        linkedHashMap.put("name", "昵称");
        linkedHashMap.put("mail", "邮箱");
        linkedHashMap.put("qq", "QQ");
        linkedHashMap.put("phone", "电话");
        linkedHashMap.put("testBigDecimal", "BigDecimal");
        linkedHashMap.put("testDouble", "Double");
        linkedHashMap.put("testFloat", "Float");
        linkedHashMap.put("testLong", "Long");
        //excelWriteUtils.writeExcelFileUrl("sheet1", linkedHashMap, userInfoList, excelFileUrl);

        //3.web 导出
        //设置响应头和客户端保存文件名
        // Spring Mvc
        /*excelWriteUtils.wirteExcel("sheet1", linkedHashMap, userInfoList, outputStream, titleSize);
        HttpServletResponse response =null;
         response.setHeader("Content-Disposition", "attachment; filename=" + DateTime.now().toString("yyyy-MM-dd_") + UUID.randomUUID() + ".xls");
        response.addHeader("Content-Length", "" + outputStream.toByteArray().length);
        response.setContentType("application/octet-stream; charset=UTF-8");
        IOUtils.write(outputStream.toByteArray(), response.getOutputStream());*/

    }

    /**
     * 写入多个sheet
     */
    @Test
    public void testWriteSheets() {

        ExcelWriteUtils excelWriteUtils = ExcelWriteUtils.createWriteExcel();
        //设置内容样式-默认false
        excelWriteUtils.setDefaultColumnStyleFlag(true);
        //标题颜色
        excelWriteUtils.setTitleBackColor(null);
        List<UserInfo> userInfoList = null;
        for (int i = 0; i < 3; i++) {
            ExcelWriteUtils.WriteCriteria criteria = excelWriteUtils.createWriteCriteria();
            criteria.setTitleWrite("哈哈" + i);
            userInfoList = new ArrayList<>();
            for (int j = 0; j < 50; j++) {
                UserInfo userInfo = new UserInfo();
                userInfo.setAccount("account-" + i + ":" + j);
                userInfo.setPassword("password" + i + ":" + j);
                userInfo.setName("名称名称名称名称名称名称名称" + i + ":" + j);
                userInfo.setMail("邮箱邮箱邮箱邮箱邮箱邮箱邮箱邮箱邮箱邮箱邮箱邮箱邮箱邮箱邮箱sssss得得得得得得得得得得得得得得得得得得得得得得得得" + i + ":" + j);
                userInfo.setPhone("电话:123655" + i + ":" + j);
                userInfo.setQq("qq=" + i + ":" + j);
                userInfo.setBirthDay(new Date());
                userInfo.setTestBigDecimal(BigDecimal.valueOf(777.77707));
                userInfo.setTestDouble(6666.66);
                userInfo.setTestFloat(55.55505F);
                userInfo.setTestLong(1000L);
                userInfoList.add(userInfo);
            }
            criteria.setDataList(userInfoList);
            criteria.setSheetName("页" + i);
            excelWriteUtils.addCriteria(criteria);
        }
        Map<String, String> linkedHashMap = new LinkedHashMap<>();
        linkedHashMap.put("account", "账号");
        linkedHashMap.put("password", "密码");
        linkedHashMap.put("name", "昵称");
        linkedHashMap.put("mail", "邮箱");
        linkedHashMap.put("qq", "QQ");
        linkedHashMap.put("phone", "电话");
        linkedHashMap.put("testBigDecimal", "BigDecimal");
        linkedHashMap.put("testDouble", "Double");
        linkedHashMap.put("testFloat", "Float");
        linkedHashMap.put("testLong", "Long");
        //设置导出表头
        excelWriteUtils.setDefaultWriteTitleColumnAndNameMap(linkedHashMap);
        //输出到本地文件url
        String excelFileUrl = FileUtil.createMkdirMulti("P:/", true, "file/excel/");
        excelFileUrl = excelFileUrl + DateTime.now().toString("yyyy-MM-dd_") + UUID.randomUUID() + ".xls";
        excelWriteUtils.writeExcel(excelWriteUtils.getAddWriteCriteriaList(), new File(excelFileUrl));
        /*String toJson = JsonUtil.toJson(writeUtilsTest);
        System.out.println("toJson:" + toJson);*/
        System.out.println("success");

    }

    /**
     * 写入多个sheet,不同的表头
     */
    @Test
    public void testWriteSheetsTitle() {
        Map<String, String> linkedHashMap1 = new LinkedHashMap<>();
        linkedHashMap1.put("account", "账号");
        linkedHashMap1.put("password", "密码");
        linkedHashMap1.put("name", "昵称");
        linkedHashMap1.put("mail", "邮箱");
        linkedHashMap1.put("qq", "QQ");
        linkedHashMap1.put("phone", "电话");
        linkedHashMap1.put("testBigDecimal", "BigDecimal");
        linkedHashMap1.put("testDouble", "Double");
        linkedHashMap1.put("testFloat", "Float");
        linkedHashMap1.put("testLong", "Long");
        Map<String, String> linkedHashMap2 = new LinkedHashMap<>();
        linkedHashMap2.put("account", "账号");
        linkedHashMap2.put("password", "密码");
        linkedHashMap2.put("name", "昵称");
        linkedHashMap2.put("mail", "邮箱");
        linkedHashMap2.put("qq", "QQ");
        Map<String, String> linkedHashMap3 = new LinkedHashMap<>();
        linkedHashMap3.put("account", "账号");
        linkedHashMap3.put("phone", "电话");
        linkedHashMap3.put("testBigDecimal", "BigDecimal");
        linkedHashMap3.put("testDouble", "Double");
        linkedHashMap3.put("testFloat", "Float");
        linkedHashMap3.put("testLong", "Long");
        ExcelWriteUtils excelWriteUtils = ExcelWriteUtils.createWriteExcel();
        //设置内容样式-默认false
        excelWriteUtils.setDefaultColumnStyleFlag(true);
        //标题颜色
        excelWriteUtils.setTitleBackColor(null);
        //设置全部sheet都没有表头
        excelWriteUtils.setDefaultTitleHeaderFlag(false);
        //Double/Float 保留两位格式化:true 格式化,false:不格式(默认true),注：为true时DecimalStyle不会生效
        excelWriteUtils.setDecimalFormatFlag(false);

        //格式化值为指定类型显示,值类型不改变(默认：false),  注：为true时生效,但DecimalFormatFlag必须为false
        excelWriteUtils.setDecimalStyleFlag(true);
        //设置double类型格式化为金额格式(默认：格式化成两位小数)
        excelWriteUtils.setDoubleDecimal("#,##0.00");
        excelWriteUtils.setBigDecimalDecimal("#,##0.00");
        List<UserInfo> userInfoList = null;
        for (int i = 0; i < 3; i++) {
            ExcelWriteUtils.WriteCriteria criteria = excelWriteUtils.createWriteCriteria();
            userInfoList = new ArrayList<>();
            for (int j = 0; j < 50; j++) {
                UserInfo userInfo = new UserInfo();
                userInfo.setAccount("account-" + i + ":" + j);
                userInfo.setPassword("password" + i + ":" + j);
                userInfo.setName("名称名称名称名称名称名称名称" + i + ":" + j);
                userInfo.setMail("邮箱邮箱邮箱邮箱邮箱邮箱邮箱邮箱邮箱邮箱邮箱邮箱邮箱邮箱邮箱sssss得得得得得得得得得得得得得得得得得得得得得得得得" + i + ":" + j);
                userInfo.setPhone("电话:123655" + i + ":" + j);
                userInfo.setQq("qq=" + i + ":" + j);
                userInfo.setBirthDay(new Date());
                userInfo.setTestBigDecimal(BigDecimal.valueOf(777777.77707));
                userInfo.setTestDouble(6666666.66);
                userInfo.setTestFloat(55.55505F);
                userInfo.setTestLong(1000L);
                userInfoList.add(userInfo);
            }
            //每页的表头
            if (i == 0) {
                criteria.setTitleWrite("第一页表头");
                criteria.setWriteTitleColumnAndNameMap(linkedHashMap1);
                //设置当前有表头
                criteria.setTitleHeaderFlag(true);
            } else if (i == 1) {
                criteria.setTitleWrite("第二页表头");
                criteria.setWriteTitleColumnAndNameMap(linkedHashMap2);
            } else {
                criteria.setWriteTitleColumnAndNameMap(linkedHashMap3);
                //自定义每页的宽度
                criteria.setTitleSize(new Integer[]{50, 20, 30});
                //取消表头
                criteria.setTitleHeaderFlag(false);
            }
            //列名,当每页自定义表头时，表头名默认:TitleColumn,都定义，优先使用WriteTitleColumnAndNameMap
            //criteria.setTitleColumn(new String[]{"name", "age"});
            //定义页的数据
            criteria.setDataList(userInfoList);
            //页名称
            criteria.setSheetName("页" + i);
            excelWriteUtils.addCriteria(criteria);
        }

        //设置导出表头
        //excelWriteUtils.setDefaultWriteTitleColumnAndNameMap(linkedHashMap1);
        //输出到本地文件url
        String excelFileUrl = FileUtil.createMkdirMulti("P:/", true, "file/excel/");
        excelFileUrl = excelFileUrl + DateTime.now().toString("yyyy-MM-dd_") + UUID.randomUUID() + ".xls";
        excelWriteUtils.writeExcel(excelWriteUtils.getAddWriteCriteriaList(), new File(excelFileUrl));
        System.out.println("success");


    }

    /**
     * 导出多个标题
     */
    @Test
    public void testWriteSheetsTitles() throws IOException {
        ExcelWriteUtils excelWriteUtils = ExcelWriteUtils.createWriteExcel();
        //设置内容样式-默认false
        excelWriteUtils.setDefaultColumnStyleFlag(true);
        //标题颜色
        excelWriteUtils.setTitleBackColor(null);
        excelWriteUtils.setDefaultWriteTitle("标题");
        excelWriteUtils.setDefaultWriteTitleList(Arrays.asList("标题1", "标题2", "标题3"));
        List<Map<String, Object>> headerList = new ArrayList<>();
        Map<String, Object> map1 = new LinkedHashMap<>();
        map1.put("供应商名称：", "宁波绚彩照明有限公司");
        Map<String, Object> map2 = new LinkedHashMap<>();
        map2.put("日期段：", "2018-11-01~2019-03-15");
        map2.put("制表日期：", "2019-03-15");
        headerList.add(map1);
        headerList.add(map2);
        excelWriteUtils.setDefaultCustomHeaderList(headerList);
        List<UserInfo> userInfoList = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            UserInfo userInfo = new UserInfo();
            userInfo.setAccount("account-" + i + ":" + i);
            userInfo.setPassword("password" + i + ":" + i);
            userInfo.setName("名称名称名称名称名称名称名称" + i + ":" + i);
            userInfo.setMail("邮箱邮箱邮箱邮箱邮箱邮箱邮箱邮箱邮箱邮箱邮箱邮箱邮箱邮箱邮箱sssss得得得得得得得得得得得得得得得得得得得得得得得得" + i + ":" + i);
            userInfo.setPhone("电话:123655" + i + ":" + i);
            userInfo.setQq("qq=" + i + ":" + i);
            userInfo.setBirthDay(new Date());
            userInfo.setTestBigDecimal(BigDecimal.valueOf(777.77707));
            userInfo.setTestDouble(6666.66);
            userInfo.setTestFloat(55.55505F);
            userInfo.setTestLong(1000L);
            userInfoList.add(userInfo);
        }
        //输出到本地文件url
        String excelFileUrl = FileUtil.createMkdirMulti("P:/", true, "file/excel/");
        excelFileUrl = excelFileUrl + DateTime.now().toString("yyyy-MM-dd_") + UUID.randomUUID() + ".xls";

        Map<String, String> linkedHashMap = new LinkedHashMap<>();
        linkedHashMap.put("account", "账号");
        linkedHashMap.put("password", "密码");
        linkedHashMap.put("name", "昵称");
        linkedHashMap.put("mail", "邮箱");
        linkedHashMap.put("qq", "QQ");
        linkedHashMap.put("phone", "电话");
        linkedHashMap.put("testBigDecimal", "BigDecimal");
        linkedHashMap.put("testDouble", "Double");
        linkedHashMap.put("testFloat", "Float");
        linkedHashMap.put("testLong", "Long");
        excelWriteUtils.writeExcelFileUrl("sheet1", linkedHashMap, userInfoList, excelFileUrl);
        System.out.println("success");
    }

    @Test
    public void testDecimalFormat() {
        double num = 0.3141598653512314;
        double num1 = 0.314159445354;
        double num2 = 0.653;
        double num3 = 0.425;
        DecimalFormat format = new DecimalFormat("#.###");
        //最多保留几位
        format.setMaximumFractionDigits(10);
        //最少保留几位, 可以是0 就是 取整数
        format.setMinimumFractionDigits(0);
        System.out.println("format:" + format.format(num));
        System.out.println("format:" + format.format(num1));
        System.out.println("format:" + format.format(num2));
        System.out.println("format:" + format.format(num3));
    }

    @Test
    public void testReadFileName() {
        String PATH = "P:/file";
        int autoClearDay = 20;
        List<String> stringList = FileUtil.readFileAndPath(PATH, true);
        //List<String> stringList = FileUtil.readFileAndPathReg("P:/file", FileUtil.YEAR_REXP);
        for (String s : stringList) {
            System.out.println("s->" + s);
            clearLog(s, autoClearDay);
        }
    }

    @Test
    public void testList() {
        List<UserInfo> listA = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            listA.add(new UserInfo("account:" + i, "password:" + i));
        }
        List<UserInfo> listB = new ArrayList<>();
        for (int i = 4; i <= 10; i++) {
            listB.add(new UserInfo("account:" + i, "password:" + i));
        }
        for (int i = 4; i <= 10; i++) {
            listB.add(new UserInfo("account:" + i, "password:" + i));
        }
        for (UserInfo material : listA) {
            List<UserInfo> thisList = listB.stream().filter(d -> d.getAccount().equals(material.getAccount())).collect(Collectors.toList());
            //如果有数据
            System.out.println("thisList:" + JsonUtil.toJson(thisList));
        }
    }

    /**
     * 文件删除
     *
     * @param autoClearDay 文件保存天数
     */
    public void clearLog(String PATH, int autoClearDay) {
        File dirFile = new File(PATH);
        if (dirFile.exists()) {
            autoClearDay = autoClearDay < 0 ? 0 : autoClearDay;
            long thisTime = DateUtil.getTime();
            List<String> fileUrlList = FileUtil.readFileAndPath(PATH, true, null);
            if (fileUrlList != null && fileUrlList.size() > 0) {
                File thisFile = null;
                for (String fileUrl : fileUrlList) {
                    thisFile = new File(fileUrl);
                    long lastModified = thisFile.lastModified();
                    //相差天数
                    long day = (thisTime - lastModified) / DateUtil.DAY_MILLI;
                    if (day > autoClearDay) {
                        System.out.println("remove:" + thisFile.getAbsolutePath() + ",day:" + day);
                        //FileUtil.removeFile(thisFile);
                    }
                }
            }
        }
    }

    /**
     * 给定字符串J 代表石头中宝石的类型，和字符串 S代表你拥有的石头。 S 中每个字符代表了一种你拥有的石头的类型，你想知道你拥有的石头中有多少是宝石。
     * J 中的字母不重复，J 和 S中的所有字符都是字母。字母区分大小写，因此"a"和"A"是不同类型的石头。
     */
    @Test
    public void testbSsT() {
        String j = "aA", s = "aAAbbbb";
        String[] split = j.split("");
        Map<String, Integer> map = new HashMap<>();
        for (String s1 : split) {
            map.put(s1, 0);
        }
        String[] split1 = s.split("");
        int size = 0;
        for (String s1 : split1) {
            if (map.containsKey(s1)) {
                //Integer integer = map.get(s1) + 1;
                //map.put(s1, integer);
                size++;
            }
        }
        for (Map.Entry<String, Integer> stringIntegerEntry : map.entrySet()) {

            System.out.println("key:" + stringIntegerEntry.getKey() + ",value:" + stringIntegerEntry.getValue());
        }
        System.out.println("map:size:" + size);
        size = 0;
        Set<Character> charSet = new HashSet<>();
        for (int i = 0, let = j.length(); i < let; i++) {
            charSet.add(j.charAt(i));
        }
        for (int i = 0, let = s.length(); i < let; i++) {
            if (charSet.contains(s.charAt(i))) {
                size++;
            }
        }
        System.out.println("set:size:" + size);
    }
}
