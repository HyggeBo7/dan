package top.dearbo.base.bean;

import top.dearbo.base.enums.ResultCodeEnum;

/**
 * @version 1.0
 * @author: Bo
 * @fileName: ExceptionHandlerService
 * @createDate: 2019-11-14 12:26.
 * @description: 异常处理
 */
public interface ExceptionHandlerService {

	/**
	 * 异常code
	 */
	Integer DEFAULT_SERVER_ERROR = ResultCodeEnum.SERVER_ERROR.getKey();

	Throwable getThrowable();

	Integer getCode();

	/**
	 * 获取错误信息
	 * @param ex Throwable
	 * @return String
	 */
	default String getErrorMsg(Throwable ex) {
		return ex.getLocalizedMessage() != null ? ex.getLocalizedMessage() : ex.getMessage();
	}

}
