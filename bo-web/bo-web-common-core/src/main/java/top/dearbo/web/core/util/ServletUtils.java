package top.dearbo.web.core.util;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.List;

/**
 * @version 1.0
 * @author: Bo
 * @fileName: IpUtil
 * @createDate: 2019-06-11 10:43.
 * @description: 获取IP的工具类
 */
public class ServletUtils {

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

    private static boolean checkIpEmptyOrUnknown(String ip) {
        return StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip);
    }
}
