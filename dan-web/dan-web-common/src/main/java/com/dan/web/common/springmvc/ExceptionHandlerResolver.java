package com.dan.web.common.springmvc;

import cn.xtits.xtf.common.exception.XTException;
import cn.xtits.xtf.web.springmvc.JsonCommonRender;
import com.dan.utils.entity.AjaxResult;
import com.dan.utils.exception.AppException;
import com.dan.web.common.exception.AppWebException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Enumeration;

/**
 * Created by Administrator on 2018/12/4.
 */
public class ExceptionHandlerResolver extends SimpleMappingExceptionResolver {
    public static final Logger logger = LoggerFactory.getLogger(com.dan.web.common.springmvc.ExceptionHandlerResolver.class);

    @Override
    public ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        if (ex != null) {
            logger.error("uri={} para={} trace={}", new Object[]{request.getRequestURI(), getStringRequestParam(request), ExceptionUtils.getStackTrace(ex)});
            AjaxResult ajaxResult = null;
            if (ex instanceof XTException) {
                XTException xtException = (XTException) ex;
                Integer code = xtException.getCode();
                String message = xtException.getMessage();
                ajaxResult = new AjaxResult(code, message, null);
            } else if (ex instanceof AppException) {
                AppException appException = (AppException) ex;
                Integer code = appException.getCode();
                String message = appException.getMessage();
                ajaxResult = new AjaxResult(code, message, null);
            } else if (ex instanceof AppWebException) {
                AppWebException appWebException = (AppWebException) ex;
                Integer code = appWebException.getCode();
                String message = appWebException.getMessage();
                ajaxResult = new AjaxResult(code, message, null);
            } else if (ex.getClass().getName().equals("org.apache.shiro.authz.UnauthorizedException")) {
                //Subject does not have permission [material:update]
                ajaxResult = new AjaxResult(AjaxResult.PARAM_ERROR, ex.getMessage().replace("Subject does not have permission", "没有") + "权限", null);
            } else {
                ajaxResult = new AjaxResult(AjaxResult.PARAM_ERROR, ExceptionUtils.getStackTrace(ex), null);
            }
            try {
                String contentType = StringUtils.isNotBlank(response.getContentType()) ? response.getContentType() : MediaType.APPLICATION_JSON_UTF8_VALUE;
                response.reset();
                response.addHeader("Access-Control-Allow-Origin", "*");
                response.setCharacterEncoding("UTF-8");
                //response.setHeader("Content-type", "application/json;charset=UTF-8"); 和设置setContentType相等
                response.setContentType(contentType);
                PrintWriter printWriter = response.getWriter();
                printWriter.write(JsonCommonRender.getJson(ajaxResult));
                printWriter.flush();
                printWriter.close();
            } catch (Exception e) {
                logger.error("resolveException eror , e : {}", e);
            }
        }
        return null;
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
