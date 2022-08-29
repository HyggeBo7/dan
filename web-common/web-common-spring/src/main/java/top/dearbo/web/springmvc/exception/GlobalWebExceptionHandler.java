package top.dearbo.web.springmvc.exception;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;
import top.dearbo.base.enums.ResultCodeEnum;
import top.dearbo.util.constant.AjaxResult;
import top.dearbo.web.core.util.WebServletUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.stream.Collectors;

/**
 * 统一异常处理
 */
@ControllerAdvice
public class GlobalWebExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalWebExceptionHandler.class);

    /**
     * 接口不存在
     *
     * @param e
     * @return
     */
    @ExceptionHandler({NoHandlerFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public AjaxResult noHandlerMapping(NoHandlerFoundException e) {
        logger.error("【GlobalWebExceptionHandler】noHandlerMapping===>uri={} trace={}", e.getRequestURL(), ExceptionUtils.getStackTrace(e));
        return AjaxResult.restResult(ResultCodeEnum.REQUEST_ILLEGAL, "请求的接口 " + e.getRequestURL() + " 未找到");
    }

    /**
     * 参数异常处理
     **/
    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseBody
    public AjaxResult handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        HttpServletRequest httpServletRequest = WebServletUtils.getHttpServletRequest();
        logger.error("【GlobalWebExceptionHandler】handleMethodArgumentNotValidException===>uri={} trace={}", httpServletRequest == null ? "" : httpServletRequest.getRequestURI(), ExceptionUtils.getStackTrace(e));
        AjaxResult result = AjaxResult.restResult(ResultCodeEnum.VALIDATE_PARAM_FAIL);
        if (e.getBindingResult().hasErrors() && !CollectionUtils.isEmpty(e.getBindingResult().getFieldErrors())) {
            result.setMsg(e.getBindingResult().getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .collect(Collectors.joining(",")));
        }
        return result;
    }
}