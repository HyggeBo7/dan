package com.dan.web.common.springmvc;

import com.dan.util.constant.AjaxResult;
import com.dan.util.enums.CommonStatusEnum;
import com.dan.util.exception.ExceptionHandlerService;
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
 * @author Bo
 * @date 2018/12/4
 */
public class ExceptionHandlerResolver extends SimpleMappingExceptionResolver {
    private static final Logger logger = LoggerFactory.getLogger(ExceptionHandlerResolver.class);

    private final JsonCommonRender jsonCommonRender;

    public ExceptionHandlerResolver() {
        this(new JsonCommonRender());
    }

    public ExceptionHandlerResolver(JsonCommonRender jsonCommonRender) {
        this.jsonCommonRender = jsonCommonRender;
    }

    @Override
    public ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        if (ex != null) {
            logger.error("【ExceptionHandlerResolver】doResolveException===>uri={} para={} trace={}", request.getRequestURI(), HttpServletRequestUtils.getStringRequestParam(request), ExceptionUtils.getStackTrace(ex));
            AjaxResult<?> ajaxResult = customizeException(ex);
            if (ajaxResult == null) {
                if (ex instanceof ExceptionHandlerService) {
                    ExceptionHandlerService appException = (ExceptionHandlerService) ex;
                    Integer code = appException.getCode();
                    String message = appException.getExceptionMsg();
                    ajaxResult = new AjaxResult<Object>(code, message, null);
                } else if ("org.apache.shiro.authz.UnauthorizedException".equals(ex.getClass().getName())) {
                    //Subject does not have permission [material:update]
                    ajaxResult = new AjaxResult<Object>(CommonStatusEnum.NO_PERMISSION.value, ex.getMessage().replace("Subject does not have permission", "没有") + "权限", null);
                } else {
                    ajaxResult = new AjaxResult<Object>(CommonStatusEnum.FAIL.value, ExceptionUtils.getStackTrace(ex), null);
                }
            }

            try {
                String contentType = StringUtils.isNotBlank(response.getContentType()) ? response.getContentType() : MediaType.APPLICATION_JSON_UTF8_VALUE;
                //response.reset();
                //response.addHeader("Access-Control-Allow-Origin", "*");
                response.setCharacterEncoding("UTF-8");
                //response.setHeader("Content-type", "application/json;charset=UTF-8"); 和设置setContentType相等
                response.setContentType(contentType);
                PrintWriter printWriter = response.getWriter();
                printWriter.write(jsonCommonRender.getJson(ajaxResult));
                printWriter.flush();
                printWriter.close();
            } catch (Exception e) {
                logger.error("【ExceptionHandlerResolver】===>doResolveException-error, e : {}", e.getMessage());
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
    protected AjaxResult<?> customizeException(Exception ex) {

        return null;
    }

}
