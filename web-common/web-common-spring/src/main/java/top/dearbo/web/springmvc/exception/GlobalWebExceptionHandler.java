package top.dearbo.web.springmvc.exception;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import top.dearbo.base.enums.ResultCodeEnum;
import top.dearbo.util.constant.AjaxResult;
import top.dearbo.web.core.util.WebServletUtils;

import javax.servlet.http.HttpServletRequest;
import java.nio.file.AccessDeniedException;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 统一异常处理
 */
@ControllerAdvice
public class GlobalWebExceptionHandler {

	private static final Logger log = LoggerFactory.getLogger(GlobalWebExceptionHandler.class);

	/**
	 * 权限校验异常
	 */
	@ExceptionHandler(AccessDeniedException.class)
	@ResponseBody
	public AjaxResult handleAccessDeniedException(AccessDeniedException e, HttpServletRequest request) {
		String requestURI = request.getRequestURI();
		log.error("请求地址'{}',权限校验失败'{}'", requestURI, e.getMessage());
		return AjaxResult.restResult(ResultCodeEnum.NO_PERMISSION);
	}

	/**
	 * 请求方式不支持
	 */
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	@ResponseBody
	public AjaxResult handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
		String requestURI = request.getRequestURI();
		log.error("请求地址'{}',不支持'{}'请求", requestURI, e.getMethod());
		return AjaxResult.failed(e.getMessage());
	}

	/**
	 * 接口不存在
	 */
	@ExceptionHandler({NoHandlerFoundException.class})
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ResponseBody
	public AjaxResult noHandlerMapping(NoHandlerFoundException e) {
		log.error("请求地址不存在===>uri={} trace={}", e.getRequestURL(), ExceptionUtils.getStackTrace(e));
		return AjaxResult.restResult(ResultCodeEnum.REQUEST_ILLEGAL, "请求的接口 " + e.getRequestURL() + " 未找到");
	}

	/**
	 * 请求参数类型不匹配
	 */
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	@ResponseBody
	public AjaxResult handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e, HttpServletRequest request) {
		String requestURI = request.getRequestURI();
		String value = e.getValue() != null ? e.getValue().toString() : null;
		log.error("请求参数类型不匹配'{}',发生系统异常.", requestURI, e);
		return AjaxResult.restResult(ResultCodeEnum.PARAM_FAIL.getCode(), String.format("请求参数类型不匹配，参数[%s]要求类型为：'%s'，但输入值为：'%s'", e.getName(), e.getRequiredType() == null ? null : e.getRequiredType().getName(), value));
	}

	/**
	 * 缺少参数
	 */
	@ExceptionHandler({MissingServletRequestParameterException.class})
	@ResponseBody
	public AjaxResult handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
		log.error("缺少参数===> trace={}", ExceptionUtils.getStackTrace(e));
		return AjaxResult.restResult(ResultCodeEnum.PARAM_FAIL.getCode(), "请求参数缺少[" + e.getParameterName() + "]");
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
	 * 请求路径中缺少必需的路径变量
	 */
	@ExceptionHandler(MissingPathVariableException.class)
	@ResponseBody
	public AjaxResult handleMissingPathVariableException(MissingPathVariableException e, HttpServletRequest request) {
		String requestURI = request.getRequestURI();
		log.error("请求路径中缺少必需的路径变量'{}',发生系统异常.", requestURI, e);
		return AjaxResult.restResult(ResultCodeEnum.PARAM_FAIL.getCode(), String.format("请求路径中缺少必需的路径变量[%s]", e.getVariableName()));
	}

	/**
	 * validation Exception (以form-data形式传参)
	 */
	@ExceptionHandler({BindException.class})
	@ResponseBody
	public AjaxResult handleBindExceptionHandler(BindException e) {
		log.error("handleBindExceptionHandler===>uri={} trace={}", WebServletUtils.getHttpServletRequest().getRequestURI(), ExceptionUtils.getStackTrace(e));
		AjaxResult result = AjaxResult.restResult(ResultCodeEnum.PARAM_FAIL);
		Optional.ofNullable(getBindResultMsg(e.getBindingResult())).ifPresent(result::setMsg);
		return result;
	}

	/**
	 * 参数异常处理
	 **/
	@ExceptionHandler({MethodArgumentNotValidException.class})
	@ResponseBody
	public AjaxResult handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
		log.error("参数异常===>uri={} trace={}", WebServletUtils.getHttpServletRequest().getRequestURI(), ExceptionUtils.getStackTrace(e));
		AjaxResult result = AjaxResult.restResult(ResultCodeEnum.VALIDATE_PARAM_FAIL);
		if (e.getBindingResult().hasErrors() && !CollectionUtils.isEmpty(e.getBindingResult().getFieldErrors())) {
			Optional.ofNullable(getBindResultMsg(e.getBindingResult())).ifPresent(result::setMsg);
		}
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