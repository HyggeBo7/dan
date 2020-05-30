package com.dan.web.common.exception;

import com.dan.util.enums.CommonStatusEnum;
import com.dan.util.exception.ExceptionHandlerService;
import com.netflix.hystrix.exception.HystrixBadRequestException;

import java.io.Serializable;

/**
 * @author bo
 * @date 2016/3/2
 */
public class AppHystrixWebException extends HystrixBadRequestException implements ExceptionHandlerService, Serializable {
    private static final long serialVersionUID = 3810214508904304724L;
    private Integer code;
    private Throwable t;

    public AppHystrixWebException(String msg) {
        this(CommonStatusEnum.FAIL.value, msg);
    }

    public AppHystrixWebException(Integer code, String msg) {
        super(msg);
        this.code = code;
    }

    public AppHystrixWebException(String msg, Throwable t) {
        this(CommonStatusEnum.FAIL.value, msg, t);
    }

    public AppHystrixWebException(Integer code, Throwable t) {
        this(code, t.getMessage(), t);
    }

    public AppHystrixWebException(Integer code, String msg, Throwable t) {
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

    public void setCode(Integer code) {
        this.code = code;
    }

    @Override
    public Throwable getThrowable() {
        return t;
    }

    public void setThrowable(Throwable t) {
        this.t = t;
    }

    public static void throwEx(String message) {
        throw new AppHystrixWebException(message);
    }

    public static void throwEx(Throwable e) {
        throw new AppHystrixWebException(e.getMessage(), e);
    }

    public static void throwEx(String message, Throwable e) {
        throw new AppHystrixWebException(message, e);
    }

    public static void throwEx(Integer code, String message, Throwable e) {
        throw new AppHystrixWebException(code, message, e);
    }
}
