package top.dearbo.web.springmvc.exception;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;
import top.dearbo.base.bean.ExceptionHandlerService;
import top.dearbo.base.enums.ResultCodeEnum;
import top.dearbo.util.constant.AjaxResult;
import top.dearbo.util.data.JacksonUtils;
import top.dearbo.web.core.util.WebServletUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 处理运行时异常、自定义异常返回结果
 *
 * @author wb
 * @date 2022/08/12 14:50:57.
 */
public class WebExceptionHandlerResolver extends SimpleMappingExceptionResolver {
	private static final Logger logger = LoggerFactory.getLogger(WebExceptionHandlerResolver.class);

	private static final Properties ERROR_MESSAGE_PROPS = new Properties();

	private static final Map<String, ResultCodeEnum> ERROR_RESULT_CODE_MAP = new HashMap<>();

	@Value("${global.config.exception.errorDetailEnabled:false}")
	private boolean errorDetailEnabled;

	@Value("${global.config.exception.logEnabled:true}")
	private boolean logEnabled;

	static {
		ERROR_RESULT_CODE_MAP.put("空指针异常", ResultCodeEnum.SERVER_ERROR);
		ERROR_RESULT_CODE_MAP.put("业务异常", ResultCodeEnum.BUSINESS_ERROR);
		ERROR_RESULT_CODE_MAP.put("用户未登录", ResultCodeEnum.LOGIN_FAIL);
		ERROR_RESULT_CODE_MAP.put("访问的资源未授权", ResultCodeEnum.NO_PERMISSION);
		ERROR_RESULT_CODE_MAP.put("认证异常", ResultCodeEnum.LOGIN_FAIL);
		ERROR_RESULT_CODE_MAP.put("方法的参数异常", ResultCodeEnum.PARAM_FAIL);
		ERROR_RESULT_CODE_MAP.put("数据库异常", ResultCodeEnum.SERVER_ERROR);
		ERROR_RESULT_CODE_MAP.put("Redis连接异常", ResultCodeEnum.SERVER_ERROR);
		ERROR_RESULT_CODE_MAP.put("资源访问异常", ResultCodeEnum.SERVER_ERROR);
		ERROR_RESULT_CODE_MAP.put("连接超时", ResultCodeEnum.SERVER_ERROR);

		ERROR_MESSAGE_PROPS.put("NullPointerException", "空指针异常");
		ERROR_MESSAGE_PROPS.put("FileNotFoundException", "访问的文件不存在");
		ERROR_MESSAGE_PROPS.put("ClassNotFoundException", "类文件未找到异常");
		ERROR_MESSAGE_PROPS.put("ArrayIndexOutOfBoundsException", "数组下标越界异常");

		//数据规范异常导致数据库访问异常
		ERROR_MESSAGE_PROPS.put("DataIntegrityViolationException", "数据库异常");
		//数据库资源连接失败
		ERROR_MESSAGE_PROPS.put("TransientDataAccessResourceException", "数据库异常");
		//数据库访问异常
		ERROR_MESSAGE_PROPS.put("DataAccessException", "数据库异常");
		//Redis连接异常
		ERROR_MESSAGE_PROPS.put("RedisConnectionFailureException", "Redis连接异常");

		//非法参数
		ERROR_MESSAGE_PROPS.put("IllegalArgumentException", "方法的参数异常");
		//方法参数类型不匹配异常
		//ERROR_MESSAGE_PROPS.put("MethodArgumentTypeMismatchException", "方法的参数异常");
		//gson、jackson
		ERROR_MESSAGE_PROPS.put("JsonParseException", "方法的参数异常");
		//fastjson、fastjson2
		ERROR_MESSAGE_PROPS.put("JSONException", "方法的参数异常");

		ERROR_MESSAGE_PROPS.put("ArithmeticException", "业务异常");

		ERROR_MESSAGE_PROPS.put("UnauthenticatedException", "用户未登录");
		ERROR_MESSAGE_PROPS.put("UnauthorizedException", "访问的资源未授权");
		ERROR_MESSAGE_PROPS.put("AuthorizationException", "访问的资源未授权");
		ERROR_MESSAGE_PROPS.put("HostUnauthorizedException", "访问的资源未授权");
		ERROR_MESSAGE_PROPS.put("AuthenticationException", "认证异常");
		ERROR_MESSAGE_PROPS.put("ResourceAccessException", "资源访问异常");
		ERROR_MESSAGE_PROPS.put("ConnectException", "连接超时");
		ERROR_MESSAGE_PROPS.put("RuntimeException", "运行时异常");
	}

	public WebExceptionHandlerResolver() {
		super.setExceptionMappings(ERROR_MESSAGE_PROPS);
	}

	@Override
	public ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
		String viewName = super.determineViewName(ex, request);
		AjaxResult ajaxResult = customizeException(ex);
		String stackTrace = ExceptionUtils.getStackTrace(ex);
		if (ajaxResult == null) {
			if (ex instanceof ExceptionHandlerService) {
				ExceptionHandlerService appException = (ExceptionHandlerService) ex;
				ajaxResult = new AjaxResult(appException.getCode(), appException.getErrorMsg(ex));
			} else {
				ResultCodeEnum resultCodeEnum = ERROR_RESULT_CODE_MAP.get(viewName);
				if (resultCodeEnum != null) {
					stackTrace = viewName + "：\n" + stackTrace;
					ajaxResult = AjaxResult.restResult(resultCodeEnum);
				} else {
					ajaxResult = new AjaxResult(ResultCodeEnum.SERVER_ERROR.getKey(), getErrorMsg(ex));
				}
			}
			//返回错误明细
			if (errorDetailEnabled) {
				ajaxResult.setData(stackTrace);
			}
		}
		if (logEnabled) {
			HandlerMethod hm = null;
			if (handler instanceof HandlerMethod) {
				hm = (HandlerMethod) handler;
			}
			if (WebServletUtils.isAjax(request, hm)) {
				logger.error("【ExceptionHandlerResolver-全局异常捕获ajax】===>Name={} uri={} para={} trace={}", viewName, request.getRequestURI(), WebServletUtils.toRequestParams(request), stackTrace);
			} else {
				logger.error("【ExceptionHandlerResolver-全局异常捕获非ajax】===>Name={} uri={} para={} trace={}", viewName, request.getRequestURI(), WebServletUtils.toRequestParams(request), stackTrace);
			}
		}
		MappingJackson2JsonView view = new MappingJackson2JsonView();
		view.setObjectMapper(JacksonUtils.getMapperNotNull());
		view.setContentType("text/json;charset=UTF-8");
		return new ModelAndView(view, resultMap(ajaxResult));
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

	public boolean isErrorDetailEnabled() {
		return errorDetailEnabled;
	}

	public void setErrorDetailEnabled(boolean errorDetailEnabled) {
		this.errorDetailEnabled = errorDetailEnabled;
	}

	private String getErrorMsg(Throwable ex) {
		return ex.getLocalizedMessage() != null ? ex.getLocalizedMessage() : ex.getMessage() != null ? ex.getMessage() : ExceptionUtils.getMessage(ex);
	}

	private Map<String, ?> resultMap(AjaxResult ajaxResult) {
		Map<String, Object> map = new HashMap<>(4);
		map.put("code", ajaxResult.getCode());
		map.put("msg", ajaxResult.getMsg());
		map.put("success", ajaxResult.resultSuccess());
		if (ajaxResult.getData() != null) {
			map.put("errorDetail", ajaxResult.getData());
		}
		return map;
	}

}
