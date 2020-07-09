package top.dearbo.base.bean;

import top.dearbo.base.enums.CommonStatusEnum;

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
    int DEFAULT_SERVER_ERROR = CommonStatusEnum.SERVER_ERROR.value;

    Throwable getThrowable();

    Integer getCode();

}
