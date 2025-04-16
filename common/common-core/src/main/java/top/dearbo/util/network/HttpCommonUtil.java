package top.dearbo.util.network;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * url工具类
 *
 * @author wb
 * @date 2022-10-31 16:40.
 */
public class HttpCommonUtil {

	private static final Logger logger = LoggerFactory.getLogger(HttpCommonUtil.class);

	/**
	 * 默认编码
	 */
	public static final String DEFAULT_ENCODING = "UTF-8";
	/**
	 * Https请求
	 */
	public static final String HTTPS = "https";

	/**
	 * http
	 */
	public static final String HTTP = "http";
	public static final String METHOD_GET = "GET";
	public static final String METHOD_POST = "POST";
	public static final int DEFAULT_SUCCESS_CODE = 200;
	public static final String ACCEPT = "Accept";
	public static final String USER_AGENT = "User-Agent";
	public static final String ACCEPT_CONTENT_TYPE = "application/json, text/plain, */*";
	public static final String CONTENT_TYPE = "Content-Type";
	public static final String CONTENT_TYPE_1 = "content-type";
	public static final String CONTENT_TYPE_JSON = "application/json";

	private static final String FORM_DATA_CONTENT_TYPE = "application/x-www-form-urlencoded";

	public static SSLContext getSSLContext() {
		// 创建SSLContext对象，并使用我们指定的信任管理器初始化
		TrustManager[] tm = {new MyX509TrustManager()};
		SSLContext sslContext = null;
		try {
			sslContext = SSLContext.getInstance("SSL", "SunJSSE");
			sslContext.init(null, tm, new SecureRandom());
		} catch (NoSuchAlgorithmException | KeyManagementException | NoSuchProviderException e) {
			logger.error("sslContext https设置异常 error:", e);
		}
		return sslContext;
	}

	/**
	 * 对url参数进行获取，然后对值进行UTF-8编码返回
	 *
	 * @param url url
	 * @return
	 */
	public static String genUrlParamValue(String url) {
		return genUrlParamValue(url, DEFAULT_ENCODING);
	}

	/**
	 * 对url参数进行获取，然后对值进行指定编码返回
	 * <pre>
	 *     http://xxx.xxx.xxx?name=名称&code=name&age=18   ==> http://xxx.xxx.xxx?name=%E5%90%8D%E7%A7%B0&code=name&age=18
	 * </pre>
	 *
	 * @param url            url
	 * @param encoderCharset 编码字符
	 * @return String
	 */
	public static String genUrlParamValue(String url, String encoderCharset) {
		if (StringUtils.isNotBlank(url)) {
			String[] splitUrl = url.split("[?]");
			if (splitUrl.length > 1) {
				String[] splitParam = splitUrl[1].split("&");
				StringBuilder stringBuffer = new StringBuilder("?");
				for (String strParam : splitParam) {
					String[] splitParamData = strParam.split("=");
					if (splitParamData.length > 1) {
						stringBuffer.append(splitParamData[0]).append('=').append(urlEncoder(splitParamData[1], encoderCharset)).append('&');
					}
				}
				return splitUrl[0] + stringBuffer.substring(0, stringBuffer.length() - 1);
			}
		}
		return url;
	}

	/**
	 * url参数编码-默认对值进行UTF-8编码
	 *
	 * @param paramMap 参数
	 * @return
	 */
	public static String genUrlParam(Map<String, Object> paramMap) {
		return genUrlParam(paramMap, DEFAULT_ENCODING, false, true);
	}

	/**
	 * url参数编码
	 *
	 * @param paramMap       参数
	 * @param encoderCharset 字符编码
	 * @return String
	 */
	public static String genUrlParam(Map<String, Object> paramMap, String encoderCharset) {
		return genUrlParam(paramMap, encoderCharset, false, true);
	}

	/**
	 * 拼接Url
	 *
	 * @param paramMap       参数
	 * @param encoderCharset 编码字符
	 * @param keyEncoder     是否对key编码
	 * @param valueEncoder   是否对value编码
	 * @return String
	 */
	public static String genUrlParam(Map<String, Object> paramMap, String encoderCharset, boolean keyEncoder, boolean valueEncoder) {
		if (paramMap == null || paramMap.isEmpty()) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, Object> item : paramMap.entrySet()) {
			if (keyEncoder && StringUtils.isNotBlank(encoderCharset)) {
				sb.append(urlEncoder(item.getKey(), encoderCharset));
			} else {
				sb.append(item.getKey());
			}
			sb.append("=");
			if (valueEncoder && StringUtils.isNotBlank(encoderCharset)) {
				sb.append(urlEncoder(item.getValue().toString(), encoderCharset));
			} else {
				sb.append(item.getValue());
			}
			sb.append("&");
		}
		return sb.substring(0, sb.length() - 1);
	}

	/**
	 * 获取url参数-不对空值进行获取
	 *
	 * @param url url
	 * @return LinkedHashMap
	 */
	public static Map<String, Object> getUrlParam(String url) {
		return getUrlParam(url, false, null);
	}

	/**
	 * 获取url参数
	 * <pre>
	 *     ?name=名称&code=name&age=18
	 *     name=名称&code=name&sex=
	 *     http://xxx.xxx.xxx?name=名称&code=name&age=18
	 *     name=名称&code=name&age=18
	 * </pre>
	 *
	 * @param url            url地址
	 * @param emptyValueFlag 值为空是否获取
	 * @param emptyValue     emptyValueFlag=true,空值默认内容
	 * @return LinkedHashMap
	 */
	public static Map<String, Object> getUrlParam(String url, boolean emptyValueFlag, Object emptyValue) {
		if (StringUtils.isNotBlank(url)) {
			String strParamUrl = null;
			if (url.startsWith("?")) {
				strParamUrl = url.substring(1);
			}
			if (StringUtils.isBlank(strParamUrl) && url.contains("?")) {
				String[] splitUrl = url.split("[?]");
				if (splitUrl.length > 1) {
					strParamUrl = splitUrl[1];
				}
			}
			if (StringUtils.isBlank(strParamUrl) && url.contains("&")) {
				strParamUrl = url;
			}
			if (StringUtils.isNotBlank(strParamUrl)) {
				String[] splitParam = strParamUrl.split("&");
				Map<String, Object> paramMap = new LinkedHashMap<>(splitParam.length);
				for (String strParam : splitParam) {
					String[] splitParamData = strParam.split("=");
					if (splitParamData.length > 1) {
						paramMap.put(splitParamData[0], splitParamData[1]);
					} else if (emptyValueFlag) {
						paramMap.put(splitParamData[0], emptyValue);
					}
				}
				return paramMap;
			}
		}
		return new LinkedHashMap<>();
	}

	/**
	 * 字符进行编码
	 *
	 * @param str     字符串
	 * @param charset 编码
	 */
	public static String urlEncoder(String str, String charset) {
		if (StringUtils.isBlank(str)) {
			return "";
		}
		try {
			return URLEncoder.encode(str, charset);
		} catch (UnsupportedEncodingException e) {
			logger.error("value:【{}】 to charset:【{}】 error:【{}】", str, charset, e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 信任管理器
	 */
	public static class MyX509TrustManager implements X509TrustManager {

		/**
		 * 检查客户端证书
		 */
		@Override
		public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

		}

		/**
		 * 检查服务器端证书
		 */
		@Override
		public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

		}

		/**
		 * 返回受信任的X509证书数组
		 */
		@Override
		public X509Certificate[] getAcceptedIssuers() {
			//return new X509Certificate[0];
			return null;
		}
	}

}
