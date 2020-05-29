package com.dan.web.common.springmvc;

import com.dan.utils.exception.ExceptionHandlerService;
import com.dan.web.common.util.HttpServletRequestUtils;
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

/**
 * Created by Dan on 2018/12/4.
 */
public class ExceptionHandlerResolver extends SimpleMappingExceptionResolver {
    private static final Logger logger = LoggerFactory.getLogger(ExceptionHandlerResolver.class);

    private JsonCommonRender jsonCommonRender;

    public ExceptionHandlerResolver() {
        this(new JsonCommonRender());
    }

    public ExceptionHandlerResolver(JsonCommonRender jsonCommonRender) {
        this.jsonCommonRender = jsonCommonRender;
    }

    @Override
    public ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        if (ex != null) {
            logger.error("uri={} para={} trace={}", request.getRequestURI(), HttpServletRequestUtils.getStringRequestParam(request), ExceptionUtils.getStackTrace(ex));
            AjaxResult ajaxResult = customizeException(ex);
            /*else if (ex instanceof AppWebException) {
                AppWebException appWebException = (AppWebException) ex;
                Integer code = appWebException.getCode();
                String message = appWebException.getMessage();
                ajaxResult = new AjaxResult(code, message, null);
            }*/
            if (ajaxResult == null) {
                if (ex instanceof ExceptionHandlerService) {
                    ExceptionHandlerService appException = (ExceptionHandlerService) ex;
                    Integer code = appException.getCode();
                    String message = appException.getExceptionMsg();
                    ajaxResult = new AjaxResult(code, message, null);
                } else if (ex.getClass().getName().equals("org.apache.shiro.authz.UnauthorizedException")) {
                    //Subject does not have permission [material:update]
                    ajaxResult = new AjaxResult(AjaxResult.PARAM_ERROR, ex.getMessage().replace("Subject does not have permission", "没有") + "权限", null);
                } else {
                    ajaxResult = new AjaxResult(AjaxResult.PARAM_ERROR, ExceptionUtils.getStackTrace(ex), null);
                }
            }

            try {
                String contentType = StringUtils.isNotBlank(response.getContentType()) ? response.getContentType() : MediaType.APPLICATION_JSON_UTF8_VALUE;
                response.reset();
                response.addHeader("Access-Control-Allow-Origin", "*");
                response.setCharacterEncoding("UTF-8");
                //response.setHeader("Content-type", "application/json;charset=UTF-8"); 和设置setContentType相等
                response.setContentType(contentType);
                PrintWriter printWriter = response.getWriter();
                printWriter.write(jsonCommonRender.getJson(ajaxResult));
                printWriter.flush();
                printWriter.close();
            } catch (Exception e) {
                logger.error("resolveException eror , e : {}", e);
            }
        }
        return null;
    }


    /**
     * 方便处理其他自定义异常
     *
     * @param ex 异常
     * @return AjaxResult, 返回null, 使用默认处理
     */
    protected AjaxResult customizeException(Exception ex) {

        return null;
    }

}
