package top.dearbo.util.exception;

/**
 * @version 1.0
 * @author: Bo
 * @fileName: ExceptionHandlerService
 * @createDate: 2019-11-14 12:26.
 * @description: 异常处理
 */
public interface ExceptionHandlerService {

    Throwable getThrowable();

    Integer getCode();

    String getExceptionMsg();

}
