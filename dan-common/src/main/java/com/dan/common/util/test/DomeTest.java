package com.dan.common.util.test;

import cn.hutool.core.date.DateTime;
import com.alibaba.fastjson.JSONObject;
import com.dan.common.util.excel.ExcelWriteUtils;
import com.dan.utils.DateUtil;
import com.dan.utils.JsonUtil;
import com.dan.utils.file.FileUtil;
import com.dan.utils.xt.MapperUtil;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

/**
 * @fileName: Test
 * @author: Dan
 * @createDate: 2018-12-20 10:13.
 * @description:
 */
public class DomeTest {

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
        ExcelWriteUtils excelWriteUtils = ExcelWriteUtils.importExcel("A:/模具处理后1.xlsx", "Sheet1");
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
            userInfo.setTestBigDecimal(BigDecimal.valueOf(777.77707));
            userInfo.setTestDouble(6666.66);
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

        Integer[] titleSize = {50, 50, 50, 50, 50, 50};

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
    public void testWriteSheets() throws IOException {

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
}
