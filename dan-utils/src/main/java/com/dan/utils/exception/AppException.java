package com.dan.utils.exception;

import java.io.Serializable;

/**
 * @author dan
 * @date 2016/3/2
 */
public class AppException extends RuntimeException implements ExceptionHandlerService, Serializable {
    public static final Integer PARAM_ERROR = 400;
    public static final Integer COMMON_ERROR = -1;

    private Integer code;
    private Throwable t;

    public AppException(String msg) {
        this(COMMON_ERROR, msg);
    }

    public AppException(Integer code, String msg) {
        super(msg);
        this.code = code;
    }

    public AppException(Integer code, Throwable t) {
        super(t);
        this.code = code;
        this.t = t;
    }

    public AppException(String msg, Throwable t) {
        this(COMMON_ERROR, msg, t);
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
        throw new AppException(COMMON_ERROR, message);
    }

    public static void throwEx(Throwable e) {
        throw new AppException(COMMON_ERROR, e);
    }

    public static void throwEx(String message, Throwable e) {
        throw new AppException(message, e);
    }

    public static void throwEx(Integer code, String message, Throwable e) {
        throw new AppException(code, message, e);
    }

}
