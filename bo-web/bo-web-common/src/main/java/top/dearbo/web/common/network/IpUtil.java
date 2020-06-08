package top.dearbo.web.common.network;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @version 1.0
 * @author: Bo
 * @fileName: IpUtil
 * @createDate: 2019-06-11 10:43.
 * @description: 获取IP的工具类
 */
public class IpUtil {
    /**
     * 获取真实IP
     *
     * @param request request
     * @return ip
     */
    @Deprecated
    public static String getRealIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        return checkIp(ip) ? ip : (checkIp(ip = request.getHeader("Proxy-Client-IP")) ? ip : (checkIp(ip = request.getHeader("WL-Proxy-Client-IP")) ? ip : request.getRemoteAddr()));
    }

    /**
     * 获取真实IP地址
     *
     * @param request request
     * @return ip
     */
    public static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (checkIpEmptyOrUnknown(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (checkIpEmptyOrUnknown(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (checkIpEmptyOrUnknown(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (checkIpEmptyOrUnknown(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
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
        return ip;
    }

    /**
     * 校验IP
     *
     * @param ip ip
     * @return true|false
     */
    private static boolean checkIp(String ip) {
        return !StringUtils.isEmpty(ip) && !"unknown".equalsIgnoreCase(ip);
    }

    private static boolean checkIpEmptyOrUnknown(String ip) {
        return StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip);
    }
}
