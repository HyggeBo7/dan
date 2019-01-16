package com.dan.web.common.exception;

import com.netflix.hystrix.exception.HystrixBadRequestException;

import java.io.Serializable;

/**
 * Created by dan on 2016/3/2.
 */
public class AppWebException extends HystrixBadRequestException implements Serializable {
    private static final long serialVersionUID = -416273111872183366L;

    public static final Integer PARAM_ERROR = 400;
    public static final Integer COMMON_ERROR = -1;

    private Integer code;
    private Throwable t;

    public AppWebException(Integer code, String msg) {
        super(msg);
        this.code = code;
    }

    public AppWebException(String msg) {
        super(msg);
        this.code = COMMON_ERROR;
    }

    public AppWebException(Integer code, Throwable t) {
        super(t.getMessage(), t);
        this.code = code;
        this.t = t;
    }

    public AppWebException(Integer code, String msg, Throwable t) {
        super(msg, t);
        this.code = code;
        this.t = t;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Throwable getT() {
        return t;
    }

    public void setT(Throwable t) {
        this.t = t;
    }

    public static void throwEx(String message) {
        throw new AppWebException(COMMON_ERROR, message);
    }

    public static void throwEx(Throwable e) {
        throw new AppWebException(COMMON_ERROR, e);
    }
}
