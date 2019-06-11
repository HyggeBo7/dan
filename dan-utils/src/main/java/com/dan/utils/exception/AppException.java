package com.dan.utils.exception;

import java.io.Serializable;

/**
 * Created by dan on 2016/3/2.
 */
public class AppException extends RuntimeException implements Serializable {
    private static final long serialVersionUID = -416273111872183365L;

    public static final Integer PARAM_ERROR = 400;
    public static final Integer COMMON_ERROR = -1;

    private Integer code;
    private Throwable t;

    public AppException(Integer code, String msg) {
        super(msg);
        this.code = code;
    }

    public AppException(String msg) {
        super(msg);
        this.code = COMMON_ERROR;
    }

    public AppException(Integer code, String msg, Throwable t) {
        super(msg, t);
        this.code = code;
        this.t = t;
    }

    public AppException(Integer code, Throwable t) {
        super(t);
        this.code = code;
        this.t = t;
    }

    public AppException(String msg, Throwable t) {
        super(msg, t);
        this.code = COMMON_ERROR;
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
        throw new AppException(COMMON_ERROR, message);
    }

    public static void throwEx(Throwable e) {
        throw new AppException(COMMON_ERROR, e);
    }

    public static void throwEx(String message, Throwable e) {
        throw new AppException(message, e);
    }

}
