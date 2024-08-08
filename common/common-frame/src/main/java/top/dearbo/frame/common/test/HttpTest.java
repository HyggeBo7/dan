package top.dearbo.frame.common.test;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.junit.Test;
import top.dearbo.frame.common.test.entity.UserInfo;
import top.dearbo.util.constant.AjaxResult;
import top.dearbo.util.constant.ResultGeneric;
import top.dearbo.util.data.JacksonUtils;
import top.dearbo.util.data.JsonUtil;
import top.dearbo.util.network.HttpClientPoolUtil;
import top.dearbo.util.network.HttpUtils;
import top.dearbo.util.xt.PageResult;

import java.io.File;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.*;

/**
 * @author wb
 * @date 2021-06-09 20:43.
 */
public class HttpTest {

	@Test
	public void testHttp404() {
		String url = "https://ccc3.ddn2024ddb6666.com/cdn2024/202407/20/669a56e57327970b087f6d92/f4a4ff/index.m3u8";
		Map<String, String> headerMap = new HashMap<>(2);
		headerMap.put("Host", "861.cdn-vod.huaweicloud.com");
		headerMap.put("Referer", "http://www.mayikt.com/");
		//HttpClientPoolUtil.ResultResponse resultResponse = HttpClientPoolUtil.createRequest().doGet(url, null, headerMap);
		//System.out.println(resultResponse);
		HttpUtils.ResultResponse resultResponse1 = HttpUtils.createRequest().doGet(url, null, headerMap);
		System.out.println(resultResponse1);
	}

	@Test
	public void testHttpFileUploadAndParam() {
		String url = "http://localhost:8010/test/testUploadAndParam";
		//String url = "https://xfyl.dearbo.top/bo-api-xfylw/test/testUploadAndParam";
		Map<String, File> fileMap = new HashMap<>();
		fileMap.put("file", new File("F:/DownVideo/1.jpg"));
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("stringValue", "789张三zhan san");
		paramMap.put("booleanValue", true);
		paramMap.put("intValue", 888);
		HttpClientPoolUtil.ResultResponse resultResponse = HttpClientPoolUtil.createRequest().doPostFile(url, paramMap, null, fileMap);
		System.out.println(resultResponse);
	}

	@Test
	public void testHttpFileUpload() {
		String url = "http://47.96.5.131:8600/file-center/file/upload";
		Map<String, String> headerMap = new HashMap<>();
		headerMap.put("Authorization", "Bearer__b5d683343c607a4439acb84b00453f3a");
		Map<String, File> fileMap = new HashMap<>();
		fileMap.put("file", new File("F:/DownVideo/1.jpg"));
		HttpClientPoolUtil.ResultResponse resultResponse = HttpClientPoolUtil.createRequest().doPostFile(url, null, headerMap, fileMap);
		System.out.println(resultResponse);
	}

	@Test
	public void testHttp123() {
		Map<String, String> headerParam = new HashMap<String, String>();
		headerParam.put("Authorization", "Bearer X4eTEiMr-n2WL9U1PByTPHxSoIBU9sQ-APVZ3hH1Z7G3YJgC36ktE2z5HViagUzLtnmcqzf0w");
		Map<Object, Object> paramVO = new HashMap<Object, Object>();
		paramVO.put("option", "None");
		paramVO.put("empStatus", Arrays.asList(2, 3));
		paramVO.put("employType", null);
		paramVO.put("serviceType", Arrays.asList(0, 1, 4, 5));
		paramVO.put("approvalStatus", Arrays.asList(4));
		paramVO.put("oIds", Arrays.asList(604051848));
		paramVO.put("columns", Arrays.asList("UserID", "JobNumber"));
		paramVO.put("isWithDeleted", false);
		paramVO.put("enableTranslate", false);
		String url = "https://openapi.italent.cn/TenantBaseExternal/api/v5/Employee/GetServiceInfoByIds";
		HttpClientPoolUtil.ResultResponse resultResponse1 = HttpClientPoolUtil.createRequest().doPostJson(url, JsonUtil.toJson(paramVO), headerParam);
		HttpUtils.ResultResponse resultResponse = HttpUtils.createRequest().doPostJson(url, JsonUtil.toJson(paramVO), headerParam);
		System.out.println(resultResponse1.toString());
		System.out.println(resultResponse.toString());
	}


	@Test
	public void testHttpImg302() {
		String url = "http://pic.tsmp4.net/api/erciyuan/img.php";
		HttpUtils.ResultResponse resultResponse = HttpUtils.createRequest().doGet(url);
		List<String> stringList = resultResponse.getHeaders().get("Location");
		System.out.println(stringList.get(0));
		System.out.println("resultResponse:" + resultResponse.toString());
	}

	@Test
	public void testHttpClientCookie() {
		String url = "https://fa.jd.com/loadFa_toJson.js?aid=0_0_8000-0_0_8001-0_0_8002&_=1623329031028";
		Map<String, String> h = new HashMap<>();
		h.put("Cookie", "shshshfpa=a5102eb1-98fe-ab0f-1ecd-853dd0a44ef3-1621871059; __jdv=122270672%7Cdirect%7C-%7Cnone%7C-%7C1623250096310; shshshfpb=rFIzD0QpAhR7XRKrOQLti%20g%3D%3D; __jda=76161171.16218706041331978186930.1621870604.1623250096.1623328937.13; __jdc=76161171; __jdu=16218706041331978186930; areaId=15; ipLoc-djd=15-1213-49711-0; __jdb=76161171.2.16218706041331978186930|13.1623328937; shshshfp=6b8805efa4194d28da3c5ae440ae38cb; shshshsID=8bc7e67f43da1bb25bd2eb07ca23481a_2_1623328941321; 3AB9D23F7A4B3C9B=RCBAWNZ6GJSVA56R7PP6GGZ2XRZRZB3V3LYO5X3EQ5WQULTTFEYRHSZAGPBQNH56HCG37HOG2JJQLVAKEZC2MH2JT4");
		HttpClientPoolUtil.ResultResponse resultResponse = HttpClientPoolUtil.createRequest().doGet(url, null, h);
		Map<String, Header> headers = resultResponse.getHeaders();
		System.out.println("resultResponse:" + resultResponse.toString());
	}

	@Test
	public void testHttpProxy() {
		//183.128.238.72
		Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("58.218.92.148", 4310));
		String url1 = "http://xfyl.wubo777.top:5555/sign-in-api/test/testIp";
		String url2 = "http://xfyl.wubo777.top/bo-api-abc/test/testIp";
		String url3 = "http://httpbin.org/get";
		HttpUtils httpUtils = HttpUtils.createRequest().setProxy(proxy);
		System.out.println("url1:" + httpUtils.doGet(url1).toString());
		System.out.println("url2:" + httpUtils.doGet(url2).toString());
		System.out.println("url3:" + httpUtils.doGet(url3).toString());
	}

	@Test
	public void testHttpCode() {
		Map<String, Object> paramMap = new HashMap<>(2);
		paramMap.put("data", "{\"makeClassName\":\"制一课\",\"autoFlag\":true,\"groupName\":\"1A2\",\"useDate\":\"2020-10-28 17:12:55\",\"moldsId\":4290,\"groupId\":1,\"useName\":\"test\",\"makeClassId\":1,\"useType\":\"生产订单\",\"outMoldFlag\":true}");
		paramMap.put("oauth", "whosyourdaddy");
		HttpUtils.ResultResponse resultResponse = HttpUtils.createRequest().doPost("http://192.168.2.202:8031/hc-imolds-api/moldsUse/insertMoldsUseToAutoBack", paramMap);
		System.out.println("resultResponse:" + resultResponse.toString());
	}

	@Test
	public void testHttp() {
		String url = "https://xfyl.wubo777.top/bo-api-abc/test/testJson";
		//String url = "https://mpapi.xtits.cn/ihaj-api/housing/listHousing";
		//String url = "https://api.weixin.qq.com/sns/jscode2session";
		String data = "{\"grant_type\":\"authorization_code\",\"appid\":\"wxc6f17db215ae62a0\",\"secret\":\"25dd7249f0675bfbf18ee06ff797c087\",\"js_code\":\"061TqdSc1rzeKx0eJYVc1j2YRc1TqdSI\"}";
		Map<String, Object> dataMap = JsonUtil.fromJson(data, LinkedHashMap.class);
		dataMap.put("oauth", "whosyourdaddy");
		LinkedHashMap jsonToBean = JacksonUtils.jsonToBean(data, LinkedHashMap.class);
		Map<String, Object> stringObjectMap = JsonUtil.toMap(data);
		String header = "{\"content-type\":\"application/json\"}";
		Map<String, String> headerMap = JsonUtil.fromJson(header, LinkedHashMap.class);

		HttpUtils.ResultResponse resultResponse = HttpUtils.createRequest().doGet(url, dataMap, headerMap);
		System.out.println("value:" + resultResponse.toString());
	}

	@Test
	public void testHttpListUser() throws JsonProcessingException {
		String url = "http://119.37.194.4:5555/xtp-api/user/listUserByRoleId?roleId=12&oauth=whosyourdaddy";
		HttpUtils.ResultResponse resultResponse = HttpUtils.createRequest().doGet(url);
		if (resultResponse.isSuccess()) {
			String data = resultResponse.getData();
			//System.out.println("value:" + data);
			AjaxResult ajaxResult = JsonUtil.fromJson(data, AjaxResult.class);
			ResultGeneric resultGeneric = JsonUtil.fromJson(data, ResultGeneric.class);
			AjaxResult ajaxResult1 = JacksonUtils.jsonToBean(data, AjaxResult.class);
			PageResult<UserInfo> pagination = JsonUtil.fromGenericJson(JsonUtil.toJson(ajaxResult.getData()), PageResult.class, UserInfo.class);
			List<UserInfo> userInfoList = pagination.getData();
			System.out.println("userInfoList:" + userInfoList);
		} else {
			System.out.println(resultResponse.toString());
		}
	}

	@Test
	public void getCookieByTicket() {
		String ticket = "AAEAMJuTZLWamQDXR1CMRIrz30-C9fLSksCLUOe0FC9jRbiT0N6oI6AMlWw4nfVH-CoMZQ";
		String url = "https://passport.jd.com/uc/qrCodeTicketValidation?t=" + ticket;
		Map<String, String> headMap = new LinkedHashMap<>();
		headMap.put("Referer", "https://passport.jd.com/new/login.aspx?ReturnUrl=https%3A%2F%2Fwww.jd.com%2F");
		headMap.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.88 Safari/537.36");
		HttpUtils httpUtils = HttpUtils.createRequest();
		HttpUtils.ResultResponse resultResponse = httpUtils.doGet(url, null, headMap);
		Map<String, String> cookies = resultResponse.getCookies();
		String cookiesToString = resultResponse.getCookiesToString();
		System.out.println("resultResponse:" + resultResponse);
	}

	@Test
	public void testHttpResultCharset() {
		String url = "https://passport.jd.com/user/petName/getUserInfoForMiniJd.action";
		String cookie = "_pst=xrbbqpgex3675;logining=1;_tp=dwyTbuyrCmkao%2BEKEiggUQ%3D%3D;ceshi3.com=000;unick=%E7%9C%9F%E7%9A%84%E7%9A%AE3236;pin=xrbbqpgex3675;pinId=RmH4QHwIU784Y9wgpeiSSA;thor=256B7799946A21E62714EB4D7E421D455012762C6E2CC1F108D6B489E61B73398F0B416646AAB25798AAA644846309A1D004126F6B374A8CC4EB2CA79A377E1C51B0FC1EDFCCFBB490C1CAE79865CEF9084067DBBE59C139F56E41C0AC7AE1CBCA1DD6F6BB99B560B8615411B1010D8F515AC97AC0273C6CEC198F9A0B10035669047EDE3309DE71D2BC7DB9D2E0A97B;TrackID=1M35N8Ka3I5PoPN9SpNpcR2cQaWCS0pzos8qGcW-IAQISVphrhDwOyp3B6_-5fwkUwwYNM4_VtuG6HMhJDP5Vo4FAIYiKq1gOvvRe8yt7gFk;DeviceSeq=4e9d18ac56ad4b91860265cb1aa03600;";
		String headerJson = "{\"Content-Type\":\"application/json\",\"Cookie\":\"" + cookie + "\",\"Referer\":\"https://wqs.jd.com/\"}";
		Map<String, String> headerMap = null;
		if (StringUtils.isNotBlank(headerJson)) {
			headerMap = JsonUtil.fromJson(headerJson, LinkedHashMap.class);
		}
		HttpUtils.ResultResponse resultResponse = HttpUtils.createRequest().doPost(url, null, headerMap);
		System.out.println(JSON.parseObject(resultResponse.getData()));
	}

	@Test
	public void testHttpClientResultCharset() {
		String url = "https://passport.jd.com/user/petName/getUserInfoForMiniJd.action";
		String cookie = "_pst=xrbbqpgex3675;logining=1;_tp=dwyTbuyrCmkao%2BEKEiggUQ%3D%3D;ceshi3.com=000;unick=%E7%9C%9F%E7%9A%84%E7%9A%AE3236;pin=xrbbqpgex3675;pinId=RmH4QHwIU784Y9wgpeiSSA;thor=256B7799946A21E62714EB4D7E421D455012762C6E2CC1F108D6B489E61B73398F0B416646AAB25798AAA644846309A1D004126F6B374A8CC4EB2CA79A377E1C51B0FC1EDFCCFBB490C1CAE79865CEF9084067DBBE59C139F56E41C0AC7AE1CBCA1DD6F6BB99B560B8615411B1010D8F515AC97AC0273C6CEC198F9A0B10035669047EDE3309DE71D2BC7DB9D2E0A97B;TrackID=1M35N8Ka3I5PoPN9SpNpcR2cQaWCS0pzos8qGcW-IAQISVphrhDwOyp3B6_-5fwkUwwYNM4_VtuG6HMhJDP5Vo4FAIYiKq1gOvvRe8yt7gFk;DeviceSeq=4e9d18ac56ad4b91860265cb1aa03600;";
		String headerJson = "{\"Content-Type\":\"application/json\",\"Cookie\":\"" + cookie + "\",\"Referer\":\"https://wqs.jd.com/\"}";
		Map<String, String> headerMap = null;
		if (StringUtils.isNotBlank(headerJson)) {
			headerMap = JsonUtil.fromJson(headerJson, LinkedHashMap.class);
		}
		HttpClientPoolUtil.ResultResponse resultResponse = HttpClientPoolUtil.createRequest().doPost(url, null, headerMap);
		System.out.println(JSON.parseObject(resultResponse.getData()));
	}

	@Test
	public void testHttpParam() {
		Map<String, Object> paramMap = new HashMap<>(4);
		paramMap.put("娃娃", "你好啊 aaa");
		paramMap.put("name", "你好啊 aaa");
		paramMap.put("startProduceDate", "2021-04-07 00:00:00");
		paramMap.put("endProduceDate", "2021-04-13 23:59:59");
		paramMap.put("workOrderNo", "%0901%");
		String url = "http://117.41.37.22:808/javso/plan/listProductPlanToDrawingNoByProduceDate.do";
		HttpUtils.ResultResponse response = HttpUtils.createRequest().doPost(url, paramMap);
		System.out.println(response.getData());
	}

	@Test
	public void testHttpClientParam() {
		Map<String, Object> paramMap = new HashMap<>(4);
		paramMap.put("娃娃", "你好啊 aaa");
		paramMap.put("name", "你好啊 aaa");
		paramMap.put("startProduceDate", "2021-04-07 00:00:00");
		paramMap.put("endProduceDate", "2021-04-13 23:59:59");
		paramMap.put("workOrderNo", "%0901%");
		String url = "http://117.41.37.22:808/javso/plan/listProductPlanToDrawingNoByProduceDate.do";
		HttpClientPoolUtil.ResultResponse response = HttpClientPoolUtil.createRequest().doPost(url, paramMap);
		System.out.println(response.getData());
	}


}
