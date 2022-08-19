package top.dearbo.frame.common.test;

import org.junit.Test;
import top.dearbo.frame.common.test.entity.UserInfo;
import top.dearbo.util.data.JacksonUtils;
import top.dearbo.util.data.JsonUtil;

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
		String dataJson = "[{\"sys_group_id_field\":1.0,\"payment_id\":5.47948455604081E-06,\"Custname\":\"飒拉商业（上海）有限公司广州汉溪大道分公司\",\"LL_Name\":\"广州番禺万达广场商业物业管理有限公司\",\"Pmt_Rate\":0.16,\"DescriptionKey\":\"elec\",\"Paid_Date\":\"2019-01-01\",\"Amount\":48716.2,\"inv_id\":20847,\"Custname_R\":\"飒拉商业（上海）有限公司广州汉溪大道分公司\",\"ClientName\":\"广州番禺万达广场商业物业管理有限公司\",\"Taxrate\":0.16,\"DescriptionKey_R\":\"elec\",\"Invdate\":\"2019-01-31\",\"TaxAmount\":48716.2,\"sys_match_type_field\":0,\"sys_match_comment_field\":null},{\"sys_group_id_field\":2,\"payment_id\":13844,\"Custname\":\"波丝可商业（北京）有限公司重庆时代天街分公司\",\"LL_Name\":\"重庆龙湖成恒地产开发有限公司\",\"Pmt_Rate\":0.05,\"DescriptionKey\":\"rental\",\"Paid_Date\":\"2019-01-15\",\"Amount\":34026.76,\"inv_id\":20764,\"Custname_R\":\"波丝可商业（北京）有限公司重庆时代天街分公司\",\"ClientName\":\"重庆龙湖成恒地产开发有限公司\",\"Taxrate\":0.05,\"DescriptionKey_R\":\"rental\",\"Invdate\":\"2019-01-21\",\"TaxAmount\":34026.76,\"sys_match_type_field\":0,\"sys_match_comment_field\":null},{\"sys_group_id_field\":3,\"payment_id\":13911,\"Custname\":\"普安倍尔商业（北京）有限公司重庆时代天街分公司\",\"LL_Name\":\"重庆龙湖成恒地产开发有限公司\",\"Pmt_Rate\":0.05,\"DescriptionKey\":\"rental\",\"Paid_Date\":\"2019-01-15\",\"Amount\":42724.11,\"inv_id\":20763,\"Custname_R\":\"普安倍尔商业（北京）有限公司重庆时代天街分公司\",\"ClientName\":\"重庆龙湖成恒地产开发有限公司\",\"Taxrate\":0.05,\"DescriptionKey_R\":\"rental\",\"Invdate\":\"2019-01-21\",\"TaxAmount\":42724.11,\"sys_match_type_field\":0,\"sys_match_comment_field\":null},{\"sys_group_id_field\":4,\"payment_id\":13948,\"Custname\":\"飒拉商业（北京）有限公司哈尔滨中兴大道第二分公司\",\"LL_Name\":\"哈尔滨哈西万达广场有限公司\",\"Pmt_Rate\":0.05,\"DescriptionKey\":\"rental\",\"Paid_Date\":\"2019-01-15\",\"Amount\":66325.56,\"inv_id\":20818,\"Custname_R\":\"飒拉商业（北京）有限公司哈尔滨中兴大道第二分公司\",\"ClientName\":\"哈尔滨哈西万达广场有限公司\",\"Taxrate\":0.05,\"DescriptionKey_R\":\"rental\",\"Invdate\":\"2019-01-22\",\"TaxAmount\":66325.56,\"sys_match_type_field\":0,\"sys_match_comment_field\":null},{\"sys_group_id_field\":5,\"payment_id\":13994,\"Custname\":\"飒拉商业（上海）有限公司武汉唐家墩路分公司\",\"LL_Name\":\"武汉万达广场投资有限公司\",\"Pmt_Rate\":0.05,\"DescriptionKey\":\"rental\",\"Paid_Date\":\"2019-01-15\",\"Amount\":91516.08,\"inv_id\":20832,\"Custname_R\":\"飒拉商业（上海）有限公司武汉唐家墩路分公司\",\"ClientName\":\"武汉万达广场投资有限公司\",\"Taxrate\":0.05,\"DescriptionKey_R\":\"rental\",\"Invdate\":\"2019-01-16\",\"TaxAmount\":91516.08,\"sys_match_type_field\":0,\"sys_match_comment_field\":null},{\"sys_group_id_field\":6,\"payment_id\":14035,\"Custname\":\"普安倍尔商业（北京）有限公司重庆时代天街分公司\",\"LL_Name\":\"重庆龙湖成恒地产开发有限公司\",\"Pmt_Rate\":0.16,\"DescriptionKey\":\"elec\",\"Paid_Date\":\"2019-01-15\",\"Amount\":7739.58,\"inv_id\":20766,\"Custname_R\":\"普安倍尔商业（北京）有限公司重庆时代天街分公司\",\"ClientName\":\"重庆龙湖成恒地产开发有限公司\",\"Taxrate\":0.16,\"DescriptionKey_R\":\"elec\",\"Invdate\":\"2019-01-21\",\"TaxAmount\":7739.58,\"sys_match_type_field\":0,\"sys_match_comment_field\":null},{\"sys_group_id_field\":7,\"payment_id\":14368,\"Custname\":\"麦西姆杜特商业（上海）有限公司常州通江中路分公司\",\"LL_Name\":\"常州万达广场商业管理有限公司\",\"Pmt_Rate\":0.16,\"DescriptionKey\":\"elec\",\"Paid_Date\":\"2019-02-01\",\"Amount\":9411.49,\"inv_id\":20828,\"Custname_R\":\"麦西姆杜特商业（上海）有限公司常州通江中路分公司\",\"ClientName\":\"常州万达广场商业管理有限公司\",\"Taxrate\":0.16,\"DescriptionKey_R\":\"elec\",\"Invdate\":\"2019-02-02\",\"TaxAmount\":9411.49,\"sys_match_type_field\":0,\"sys_match_comment_field\":null},{\"sys_group_id_field\":8,\"payment_id\":14434,\"Custname\":\"奥依修商贸（上海）有限公司常州通江中路分公司\",\"LL_Name\":\"常州万达广场商业管理有限公司\",\"Pmt_Rate\":0.16,\"DescriptionKey\":\"elec\",\"Paid_Date\":\"2019-02-01\",\"Amount\":5687.83,\"inv_id\":20825,\"Custname_R\":\"奥依修商贸（上海）有限公司常州通江中路分公司\",\"ClientName\":\"常州万达广场商业管理有限公司\",\"Taxrate\":0.16,\"DescriptionKey_R\":\"elec\",\"Invdate\":\"2019-02-02\",\"TaxAmount\":5687.83,\"sys_match_type_field\":0,\"sys_match_comment_field\":null},{\"sys_group_id_field\":9,\"payment_id\":14585,\"Custname\":\"飒拉家居商贸（上海）有限公司北京朝阳北路分公司\",\"LL_Name\":\"中粮地产集团深圳物业管理有限公司北京朝阳分公司\",\"Pmt_Rate\":0.16,\"DescriptionKey\":\"elec\",\"Paid_Date\":\"2019-02-01\",\"Amount\":14374.8,\"inv_id\":20912,\"Custname_R\":\"飒拉家居商贸（上海）有限公司北京朝阳北路分公司\",\"ClientName\":\"中粮地产集团深圳物业管理有限公司北京朝阳分公司\",\"Taxrate\":0.16,\"DescriptionKey_R\":\"elec\",\"Invdate\":\"2019-02-11\",\"TaxAmount\":14374.8,\"sys_match_type_field\":0,\"sys_match_comment_field\":null},{\"sys_group_id_field\":10,\"payment_id\":14635,\"Custname\":\"飒拉商业（上海）有限公司武汉唐家墩路分公司\",\"LL_Name\":\"武汉万达广场商业管理有限公司\",\"Pmt_Rate\":0.16,\"DescriptionKey\":\"elec\",\"Paid_Date\":\"2019-02-01\",\"Amount\":29312,\"inv_id\":20864,\"Custname_R\":\"飒拉商业（上海）有限公司武汉唐家墩路分公司\",\"ClientName\":\"武汉万达广场商业管理有限公司\",\"Taxrate\":0.16,\"DescriptionKey_R\":\"elec\",\"Invdate\":\"2019-02-01\",\"TaxAmount\":29312,\"sys_match_type_field\":0,\"sys_match_comment_field\":null},{\"sys_group_id_field\":11,\"payment_id\":14647,\"Custname\":\"飒拉商业（上海）有限公司常州通江中路分公司\",\"LL_Name\":\"常州万达广场商业管理有限公司\",\"Pmt_Rate\":0.16,\"DescriptionKey\":\"elec\",\"Paid_Date\":\"2019-02-01\",\"Amount\":39136.99,\"inv_id\":20831,\"Custname_R\":\"飒拉商业（上海）有限公司常州通江中路分公司\",\"ClientName\":\"常州万达广场商业管理有限公司\",\"Taxrate\":0.16,\"DescriptionKey_R\":\"elec\",\"Invdate\":\"2019-02-02\",\"TaxAmount\":39136.99,\"sys_match_type_field\":0,\"sys_match_comment_field\":null},{\"sys_group_id_field\":12,\"payment_id\":14736,\"Custname\":\"波丝可商业（北京）有限公司重庆时代天街分公司\",\"LL_Name\":\"重庆龙湖成恒地产开发有限公司\",\"Pmt_Rate\":0.05,\"DescriptionKey\":\"rental\",\"Paid_Date\":\"2019-02-15\",\"Amount\":44566.21,\"inv_id\":20771,\"Custname_R\":\"波丝可商业（北京）有限公司重庆时代天街分公司\",\"ClientName\":\"重庆龙湖成恒地产开发有限公司\",\"Taxrate\":0.05,\"DescriptionKey_R\":\"rental\",\"Invdate\":\"2019-02-13\",\"TaxAmount\":44566.21,\"sys_match_type_field\":0,\"sys_match_comment_field\":null},{\"sys_group_id_field\":13,\"payment_id\":14771,\"Custname\":\"波丝可商业（北京）有限公司长沙枫林三路分公司\",\"LL_Name\":\"步步高商业连锁股份有限公司梅溪湖分公司\",\"Pmt_Rate\":0.05,\"DescriptionKey\":\"rental\",\"Paid_Date\":\"2019-02-15\",\"Amount\":27891.6,\"inv_id\":20785,\"Custname_R\":\"波丝可商业（北京）有限公司长沙枫林三路分公司\",\"ClientName\":\"步步高商业连锁股份有限公司梅溪湖分公司\",\"Taxrate\":0.05,\"DescriptionKey_R\":\"rental\",\"Invdate\":\"2019-02-16\",\"TaxAmount\":27891.6,\"sys_match_type_field\":0,\"sys_match_comment_field\":null},{\"sys_group_id_field\":14,\"payment_id\":14775,\"Custname\":\"波丝可商业（北京）有限公司深圳滨河大道分公司\",\"LL_Name\":\"深圳市京基百纳商业管理有限公司京基百纳时代店\",\"Pmt_Rate\":0.05,\"DescriptionKey\":\"rental\",\"Paid_Date\":\"2019-02-15\",\"Amount\":50189.88,\"inv_id\":20888,\"Custname_R\":\"波丝可商业（北京）有限公司深圳滨河大道分公司\",\"ClientName\":\"深圳市京基百纳商业管理有限公司京基百纳时代店\",\"Taxrate\":0.05,\"DescriptionKey_R\":\"rental\",\"Invdate\":\"2019-02-12\",\"TaxAmount\":50189.88,\"sys_match_type_field\":0,\"sys_match_comment_field\":null},{\"sys_group_id_field\":15,\"payment_id\":14804,\"Custname\":\"麦西姆杜特商业（上海）有限公司扬州京华城路分公司\",\"LL_Name\":\"扬州京国实业有限公司\",\"Pmt_Rate\":0.06,\"DescriptionKey\":\"Mgt\",\"Paid_Date\":\"2019-02-15\",\"Amount\":14895.8,\"inv_id\":20917,\"Custname_R\":\"麦西姆杜特商业（上海）有限公司扬州京华城路分公司\",\"ClientName\":\"扬州京国实业有限公司\",\"Taxrate\":0.06,\"DescriptionKey_R\":\"Mgt\",\"Invdate\":\"2019-02-15\",\"TaxAmount\":14895.8,\"sys_match_type_field\":0,\"sys_match_comment_field\":null},{\"sys_group_id_field\":16,\"payment_id\":14857,\"Custname\":\"麦西姆杜特商业（上海）有限公司沈阳北二中路分公司\",\"LL_Name\":\"星摩尔（沈阳）商业有限公司\",\"Pmt_Rate\":0.05,\"DescriptionKey\":\"rental\",\"Paid_Date\":\"2019-02-15\",\"Amount\":44508.6,\"inv_id\":20837,\"Custname_R\":\"麦西姆杜特商业（上海）有限公司沈阳北二中路分公司\",\"ClientName\":\"星摩尔（沈阳）商业有限公司\",\"Taxrate\":0.05,\"DescriptionKey_R\":\"rental\",\"Invdate\":\"2019-02-15\",\"TaxAmount\":44508.6,\"sys_match_type_field\":0,\"sys_match_comment_field\":null},{\"sys_group_id_field\":17,\"payment_id\":14863,\"Custname\":\"麦西姆杜特商业（上海）有限公司常州通江中路分公司\",\"LL_Name\":\"常州新北万达广场投资有限公司\",\"Pmt_Rate\":0.05,\"DescriptionKey\":\"rental\",\"Paid_Date\":\"2019-02-15\",\"Amount\":57553.15,\"inv_id\":20884,\"Custname_R\":\"麦西姆杜特商业（上海）有限公司常州通江中路分公司\",\"ClientName\":\"常州新北万达广场投资有限公司\",\"Taxrate\":0.05,\"DescriptionKey_R\":\"rental\",\"Invdate\":\"2019-02-15\",\"TaxAmount\":57553.15,\"sys_match_type_field\":0,\"sys_match_comment_field\":null},{\"sys_group_id_field\":18,\"payment_id\":14874,\"Custname\":\"麦西姆杜特商业（上海）有限公司扬州京华城路分公司\",\"LL_Name\":\"扬州京国实业有限公司\",\"Pmt_Rate\":0.05,\"DescriptionKey\":\"rental\",\"Paid_Date\":\"2019-02-15\",\"Amount\":53912.16,\"inv_id\":20916,\"Custname_R\":\"麦西姆杜特商业（上海）有限公司扬州京华城路分公司\",\"ClientName\":\"扬州京国实业有限公司\",\"Taxrate\":0.05,\"DescriptionKey_R\":\"rental\",\"Invdate\":\"2019-02-15\",\"TaxAmount\":53912.16,\"sys_match_type_field\":0,\"sys_match_comment_field\":null},{\"sys_group_id_field\":19,\"payment_id\":14958,\"Custname\":\"奥依修商贸（上海）有限公司深圳华富路分公司\",\"LL_Name\":\"深圳市中航城置业发展有限公司\",\"Pmt_Rate\":0.05,\"DescriptionKey\":\"rental\",\"Paid_Date\":\"2019-02-15\",\"Amount\":1186.5,\"inv_id\":20844,\"Custname_R\":\"奥依修商贸（上海）有限公司深圳华富路分公司\",\"ClientName\":\"深圳市中航城置业发展有限公司\",\"Taxrate\":0.05,\"DescriptionKey_R\":\"rental\",\"Invdate\":\"2019-02-13\",\"TaxAmount\":1186.5,\"sys_match_type_field\":0,\"sys_match_comment_field\":null},{\"sys_group_id_field\":20,\"payment_id\":15003,\"Custname\":\"奥依修商贸（上海）有限公司常州通江中路分公司\",\"LL_Name\":\"常州新北万达广场投资有限公司\",\"Pmt_Rate\":0.05,\"DescriptionKey\":\"rental\",\"Paid_Date\":\"2019-02-15\",\"Amount\":49499.5,\"inv_id\":20883,\"Custname_R\":\"奥依修商贸（上海）有限公司常州通江中路分公司\",\"ClientName\":\"常州新北万达广场投资有限公司\",\"Taxrate\":0.05,\"DescriptionKey_R\":\"rental\",\"Invdate\":\"2019-02-15\",\"TaxAmount\":49499.5,\"sys_match_type_field\":0,\"sys_match_comment_field\":null},{\"sys_group_id_field\":21,\"payment_id\":15027,\"Custname\":\"奥依修商贸（上海）有限公司重庆时代天街分公司\",\"LL_Name\":\"重庆龙湖成恒地产开发有限公司\",\"Pmt_Rate\":0.05,\"DescriptionKey\":\"rental\",\"Paid_Date\":\"2019-02-15\",\"Amount\":32397.86,\"inv_id\":20772,\"Custname_R\":\"奥依修商贸（上海）有限公司重庆时代天街分公司\",\"ClientName\":\"重庆龙湖成恒地产开发有限公司\",\"Taxrate\":0.05,\"DescriptionKey_R\":\"rental\",\"Invdate\":\"2019-02-13\",\"TaxAmount\":32397.86,\"sys_match_type_field\":0,\"sys_match_comment_field\":null},{\"sys_group_id_field\":22,\"payment_id\":15087,\"Custname\":\"普安倍尔商业（北京）有限公司沈阳北二中路分公司\",\"LL_Name\":\"星摩尔（沈阳）商业有限公司\",\"Pmt_Rate\":0.05,\"DescriptionKey\":\"rental\",\"Paid_Date\":\"2019-02-15\",\"Amount\":35394.01,\"inv_id\":20838,\"Custname_R\":\"普安倍尔商业（北京）有限公司沈阳北二中路分公司\",\"ClientName\":\"星摩尔（沈阳）商业有限公司\",\"Taxrate\":0.05,\"DescriptionKey_R\":\"rental\",\"Invdate\":\"2019-02-15\",\"TaxAmount\":35394.01,\"sys_match_type_field\":0,\"sys_match_comment_field\":null},{\"sys_group_id_field\":23,\"payment_id\":15120,\"Custname\":\"普安倍尔商业（北京）有限公司重庆时代天街分公司\",\"LL_Name\":\"重庆龙湖成恒地产开发有限公司\",\"Pmt_Rate\":0.05,\"DescriptionKey\":\"rental\",\"Paid_Date\":\"2019-02-15\",\"Amount\":53788.98,\"inv_id\":20769,\"Custname_R\":\"普安倍尔商业（北京）有限公司重庆时代天街分公司\",\"ClientName\":\"重庆龙湖成恒地产开发有限公司\",\"Taxrate\":0.05,\"DescriptionKey_R\":\"rental\",\"Invdate\":\"2019-02-13\",\"TaxAmount\":53788.98,\"sys_match_type_field\":0,\"sys_match_comment_field\":null},{\"sys_group_id_field\":24,\"payment_id\":15123,\"Custname\":\"普安倍尔商业（北京）有限公司宁波天童南路分公司\",\"LL_Name\":\"银泰百货宁波鄞州有限公司环球城分公司\",\"Pmt_Rate\":0.1,\"DescriptionKey\":\"rental\",\"Paid_Date\":\"2019-02-15\",\"Amount\":34336.95,\"inv_id\":20810,\"Custname_R\":\"普安倍尔商业（北京）有限公司宁波天童南路分公司\",\"ClientName\":\"银泰百货宁波鄞州有限公司环球城分公司\",\"Taxrate\":0.1,\"DescriptionKey_R\":\"rental\",\"Invdate\":\"2019-02-15\",\"TaxAmount\":34336.95,\"sys_match_type_field\":0,\"sys_match_comment_field\":null},{\"sys_group_id_field\":25,\"payment_id\":15202,\"Custname\":\"斯特拉迪瓦里斯商业（上海）有限公司深圳滨河大道分公司\",\"LL_Name\":\"深圳市京基百纳商业管理有限公司京基百纳时代店\",\"Pmt_Rate\":0.05,\"DescriptionKey\":\"rental\",\"Paid_Date\":\"2019-02-15\",\"Amount\":21071.27,\"inv_id\":20885,\"Custname_R\":\"斯特拉迪瓦里斯商业（上海）有限公司深圳滨河大道分公司\",\"ClientName\":\"深圳市京基百纳商业管理有限公司京基百纳时代店\",\"Taxrate\":0.05,\"DescriptionKey_R\":\"rental\",\"Invdate\":\"2019-02-12\",\"TaxAmount\":21071.27,\"sys_match_type_field\":0,\"sys_match_comment_field\":null},{\"sys_group_id_field\":26,\"payment_id\":15373,\"Custname\":\"飒拉商业（上海）有限公司扬州京华城路分公司\",\"LL_Name\":\"扬州京国实业有限公司\",\"Pmt_Rate\":0.06,\"DescriptionKey\":\"Mgt\",\"Paid_Date\":\"2019-02-15\",\"Amount\":76538.75,\"inv_id\":20919,\"Custname_R\":\"飒拉商业（上海）有限公司扬州京华城路分公司\",\"ClientName\":\"扬州京国实业有限公司\",\"Taxrate\":0.06,\"DescriptionKey_R\":\"Mgt\",\"Invdate\":\"2019-02-15\",\"TaxAmount\":76538.75,\"sys_match_type_field\":0,\"sys_match_comment_field\":null},{\"sys_group_id_field\":27,\"payment_id\":15467,\"Custname\":\"飒拉商业（上海）有限公司扬州京华城路分公司\",\"LL_Name\":\"扬州京国实业有限公司\",\"Pmt_Rate\":0.05,\"DescriptionKey\":\"rental\",\"Paid_Date\":\"2019-02-15\",\"Amount\":145446.67,\"inv_id\":20918,\"Custname_R\":\"飒拉商业（上海）有限公司扬州京华城路分公司\",\"ClientName\":\"扬州京国实业有限公司\",\"Taxrate\":0.05,\"DescriptionKey_R\":\"rental\",\"Invdate\":\"2019-02-15\",\"TaxAmount\":145446.67,\"sys_match_type_field\":0,\"sys_match_comment_field\":null},{\"sys_group_id_field\":28,\"payment_id\":15479,\"Custname\":\"飒拉商业（上海）有限公司宁波天童南路分公司\",\"LL_Name\":\"银泰百货宁波鄞州有限公司环球城分公司\",\"Pmt_Rate\":0.1,\"DescriptionKey\":\"rental\",\"Paid_Date\":\"2019-02-15\",\"Amount\":116810.59,\"inv_id\":20809,\"Custname_R\":\"飒拉商业（上海）有限公司宁波天童南路分公司\",\"ClientName\":\"银泰百货宁波鄞州有限公司环球城分公司\",\"Taxrate\":0.1,\"DescriptionKey_R\":\"rental\",\"Invdate\":\"2019-02-15\",\"TaxAmount\":116810.59,\"sys_match_type_field\":0,\"sys_match_comment_field\":null},{\"sys_group_id_field\":29,\"payment_id\":15484,\"Custname\":\"飒拉商业（上海）有限公司重庆时代天街分公司\",\"LL_Name\":\"重庆龙湖成恒地产开发有限公司\",\"Pmt_Rate\":0.05,\"DescriptionKey\":\"rental\",\"Paid_Date\":\"2019-02-15\",\"Amount\":196900.68,\"inv_id\":20770,\"Custname_R\":\"飒拉商业（上海）有限公司重庆时代天街分公司\",\"ClientName\":\"重庆龙湖成恒地产开发有限公司\",\"Taxrate\":0.05,\"DescriptionKey_R\":\"rental\",\"Invdate\":\"2019-02-13\",\"TaxAmount\":196900.68,\"sys_match_type_field\":0,\"sys_match_comment_field\":null},{\"sys_group_id_field\":30,\"payment_id\":15524,\"Custname\":\"飒拉商业（上海）有限公司常州通江中路分公司\",\"LL_Name\":\"常州新北万达广场投资有限公司\",\"Pmt_Rate\":0.05,\"DescriptionKey\":\"rental\",\"Paid_Date\":\"2019-02-15\",\"Amount\":167292.39,\"inv_id\":20882,\"Custname_R\":\"飒拉商业（上海）有限公司常州通江中路分公司\",\"ClientName\":\"常州新北万达广场投资有限公司\",\"Taxrate\":0.05,\"DescriptionKey_R\":\"rental\",\"Invdate\":\"2019-02-15\",\"TaxAmount\":167292.39,\"sys_match_type_field\":0,\"sys_match_comment_field\":null},{\"sys_group_id_field\":31,\"payment_id\":15543,\"Custname\":\"飒拉商业（上海）有限公司长沙枫林三路分公司\",\"LL_Name\":\"步步高商业连锁股份有限公司梅溪湖分公司\",\"Pmt_Rate\":0.05,\"DescriptionKey\":\"rental\",\"Paid_Date\":\"2019-02-15\",\"Amount\":97037.67,\"inv_id\":20786,\"Custname_R\":\"飒拉商业（上海）有限公司长沙枫林三路分公司\",\"ClientName\":\"步步高商业连锁股份有限公司梅溪湖分公司\",\"Taxrate\":0.05,\"DescriptionKey_R\":\"rental\",\"Invdate\":\"2019-02-16\",\"TaxAmount\":97037.67,\"sys_match_type_field\":0,\"sys_match_comment_field\":null},{\"sys_group_id_field\":32,\"payment_id\":15594,\"Custname\":\"波丝可商业（北京）有限公司深圳滨河大道分公司\",\"LL_Name\":\"深圳市京基百纳商业管理有限公司京基百纳时代店\",\"Pmt_Rate\":0.16,\"DescriptionKey\":\"elec\",\"Paid_Date\":\"2019-02-15\",\"Amount\":11257.5,\"inv_id\":20889,\"Custname_R\":\"波丝可商业（北京）有限公司深圳滨河大道分公司\",\"ClientName\":\"深圳市京基百纳商业管理有限公司京基百纳时代店\",\"Taxrate\":0.16,\"DescriptionKey_R\":\"elec\",\"Invdate\":\"2019-02-12\",\"TaxAmount\":11257.5,\"sys_match_type_field\":0,\"sys_match_comment_field\":null},{\"sys_group_id_field\":33,\"payment_id\":15679,\"Custname\":\"奥依修商贸（上海）有限公司重庆时代天街分公司\",\"LL_Name\":\"重庆龙湖成恒地产开发有限公司\",\"Pmt_Rate\":0.16,\"DescriptionKey\":\"elec\",\"Paid_Date\":\"2019-02-15\",\"Amount\":81.8,\"inv_id\":20777,\"Custname_R\":\"奥依修商贸（上海）有限公司重庆时代天街分公司\",\"ClientName\":\"重庆龙湖成恒地产开发有限公司\",\"Taxrate\":0.16,\"DescriptionKey_R\":\"elec\",\"Invdate\":\"2019-02-13\",\"TaxAmount\":81.8,\"sys_match_type_field\":0,\"sys_match_comment_field\":null},{\"sys_group_id_field\":34,\"payment_id\":15723,\"Custname\":\"普安倍尔商业（北京）有限公司重庆时代天街分公司\",\"LL_Name\":\"重庆龙湖成恒地产开发有限公司\",\"Pmt_Rate\":0.16,\"DescriptionKey\":\"elec\",\"Paid_Date\":\"2019-02-15\",\"Amount\":9923.85,\"inv_id\":20773,\"Custname_R\":\"普安倍尔商业（北京）有限公司重庆时代天街分公司\",\"ClientName\":\"重庆龙湖成恒地产开发有限公司\",\"Taxrate\":0.16,\"DescriptionKey_R\":\"elec\",\"Invdate\":\"2019-02-13\",\"TaxAmount\":9923.85,\"sys_match_type_field\":0,\"sys_match_comment_field\":null},{\"sys_group_id_field\":35,\"payment_id\":15726,\"Custname\":\"普安倍尔商业（北京）有限公司宁波天童南路分公司\",\"LL_Name\":\"银泰百货宁波鄞州有限公司环球城分公司\",\"Pmt_Rate\":0.16,\"DescriptionKey\":\"elec\",\"Paid_Date\":\"2019-02-15\",\"Amount\":16984.91,\"inv_id\":20866,\"Custname_R\":\"普安倍尔商业（北京）有限公司宁波天童南路分公司\",\"ClientName\":\"银泰百货宁波鄞州有限公司环球城分公司\",\"Taxrate\":0.16,\"DescriptionKey_R\":\"elec\",\"Invdate\":\"2019-02-02\",\"TaxAmount\":16984.91,\"sys_match_type_field\":0,\"sys_match_comment_field\":null},{\"sys_group_id_field\":36,\"payment_id\":15847,\"Custname\":\"飒拉商业（上海）有限公司宁波天童南路分公司\",\"LL_Name\":\"银泰百货宁波鄞州有限公司环球城分公司\",\"Pmt_Rate\":0.16,\"DescriptionKey\":\"elec\",\"Paid_Date\":\"2019-02-15\",\"Amount\":52019.76,\"inv_id\":20867,\"Custname_R\":\"飒拉商业（上海）有限公司宁波天童南路分公司\",\"ClientName\":\"银泰百货宁波鄞州有限公司环球城分公司\",\"Taxrate\":0.16,\"DescriptionKey_R\":\"elec\",\"Invdate\":\"2019-02-02\",\"TaxAmount\":52019.76,\"sys_match_type_field\":0,\"sys_match_comment_field\":null}]";
		//重新map处理数字逻辑
		List<Map<String, Object>> maps = JsonUtil.toMapList(dataJson);
		//数字全部当前小数处理
		List<LinkedHashMap> linkedHashMaps = JsonUtil.fromListJson(dataJson, LinkedHashMap.class);
		List<Map<String, Object>> mapList = JacksonUtils.toMapList(dataJson);
		System.out.println("hashMaps:" + maps.size());
		for (Map hashMap : maps) {
			for (Object o : hashMap.entrySet()) {
				System.out.println(o);
			}
		}
	}


}