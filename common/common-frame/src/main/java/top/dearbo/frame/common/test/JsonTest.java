package top.dearbo.frame.common.test;

import org.junit.Test;
import top.dearbo.base.enums.ResultCodeEnum;
import top.dearbo.frame.common.test.entity.UserInfo;
import top.dearbo.util.constant.AjaxResult;
import top.dearbo.util.data.JacksonUtils;
import top.dearbo.util.data.JsonUtil;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @version 1.0
 * @author: Bo
 * @fileName: JsonTest
 * @createDate: 2020-06-01 12:11.
 * @description: json测试类
 */
public class JsonTest {

	@Test
	public void testJsonBean() {
		UserInfo userInfo = new UserInfo();
		userInfo.setAccount("账号");
		String toJson = JsonUtil.toJson(userInfo);
		UserInfo userInfo1 = JacksonUtils.jsonToBean(toJson, UserInfo.class);
		System.out.println("===");
	}

	@Test
	public void testJsonToMap() {
		long longValue = 2L;
		float fValue = 2.2f;
		String dataListJson = "[{\"nValue\":null,\"intValue\":1,\"longValue\":" + longValue + ",\"fValue\":" + fValue + ",\"sys_group_id_field\":1.0,\"payment_id\":5.47948455604081E-06,\"Custname\":\"飒拉商业（上海）有限公司广州汉溪大道分公司\",\"LL_Name\":\"广州番禺万达广场商业物业管理有限公司\",\"Pmt_Rate\":0.16,\"DescriptionKey\":\"elec\",\"Paid_Date\":\"2019-01-01\",\"Amount\":48716.2,\"inv_id\":20847,\"Custname_R\":\"飒拉商业（上海）有限公司广州汉溪大道分公司\",\"ClientName\":\"广州番禺万达广场商业物业管理有限公司\",\"Taxrate\":0.16,\"DescriptionKey_R\":\"elec\",\"Invdate\":\"2019-01-31\",\"TaxAmount\":48716.2,\"sys_match_type_field\":0,\"sys_match_comment_field\":null}]";
		String dataJson = "{\"nValue\":null,\"intValue\":1,\"longValue\":" + longValue + ",\"fValue\":" + fValue + ",\"sys_group_id_field\":1.0,\"payment_id\":5.47948455604081E-06,\"Custname\":\"飒拉商业（上海）有限公司广州汉溪大道分公司\",\"LL_Name\":\"广州番禺万达广场商业物业管理有限公司\",\"Pmt_Rate\":0.16,\"DescriptionKey\":\"elec\",\"Paid_Date\":\"2019-01-01\",\"Amount\":48716.2,\"inv_id\":20847,\"Custname_R\":\"飒拉商业（上海）有限公司广州汉溪大道分公司\",\"ClientName\":\"广州番禺万达广场商业物业管理有限公司\",\"Taxrate\":0.16,\"DescriptionKey_R\":\"elec\",\"Invdate\":\"2019-01-31\",\"TaxAmount\":48716.2,\"sys_match_type_field\":0,\"sys_match_comment_field\":null}";
		Map<String, Object> map = JsonUtil.toMap(dataJson);
		for (Map.Entry<String, Object> item : map.entrySet()) {
			System.out.println(item.getKey() + ":" + item.getValue());
		}
		//重新map处理数字逻辑
		List<Map<String, Object>> maps = JsonUtil.toMapList(dataListJson);
		List<Map<String, Object>> mapList = JacksonUtils.toMapList(dataListJson);
		System.out.println("\nhashMaps:" + maps.size());
		for (Map<String, Object> hashMap : maps) {
			for (Object o : hashMap.entrySet()) {
				System.out.println(o);
			}
		}
		System.out.println("\nJacksonUtils-mapList:" + mapList.size());
		for (Map<String, Object> hashMap : mapList) {
			for (Object o : hashMap.entrySet()) {
				System.out.println(o);
			}
		}
	}


	@Test
	public void testJsonToMapNumber() {
		String json = "{\"ratedShorttermWithstandTime\":3,\"source#Name\":\"设备增加-其他途径\",\"operationMode#Name\":\"电动\",\"source\":\"10\",\"isCommission#Name\":\"否\",\"mechanismType#Name\":\"单相操作\",\"astOrg\":\"8a3983714c26a51f014c26a56b9b0087\",\"combinationType\":\"01\",\"ctime\":\"1998-09-29 00:00:00\",\"mechanismType\":\"03\",\"model\":\"GW12A-550DW\",\"ratedFrequency\":50,\"phase\":\"5\",\"manufactureNum\":\"20068.08\",\"equipCode\":\"20M01000034261983\",\"isCommission\":\"0\",\"astType#Name\":\"接地开关\",\"phase#Name\":\"ABC\",\"mainLoopResistance\":200,\"astId\":\"404e71a3-b58e-e808-e053-360ab90ac9c8w11np1\",\"ratedCurrent\":4000,\"manufactureCountry#Name\":\"中国\",\"operateDate\":\"1998-09-26 00:00:00\",\"deployState\":\"30\",\"astNature\":\"03\",\"mechanismPhasesNum\":\"01\",\"phasesQuantity\":\"3\",\"manufacturer\":\"009562\",\"operationMode\":\"02\",\"astOrgName\":\"国网重庆市电力公司超高压分公司\",\"ratedVoltage\":\"550\",\"disconnector\":\"3DC848B9-A414-4172-9CC3-AA42E6F39EE9-03824\",\"phasesQuantity#Name\":\"三相\",\"astType\":\"2\",\"manufactureDate\":\"1997-09-26 00:00:00\",\"thermalCurrent\":63,\"astNature#Name\":\"省（直辖市、自治区）公司\",\"mechanismPhasesNum#Name\":\"\",\"manufactureCountry\":\"CN\",\"dynamicCurrent\":160,\"manufacturer#Name\":\"新东北电气集团高压开关有限公司\",\"wbsNum\":\"PM2013\",\"deployState#Name\":\"退役\",\"combinationType#Name\":\"否\"}";
		String jsonArray = "[{\"ratedShorttermWithstandTime\":3,\"source#Name\":\"设备增加-其他途径\",\"operationMode#Name\":\"电动\",\"source\":\"10\",\"isCommission#Name\":\"否\",\"mechanismType#Name\":\"单相操作\",\"astOrg\":\"8a3983714c26a51f014c26a56b9b0087\",\"combinationType\":\"01\",\"ctime\":\"1998-09-29 00:00:00\",\"mechanismType\":\"03\",\"model\":\"GW12A-550DW\",\"ratedFrequency\":50,\"phase\":\"5\",\"manufactureNum\":\"20068.08\",\"equipCode\":\"20M01000034261983\",\"isCommission\":\"0\",\"astType#Name\":\"接地开关\",\"phase#Name\":\"ABC\",\"mainLoopResistance\":200,\"astId\":\"404e71a3-b58e-e808-e053-360ab90ac9c8w11np1\",\"ratedCurrent\":4000,\"manufactureCountry#Name\":\"中国\",\"operateDate\":\"1998-09-26 00:00:00\",\"deployState\":\"30\",\"astNature\":\"03\",\"mechanismPhasesNum\":\"01\",\"phasesQuantity\":\"3\",\"manufacturer\":\"009562\",\"operationMode\":\"02\",\"astOrgName\":\"国网重庆市电力公司超高压分公司\",\"ratedVoltage\":\"550\",\"disconnector\":\"3DC848B9-A414-4172-9CC3-AA42E6F39EE9-03824\",\"phasesQuantity#Name\":\"三相\",\"astType\":\"2\",\"manufactureDate\":\"1997-09-26 00:00:00\",\"thermalCurrent\":63,\"astNature#Name\":\"省（直辖市、自治区）公司\",\"mechanismPhasesNum#Name\":\"\",\"manufactureCountry\":\"CN\",\"dynamicCurrent\":160,\"manufacturer#Name\":\"新东北电气集团高压开关有限公司\",\"wbsNum\":\"PM2013\",\"deployState#Name\":\"退役\",\"combinationType#Name\":\"否\"}]";
		//String json = "{\"equipForm\":\"01\",\"source\":\"01\",\"astOrg\":\"8a3983714c26a51f014c26a56b9b0087\",\"ctime\":1639152000000,\"equipCode\":\"20M01000045763120\",\"isDomestic\":\"1\",\"astId\":\"A73512691C8A398A5B7D86A5FD017DA73507500BF5\",\"ctRatedCurrent\":1,\"projectNum\":\"FM2013\",\"projectName\":\"FM2013\",\"deployState\":\"20\",\"astNature\":\"03\",\"manufacturer\":\"19117\",\"astOrgName\":\"国网重庆市电力公司超高压分公司\",\"manufactureDate\":1149264000000,\"startTime\":1194364800000,\"manufactureCountry\":\"CN\",\"wbsNum\":\"FM2013\",\"utcNum\":\"zStlNwpKInHnG1Fsr5E5Eww6\",\"deviceModel\":\"RCS931E\"}";
		//String jsonArray = "[{\"equipForm\":\"01\",\"source\":\"01\",\"astOrg\":\"8a3983714c26a51f014c26a56b9b0087\",\"ctime\":1639152000000,\"equipCode\":\"20M01000045763120\",\"isDomestic\":\"1\",\"astId\":\"A73512691C8A398A5B7D86A5FD017DA73507500BF5\",\"ctRatedCurrent\":1,\"projectNum\":\"FM2013\",\"projectName\":\"FM2013\",\"deployState\":\"20\",\"astNature\":\"03\",\"manufacturer\":\"19117\",\"astOrgName\":\"国网重庆市电力公司超高压分公司\",\"manufactureDate\":1149264000000,\"startTime\":1194364800000,\"manufactureCountry\":\"CN\",\"wbsNum\":\"FM2013\",\"utcNum\":\"zStlNwpKInHnG1Fsr5E5Eww6\",\"deviceModel\":\"RCS931E\"}]";
		Map<String, Object> objectMap = JsonUtil.toMap(json);
		List<Map<String, Object>> mapList = JsonUtil.toMapList(jsonArray);
		System.out.println(JsonUtil.GSON_MAP.toJson(objectMap));
		System.out.println(JsonUtil.GSON_MAP.toJson(mapList));

		System.out.println(JsonUtil.toJson(AjaxResult.success()));
		System.out.println(JsonUtil.toJson(AjaxResult.failed()));
		AjaxResult.resetSuccess(ResultCodeEnum.SUCCESS_200.getCode(), ResultCodeEnum.SUCCESS_200.getValue());
		System.out.println(JsonUtil.toJson(AjaxResult.success()));
		System.out.println(JsonUtil.toJson(AjaxResult.failed()));
	}


}
