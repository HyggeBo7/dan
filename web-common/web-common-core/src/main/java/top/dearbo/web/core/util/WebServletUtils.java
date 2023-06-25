package top.dearbo.web.core.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;

/**
 * web工具类
 *
 * @author wb
 * @date 2022/08/11 11:24:31.
 */
public class WebServletUtils extends org.springframework.web.util.WebUtils {

	public static HttpServletRequest getHttpServletRequest() {
		return ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
	}

	public static HttpServletResponse getHttpServletResponse() {
		return ((ServletRequestAttributes)Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getResponse();
	}

	public static boolean isAjax(HttpServletRequest request, HandlerMethod handlerMethod) {
		if (isAjax(request)) {
			return true;
		} else if (handlerMethod == null) {
			return false;
		} else {
			ResponseBody responseBody = (ResponseBody) handlerMethod.getMethodAnnotation(ResponseBody.class);
			return null != responseBody;
		}
	}

	public static boolean isAjax(HttpServletRequest request) {
		return request.getHeader("Content-Type") != null && request.getHeader("Content-Type").contains("application/json") || request.getHeader("accept") != null && request.getHeader("accept").contains("application/json") || request.getHeader("X-Requested-With") != null && request.getHeader("X-Requested-With").contains("XMLHttpRequest");
	}

	public static String getCookieValue(HttpServletRequest request, String name) {
		Cookie cookie = getCookie(request, name);
		return cookie != null ? cookie.getValue() : null;
	}

	public static void removeCookie(HttpServletResponse response, String key) {
		setCookie(response, key, (String) null, 0);
	}

	public static void setCookie(HttpServletResponse response, String name, String value, int maxAgeInSeconds) {
		Cookie cookie = new Cookie(name, value);
		cookie.setPath("/");
		cookie.setMaxAge(maxAgeInSeconds);
		cookie.setHttpOnly(true);
		response.addCookie(cookie);
	}

	/**
	 * 获取真实IP地址
	 *
	 * @param request request
	 * @return ip
	 */
	public static String getIpAddress(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		String[] headers = new String[]{"X-Forwarded-For", "X-Real-IP", "Proxy-Client-IP", "WL-Proxy-Client-IP", "HTTP_CLIENT_IP", "HTTP_X_FORWARDED_FOR"};
		for (String header : headers) {
			ip = request.getHeader(header);
			if (!checkIpEmptyOrUnknown(ip)) {
				break;
			}
		}
		if (checkIpEmptyOrUnknown(ip)) {
			ip = request.getRemoteAddr();
			if ("127.0.0.1".equals(ip) || "0:0:0:0:0:0:0:1".equals(ip)) {
				try {
					//根据网卡取本机配置的IP
					InetAddress inetAddress = InetAddress.getLocalHost();
					return inetAddress.getHostAddress();
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}
			}
		}
		// 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
		if (!checkIpEmptyOrUnknown(ip)) {
			int index = ip.indexOf(",");
			if (index > 0) {
				ip = ip.substring(0, index);
			}
		}
		return ip;
	}

	/**
	 * 请求参数转成String
	 *
	 * @param request HttpServletRequest
	 * @return String
	 */
	public static String toRequestParams(HttpServletRequest request) {
		return toRequestParams(request, null);
	}

	/**
	 * 请求参数转成String
	 *
	 * @param request        HttpServletRequest
	 * @param excludeKeyList 需要排除的key
	 * @return String
	 */
	public static String toRequestParams(HttpServletRequest request, List<String> excludeKeyList) {
		StringBuilder dataSb = new StringBuilder();
		Enumeration<String> enumeration = request.getParameterNames();
		int i = 0;
		while (enumeration.hasMoreElements()) {
			String key = enumeration.nextElement();
			if (excludeKeyList != null && excludeKeyList.contains(key)) {
				continue;
			}
			if (i > 0) {
				dataSb.append("&");
			} else {
				i++;
			}
			dataSb.append(key).append("=").append(request.getParameter(key));
		}
		return dataSb.toString();
	}

	public static String readReader(BufferedReader reader) throws IOException {
		if (reader != null) {
			StringBuilder requestBuilder = new StringBuilder();
			String inputStr;
			try {
				while ((inputStr = reader.readLine()) != null) {
					requestBuilder.append(inputStr);
				}
			} finally {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return requestBuilder.toString();
		}
		return null;
	}

	private static boolean checkIpEmptyOrUnknown(String ip) {
		return StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip);
	}
}
