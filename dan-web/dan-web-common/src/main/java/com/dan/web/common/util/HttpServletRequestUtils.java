package com.dan.web.common.util;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * @version 1.0
 * @author: Bo
 * @fileName: ServletRequestUtils
 * @createDate: 2019-07-03 16:05.
 * @description:
 */
public class HttpServletRequestUtils {

    public static String getStringRequestParam(HttpServletRequest request) {
        StringBuilder dataSb = new StringBuilder();
        Enumeration<String> enumeration = request.getParameterNames();
        while (enumeration.hasMoreElements()) {
            String key = enumeration.nextElement();
            String data = request.getParameter(key);

            dataSb.append(key).append("=").append(data).append("&");
        }
        if (dataSb.length() > 0) {
            dataSb.deleteCharAt(dataSb.length() - 1);
        }

        return dataSb.toString();
    }
}
