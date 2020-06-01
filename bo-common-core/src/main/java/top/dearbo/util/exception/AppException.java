package top.dearbo.util.exception;

import top.dearbo.util.enums.CommonStatusEnum;

import java.io.Serializable;

/**
 * @author bo
 * @date 2016/3/2
 */
public class AppException extends RuntimeException implements ExceptionHandlerService, Serializable {
    private static final long serialVersionUID = -1107448809469267939L;
    private Integer code;
    private Throwable t;

    public AppException(String msg) {
        this(CommonStatusEnum.FAIL.value, msg);
    }

    public AppException(Integer code, String msg) {
        super(msg);
        this.code = code;
    }

    public AppException(Integer code, Throwable t) {
        super(t);
        this.code = code;
    }

    public AppException(String msg, Throwable t) {
        this(CommonStatusEnum.FAIL.value, msg, t);
    }

    public AppException(Integer code, String msg, Throwable t) {
        super(msg, t);
        this.code = code;
        this.t = t;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getExceptionMsg() {
        return getMessage();
    }

    @Override
    public Throwable getThrowable() {
        return t;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public void setThrowable(Throwable t) {
        this.t = t;
    }

    public static void throwEx(String message) {
        throw new AppException(message);
    }

    public static void throwEx(Throwable e) {
        throw new AppException(e.getMessage(), e);
    }

    public static void throwEx(String message, Throwable e) {
        throw new AppException(message, e);
    }

    public static void throwEx(Integer code, String message, Throwable e) {
        throw new AppException(code, message, e);
    }

}
