package top.dearbo.frame.common.test;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import top.dearbo.frame.common.util.excel.ExcelWriteUtils;
import top.dearbo.util.data.JsonUtil;
import top.dearbo.util.lang.StringUtil;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author wb
 * @date 2021-07-29 15:05.
 */
public class ExcelDomeTest {

	@Test
	public void test04() throws IOException {
		ExcelWriteUtils excelWriteUtils = ExcelWriteUtils.importExcel("D:\\data\\Tencent\\WeChat\\WeChat Files\\wxid_lwx1lw20tevc22\\FileStorage\\MsgAttach\\00334dfcb81a9623012ec503a6c02baf\\File\\2022-07\\格式化百千万表格.xls", "街镇园区");
		//显示读取行号
		excelWriteUtils.setDebugNumFlag(false);
		excelWriteUtils.setImportDataFormat("yyyy-MM-dd");
		List<List<Map<String, String>>> listList = excelWriteUtils.readExcelWithoutTitle(true, 1, false);
		StringBuffer stringBuffer = new StringBuffer();
		Map<String, List<Map<String, String>>> dataMap = new LinkedHashMap<>();
		for (List<Map<String, String>> listMap : listList) {
			for (Map<String, String> map : listMap) {
				String a = map.get("楼宇名称");
				List<Map<String, String>> mapList = dataMap.get(a);
				if (mapList != null) {
					mapList.add(map);
				} else {
					mapList = new ArrayList<>();
					mapList.add(map);
					dataMap.put(a, mapList);
				}
				/*String b = map.get("楼宇服务专员姓名及联系方式");
				String c = map.get("备注");*/

				//楼宇名称	楼宇服务专员姓名及联系方式	备注
				/*stringBuffer.append("INSERT INTO `article_content` (`parent_id`, `name`, `is_deleted`, `status`, `address`, `content`) VALUES (0, '" + map.get("主办行营业网点名称") + "', 0, 40," +
						" '" + map.get("主办行营业网点地址") + "', '[{\"name\":\"" + map.get("主办行银行营业网点联系人") + "\",\"phone\":[\"" + map.get("主办行营业网点联系方式") + "\"]}]');\n");*/

			}
		}
		for (Map.Entry<String, List<Map<String, String>>> entry : dataMap.entrySet()) {
			/*System.out.print(entry.getKey() + "=========" + entry.getValue() + ",");
			System.out.println("\n++++++++++++++++++++++++++++++++++++++++++++++++++\n");*/
			stringBuffer.append(buildValue(entry.getKey(), entry.getValue())).append("\n");
		}
		String sql = stringBuffer.toString();
		System.out.println("stringBuffer:\n" + sql);
	}

	private String buildValue(String key, List<Map<String, String>> mapList) {
		List<Map<String,String>> phoneList = new ArrayList<>();
		for (Map<String, String> map : mapList) {
			String b = map.get("楼宇服务专员姓名及联系方式");
			if (StringUtils.isNotBlank(b)) {
				String[] split = b.split("\n");
				for (String value : split) {
					if (StringUtils.isNotBlank(value)) {
						StringBuilder stringBuffer = new StringBuilder();
						char[] chars = value.trim().toCharArray();
						boolean isNumeral = false;
						for (char aChar : chars) {
							if (!isNumeral) {
								isNumeral = StringUtil.isNumeral(aChar);
							}
							if (isNumeral && StringUtil.isChinese(aChar)) {
								isNumeral = false;
								stringBuffer.append(',');
							}
							stringBuffer.append(aChar);
						}
						String newValue = stringBuffer.toString();
						String[] split1 = newValue.split(",");
						for (String value2 : split1) {
							//去除无号码的
							if (value2.length() > 8) {
								//中文和号码分离
								char[] charArray = value2.toCharArray();
								StringBuilder name = new StringBuilder();
								StringBuilder phone = new StringBuilder();
								for (char aChar : charArray) {
									if (StringUtil.isChinese(aChar)) {
										name.append(aChar);
									}
									if (StringUtil.isNumeral(aChar)) {
										phone.append(aChar);
									}
								}
								Map<String, String> jsonObject = new LinkedHashMap<>();
								jsonObject.put("name", name.toString());
								jsonObject.put("phone", phone.toString());
								phoneList.add(jsonObject);
							} else {
								System.out.println(value2);
							}
						}
					}
				}
			}
		}
		//String join = StringUtils.join(phoneList, ",");
		String join = JsonUtil.toJson(phoneList);
		String sql = "INSERT INTO `article_content` (`parent_id`, `name`, `is_deleted`, `status`, `content`) VALUES (3, '" + key + "', 0, 40, '" + join + "');";
		return sql;
	}

	@Test
	public void test01() throws IOException {

		ExcelWriteUtils excelWriteUtils = ExcelWriteUtils.importExcel("E:\\dan\\数字重庆\\xjb\\【新】江北区找场地.xlsx", "Sheet1");
		//显示读取行号
		excelWriteUtils.setDebugNumFlag(false);
		excelWriteUtils.setImportDataFormat("yyyy-MM-dd");
		List<List<Map<String, String>>> listList = excelWriteUtils.readExcelWithoutTitle(true, 1, false);
		StringBuffer stringBuffer = new StringBuffer();
		for (List<Map<String, String>> listMap : listList) {
			for (Map<String, String> map : listMap) {
				//企业名称	地址	联系电话
				//stringBuffer.append("update article_content set address = '" + map.get("地址") + "',contact_phone = '" + map.get("联系电话") + "' where name = '" + map.get("企业名称") + "';\n");
				//String sql = "INSERT INTO `article_content` (`parent_id`, `name`, `is_deleted`, `status`, `content`, `sort`) VALUES (3, '" + key + "', 0, 40, '" + join + "','" + remark + "');";
				//序号	楼宇名称	楼宇地址	所属区域	联系人	联系方式
				//INSERT INTO `site_basic_info` (`name`, `area_name`, `sort_no`, `address`, `contact_name`, `phone`, `status`, `is_deleted`) VALUES ('1', '1', 1, '1', '1', '1', 40, 0)
				stringBuffer.append("INSERT INTO `site_basic_info` (`name`, `area_name`, `sort_no`, `address`, `contact_name`, `phone`, `status`, `is_deleted`) VALUES " +
						"('" + map.get("楼宇名称") + "', '" + map.get("所属区域") + "', " + new BigDecimal(map.get("序号")).toBigInteger() + ", '" + map.get("楼宇地址") + "', '" + map.get("联系人") + "', '" + map.get("联系方式") + "', 40, 0);\n");
			}
		}
		String sql = stringBuffer.toString();
		System.out.println("stringBuffer:\n" + sql);
	}

	@Test
	public void test02() throws IOException {
		ExcelWriteUtils excelWriteUtils = ExcelWriteUtils.importExcel("P:/修改结算里程数据.xlsx", "Sheet1");
		//显示读取行号
		excelWriteUtils.setDebugNumFlag(false);
		excelWriteUtils.setImportDataFormat("yyyy-MM-dd");
		List<List<Map<String, String>>> listList = excelWriteUtils.readExcelWithoutTitle(true, 1, false);
		StringBuffer stringBuffer = new StringBuffer();
		for (List<Map<String, String>> listMap : listList) {
			for (Map<String, String> map : listMap) {
				for (Map.Entry<String, String> entry : map.entrySet()) {
					System.out.print(entry.getKey() + "=========" + entry.getValue() + ",");

				}
				System.out.println("\n++++++++++++++++++++++++++++++++++++++++++++++++++\n");

				stringBuffer.append("update tk_order set statement_mileage = " + ((int) Math.rint(Double.parseDouble(map.get("结算里程")))) + " where id = '" + map.get("订单号") + "';\n");
			}
			String collect = listMap.stream().map(d -> d.get("工号")).collect(Collectors.joining("','"));
			System.out.println("collect:" + collect);
		}
		String sql = stringBuffer.toString();
		System.out.println("stringBuffer:" + sql);
	}

	@Test
	public void test03() throws IOException {
		ExcelWriteUtils excelWriteUtils = ExcelWriteUtils.importExcelAllSheet("P:/Excel_Template/挂车信息管理20211220153422.xlsx");
		//显示读取行号
		excelWriteUtils.setDebugNumFlag(false);
		List<List<Map<String, String>>> listList = excelWriteUtils.readExcelWithoutTitle(true, 1, false);
		StringBuffer stringBuffer = new StringBuffer();
		for (List<Map<String, String>> listMap : listList) {
			for (Map<String, String> map : listMap) {
				for (Map.Entry<String, String> entry : map.entrySet()) {
					System.out.print(entry.getKey() + "=========" + entry.getValue() + ",");
				}
				System.out.println("\n++++++++++++++++++++++++++++++++++++++++++++++++++\n");
				stringBuffer.append("update bd_trailer set car_register_company = '" + map.get("车辆登记公司") + "' where id = '" + map.get("挂车车牌号") + "';\n");
			}
		}
		String sql = stringBuffer.toString();
		System.out.println("stringBuffer:" + sql);
	}
}
