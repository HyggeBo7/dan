package com.dan.web.common.springmvc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;

public class RequestLogInterceptor extends HandlerInterceptorAdapter {

    public static final Logger logger = LoggerFactory.getLogger(cn.xtits.xtf.web.springmvc.RequestLogInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        logger.info("uri={} para={}",new Object[]{ request.getRequestURI(), getStringRequestParam(request)});
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }

    private String getStringRequestParam(HttpServletRequest request) {
        Enumeration enumeration = request.getParameterNames();
        StringBuffer dataSb = new StringBuffer();
        while (enumeration.hasMoreElements()) {
            String key = (String) enumeration.nextElement();
            String data = request.getParameter(key);

            dataSb.append(key).append("=").append(data).append("&");
        }
        if (dataSb.length() > 0)
            dataSb.deleteCharAt(dataSb.length() - 1);

        return dataSb.toString();
    }

}
