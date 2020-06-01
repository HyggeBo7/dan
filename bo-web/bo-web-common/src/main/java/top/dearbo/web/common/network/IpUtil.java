package top.dearbo.web.common.network;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;

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
     * @param request
     * @return
     */
    public static String getRealIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        return checkIp(ip) ? ip : (checkIp(ip = request.getHeader("Proxy-Client-IP")) ? ip : (checkIp(ip = request.getHeader("WL-Proxy-Client-IP")) ? ip : request.getRemoteAddr()));
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
}
