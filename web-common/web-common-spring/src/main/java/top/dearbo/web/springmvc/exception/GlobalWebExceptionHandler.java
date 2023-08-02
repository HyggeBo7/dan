package top.dearbo.web.springmvc.exception;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;
import top.dearbo.base.enums.ResultCodeEnum;
import top.dearbo.util.constant.AjaxResult;
import top.dearbo.web.core.util.WebServletUtils;

import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 统一异常处理
 */
@ControllerAdvice
public class GlobalWebExceptionHandler {

	private static final Logger log = LoggerFactory.getLogger(GlobalWebExceptionHandler.class);

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
		log.warn("noHandlerMapping===>uri={} trace={}", e.getRequestURL(), ExceptionUtils.getStackTrace(e));
		return AjaxResult.restResult(ResultCodeEnum.REQUEST_ILLEGAL, "请求的接口 " + e.getRequestURL() + " 未找到");
	}

	/**
	 * 缺少参数
	 */
	@ExceptionHandler({MissingServletRequestParameterException.class})
	@ResponseBody
	public AjaxResult handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
		log.warn("handleMissingServletRequestParameterException===> trace={}", ExceptionUtils.getStackTrace(e));
		AjaxResult result = AjaxResult.restResult(ResultCodeEnum.VALIDATE_PARAM_FAIL);
		result.setMsg("请求参数缺少[" + e.getParameterName() + "]");
		return result;
	}

	/**
	 * 单参数校验失败
	 */
	/*@ExceptionHandler({ConstraintViolationException.class})
	@ResponseBody
	public AjaxResult handleConstraintViolationException(ConstraintViolationException e) {
		Set<ConstraintViolation<?>> set = e.getConstraintViolations();
		List<ConstraintViolation<?>> list = new ArrayList<>(set);
		log.warn("handleConstraintViolationException request params validate ex, {}", list.get(0).getMessage());
		AjaxResult result = AjaxResult.restResult(ResultCodeEnum.VALIDATE_PARAM_FAIL);
		result.setMsg(list.get(0).getMessage());
		return result;
	}*/

	/**
	 * 参数异常处理
	 **/
	@ExceptionHandler({MethodArgumentNotValidException.class})
	@ResponseBody
	public AjaxResult handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
		log.warn("handleMethodArgumentNotValidException===>uri={} trace={}", WebServletUtils.getHttpServletRequest().getRequestURI(), ExceptionUtils.getStackTrace(e));
		AjaxResult result = AjaxResult.restResult(ResultCodeEnum.VALIDATE_PARAM_FAIL);
		if (e.getBindingResult().hasErrors() && !CollectionUtils.isEmpty(e.getBindingResult().getFieldErrors())) {
			Optional.ofNullable(getBindResultMsg(e.getBindingResult())).ifPresent(result::setMsg);
		}
		return result;
	}

	/**
	 * validation Exception (以form-data形式传参)
	 */
	@ExceptionHandler({BindException.class})
	@ResponseBody
	public AjaxResult handleBindExceptionHandler(BindException e) {
		log.warn("handleBindExceptionHandler===>uri={} trace={}", WebServletUtils.getHttpServletRequest().getRequestURI(), ExceptionUtils.getStackTrace(e));
		AjaxResult result = AjaxResult.restResult(ResultCodeEnum.VALIDATE_PARAM_FAIL);
		Optional.ofNullable(getBindResultMsg(e.getBindingResult())).ifPresent(result::setMsg);
		return result;
	}

	private String getBindResultMsg(BindingResult bindingResult) {
		if (bindingResult != null && CollectionUtils.isEmpty(bindingResult.getFieldErrors())) {
			return bindingResult.getFieldErrors()
					.stream()
					.map(FieldError::getDefaultMessage)
					.collect(Collectors.joining(","));
		}
		return null;
	}
}