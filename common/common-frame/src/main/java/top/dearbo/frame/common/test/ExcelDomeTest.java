package top.dearbo.frame.common.test;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import top.dearbo.frame.common.util.excel.ExcelWriteUtils;
import top.dearbo.util.data.JsonUtil;
import top.dearbo.util.exception.AppException;
import top.dearbo.util.lang.DateUtil;
import top.dearbo.util.lang.StringUtil;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author wb
 * @date 2021-07-29 15:05.
 */
public class ExcelDomeTest {

	@Test
	public void test05() throws IOException {
		//1-国家级，2-省级，3-市级，4-区县级，9-其他
		Map<String, String> levelMap = new HashMap<>();
		levelMap.put("国家级", "1");
		levelMap.put("省级", "2");
		levelMap.put("市级", "3");
		levelMap.put("区县级", "4");
		levelMap.put("区级", "4");
		levelMap.put("其他", "9");
		//市级：industry_type：产业：1-汽车产业,2-生物医药,3-电子信息,4-金融产业,5-其他
		//小江北：industry_type：产业：1-制造业,2-商贸业,3-软件和信息业,4-金融业,5-其他服务业
		Map<String, String> policyTypeMap = new HashMap<>();
		/*policyTypeMap.put("汽车产业", "1");
		policyTypeMap.put("生物医药", "2");
		policyTypeMap.put("电子信息", "3");
		policyTypeMap.put("金融产业", "5");
		policyTypeMap.put("其他", "4");*/
		policyTypeMap.put("制造业", "1");
		policyTypeMap.put("商贸业", "2");
		policyTypeMap.put("金融业", "4");
		policyTypeMap.put("金融产业", "4");
		policyTypeMap.put("软件和信息业", "3");
		policyTypeMap.put("其他服务业", "5");
		//type:政策分类：1-资金补助、2-税费减免、3-资质荣誉、4-专项政策、5-其他
		Map<String, String> industryMap = new HashMap<>();
		industryMap.put("资金补助", "1");
		industryMap.put("税费减免", "2");
		industryMap.put("资质荣誉", "3");
		industryMap.put("专项政策", "4");
		industryMap.put("专项", "4");
		industryMap.put("其他", "5");
		//ExcelWriteUtils excelWriteUtils = ExcelWriteUtils.importExcel("E:\\dan\\数字重庆\\市级\\政策拆解0714.xlsx", "Sheet1");
		ExcelWriteUtils excelWriteUtils = ExcelWriteUtils.importExcel("E:\\dan\\数字重庆\\小江办\\政策拆解（江北）_20220722.xlsx", "Sheet1");
		//显示读取行号
		excelWriteUtils.setDebugNumFlag(false);
		excelWriteUtils.setImportDataFormat("yyyy-MM-dd");

		List<List<Map<String, String>>> listList = excelWriteUtils.readExcelWithoutTitle(true, 1, false);
		StringBuffer stringBuffer = new StringBuffer();
		for (List<Map<String, String>> listMap : listList) {
			for (Map<String, String> map : listMap) {
				//System.out.println("发布日期：" + getFormatStringDate(map.get("发布日期")) + "，申报时间：" + getFormatStringDate(map.get("申报时间")) + "，截止时间：" + getFormatStringDate(map.get("截止时间")));
				//System.out.println(buildTitleUrl(map.get("政策原文"), map.get("URL"), map.get("序号")));
				//System.out.println(transformValue(industryMap, map.get("政策分类"), map.get("序号")));
				//System.out.println(transformValue(policyTypeMap, map.get("所属产业"), map.get("序号")));
				String no = new BigDecimal(map.get("序号")).toBigInteger().toString();
				//1-申报类 2-非申报类
				int policyType = map.get("是否申报类政策").equals("是") ? 1 : 2;
				String publishDate = getFormatStringDate(map.get("发布日期"));
				//序号	政策名称	发布机构	发布日期	申报时间	截止时间	是否申报类政策	政策层级	政策分类	所属产业	奖励额度/减免方式	申报条件	申报材料	申报方式	联系方式	适用对象	政策摘要	要点解读	政策原文	URL
				String sql = "INSERT INTO `policy_info_sync` (`code`, `abbr`, `start_application_date`, `end_application_date`, `issuing_department`, `target_group`, `application_form`, `application_condition`, `application_materials`, `level`, `industry_type`, `type`, `publish_date`, `contact_name`, `summary`, `interpret`, `content`, `is_deleted`, `status`, `relative_policy`, `policy_type`) VALUES " +
						"('" + no + "', " + getSqlString(map.get("政策名称")) + ", " + getSqlString(getFormatStringDate(map.get("申报时间"))) + ", " + getSqlString(getFormatStringDate(map.get("截止时间"))) +
						"," + getSqlString(map.get("发布机构")) + ", " + getSqlString(map.get("适用对象")) + ", " + getSqlString(map.get("申报方式")) +
						", " + getSqlString(map.get("申报条件")) + ", " + getSqlString(map.get("申报材料")) +
						", " + levelMap.get(map.get("政策层级")) + ", " + getSqlString(transformValue(policyTypeMap, map.get("所属产业"), no)) + ", " + getSqlString(transformValue(industryMap, map.get("政策分类"), no)) +
						", " + getSqlString(publishDate == null ? null : publishDate + " 00:00:00") + ", " + getSqlString(map.get("联系方式")) + ", " + getSqlString(map.get("政策摘要")) + ", " + getSqlString(map.get("要点解读")) +
						", " + getSqlString(map.get("奖励额度/减免方式")) + " , 0, 40, " + getSqlString(buildTitleUrl(map.get("政策原文"), map.get("URL"), no)) + ", " + policyType + ");";
				//"('"+no+"', '政策名称', '2022-04-12', '2022-07-20', '发布机构', '适用对象', '申请方式', '申请条件', '申请材料', 1, '1,2', '1,2', '2022-04-12 00:00:00', '联系方式', '政策摘要', '要点解读', 0, 40, '标题+url', 1);";
				stringBuffer.append(sql).append("\n");
			}
		}
		String sql = stringBuffer.toString();
		System.out.println("stringBuffer:\n" + sql);
	}


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
			stringBuffer.append(buildTest04Value(entry.getKey(), entry.getValue())).append("\n");
		}
		String sql = stringBuffer.toString();
		System.out.println("stringBuffer:\n" + sql);
	}

	private String buildTest04Value(String key, List<Map<String, String>> mapList) {
		List<Map<String, String>> phoneList = new ArrayList<>();
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

		ExcelWriteUtils excelWriteUtils = ExcelWriteUtils.importExcel("E:\\dan\\数字重庆\\市监\\个体工商户.xlsx", "Sheet1");
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
				//E:\dan\数字重庆\xjb\【新】江北区找场地.xlsx：序号	楼宇名称	楼宇地址	所属区域	联系人	联系方式
				//INSERT INTO `site_basic_info` (`name`, `area_name`, `sort_no`, `address`, `contact_name`, `phone`, `status`, `is_deleted`) VALUES ('1', '1', 1, '1', '1', '1', 40, 0)
				//String sql = "INSERT INTO `site_basic_info` (`name`, `area_name`, `sort_no`, `address`, `contact_name`, `phone`, `status`, `is_deleted`) VALUES ('" + map.get("楼宇名称") + "', '" + map.get("所属区域") + "', " + new BigDecimal(map.get("序号")).toBigInteger() + ", '" + map.get("楼宇地址") + "', '" + map.get("联系人") + "', '" + map.get("联系方式") + "', 40, 0);\n";
				String sql = "INSERT INTO `article_content` ( `parent_id`, `name`,`link`, `is_deleted`, `publish_date`, `sort`,`status`) VALUES ( 1, '" + map.get("标题") + "'," +
						" '" + map.get("引用页面地址") + "', 0, '" + getFormatStringDate(map.get("发布时间")) + " 00:00:00" + "', 3, 40);\n";
				stringBuffer.append(sql);
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

	public String transformValue(Map<String, String> map, String value, String no) {
		if (StringUtils.isNotBlank(value)) {
			value = StringUtils.remove(value.trim(), "\n");
			String splitChar = ",";
			if (value.contains("、")) {
				splitChar = "、";
			} else if (value.contains("，")) {
				splitChar = "，";
			}
			String[] splitArray = value.split(splitChar);
			List<String> stringList = new ArrayList<>();
			for (String split : splitArray) {
				String mapValue = map.get(split);
				if (mapValue == null) {
					AppException.throwEx(no + "获取值为空,value：" + value + ",map:" + JsonUtil.toJson(map));
				}
				stringList.add(mapValue);
			}
			return StringUtils.join(stringList, ",");
		}
		return value;
	}

	public String buildTitleUrl(String title, String url, String no) {
		if (StringUtils.isBlank(title) || StringUtils.isBlank(url)) {
			AppException.throwEx("存在标题/Url为空：title:" + title + ",url:" + url);
		}
		List<String> removeList = Arrays.asList("\n", "（一）");
		List<String> replaceList = Arrays.asList("（二）", "（三）", "（四）", "（五）", "（六）");
		for (String removeValue : removeList) {
			title = StringUtils.remove(title, removeValue);
			url = StringUtils.remove(url, removeValue);
		}
		for (String replaceValue : replaceList) {
			title = title.replaceAll(replaceValue, ",");
			url = url.replaceAll(replaceValue, ",");
		}
		String[] titleArray = title.split(",");
		String[] urlArray = url.split(",");
		if (titleArray.length != urlArray.length) {
			AppException.throwEx(no + "长度不一致：【title：" + title + "，url：" + url + "】titleArray：" + titleArray.length + ",urlArray：" + urlArray.length);
		}
		List<Map<String, String>> mapList = new ArrayList<>();
		Map<String, String> map = new LinkedHashMap<>();
		for (int i = 0; i < titleArray.length; i++) {
			map.put(titleArray[i], urlArray[i]);
		}
		Map<String, String> dataMap;
		for (Map.Entry<String, String> item : map.entrySet()) {
			dataMap = new LinkedHashMap<>();
			dataMap.put("title", item.getKey());
			dataMap.put("url", item.getValue());
			mapList.add(dataMap);
		}
		return JsonUtil.toJson(mapList);
	}

	public String getFormatStringDate(String value) {
		if (StringUtils.isBlank(value)) {
			return null;
		}
		value = StringUtils.remove(value, " ").trim();
		value = StringUtils.remove(value, " ");
		if (value.contains("-")) {
			return value;
		}
		if (value.endsWith("年")) {
			value = value + "12月31日";
			Date date = DateUtil.parseToDate(value, "yyyy年MM月dd日");
			return DateUtil.parseToString(date, "yyyy-MM-dd");
		} else if (value.endsWith(".0")) {
			Date javaDate = ExcelWriteUtils.getJavaDate(Double.parseDouble(value));
			return DateUtil.parseToString(javaDate, "yyyy-MM-dd");
		} else if (value.contains("年") && value.contains("月") && value.contains("日")) {
			Date date = DateUtil.parseToDate(value, "yyyy年MM月dd日");
			return DateUtil.parseToString(date, "yyyy-MM-dd");
		} else {
			System.out.println("未知结果：" + value);
		}
		return value;
	}

	public String getSqlString(String value) {
		if (value == null) {
			return null;
		}
		return "'" + value + "'";
	}
}
