package top.dearbo.web.springmvc;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;
import top.dearbo.util.constant.AjaxResult;
import top.dearbo.util.enums.CommonStatusEnum;
import top.dearbo.util.exception.ExceptionHandlerService;
import top.dearbo.web.core.util.ServletUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Bo
 * @date 2018/12/4
 */
public class ExceptionHandlerResolver extends SimpleMappingExceptionResolver {
    private static final Logger logger = LoggerFactory.getLogger(ExceptionHandlerResolver.class);

    private final ModelAndView defaultModelAndView = new ModelAndView();

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
            logger.error("【ExceptionHandlerResolver】doResolveException===>uri={} para={} trace={}", request.getRequestURI(), ServletUtils.toRequestParams(request), ExceptionUtils.getStackTrace(ex));
            AjaxResult ajaxResult = customizeException(ex);
            if (ajaxResult == null) {
                if (ex instanceof ExceptionHandlerService) {
                    ExceptionHandlerService appException = (ExceptionHandlerService) ex;
                    ajaxResult = new AjaxResult(appException.getCode(), getErrorMsg(ex));
                } else if ("org.apache.shiro.authz.UnauthorizedException".equals(ex.getClass().getName())) {
                    //Subject does not have permission [material:update]
                    ajaxResult = new AjaxResult(CommonStatusEnum.NO_PERMISSION.value, ex.getMessage().replace("Subject does not have permission", "没有") + "权限", null);
                } else {
                    ajaxResult = new AjaxResult(CommonStatusEnum.FAIL.value, getErrorMsg(ex), ExceptionUtils.getStackTrace(ex));
                }
            }
            try {
                String contentType = StringUtils.isNotBlank(response.getContentType()) ? response.getContentType() : MediaType.APPLICATION_JSON_VALUE;
                //response.reset();
                //response.addHeader("Access-Control-Allow-Origin", "*");
                response.setCharacterEncoding("UTF-8");
                //response.setHeader("Content-type", "application/json;charset=UTF-8"); 和设置setContentType相等
                response.setContentType(contentType);
                String json = jsonCommonRender.getJson(ajaxResult);
                if (json != null) {
                    response.getWriter().write(json);
                }
                return getExceptionModelAndView(request, response, ex);
            } catch (Exception e) {
                logger.error("【ExceptionHandlerResolver】===>doResolveException-error, e : {}", getErrorMsg(e));
            }
        }
        return null;
    }

    private String getErrorMsg(Throwable ex) {
        return ex.getMessage() != null ? ex.getMessage() : ex.getLocalizedMessage() != null ? ex.getLocalizedMessage() : ExceptionUtils.getMessage(ex);
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

    /**
     * 异常的 ModelAndView
     *
     * @return ModelAndView
     */
    protected ModelAndView getExceptionModelAndView(HttpServletRequest request, HttpServletResponse response, Exception ex) {
        return defaultModelAndView;
    }

}
