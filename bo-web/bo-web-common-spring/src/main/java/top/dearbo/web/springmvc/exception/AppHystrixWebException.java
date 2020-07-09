package top.dearbo.web.springmvc.exception;

import com.netflix.hystrix.exception.HystrixBadRequestException;
import top.dearbo.base.bean.ExceptionHandlerService;

import java.io.Serializable;

/**
 * @author bo
 * @date 2016/3/2
 */
public class AppHystrixWebException extends HystrixBadRequestException implements ExceptionHandlerService, Serializable {
    private static final long serialVersionUID = -3470296198787284756L;
    private Integer code;
    private Throwable t;

    public AppHystrixWebException(String msg) {
        this(DEFAULT_SERVER_ERROR, msg);
    }

    public AppHystrixWebException(Integer code, String msg) {
        super(msg);
        this.code = code;
    }

    public AppHystrixWebException(String msg, Throwable t) {
        this(DEFAULT_SERVER_ERROR, msg, t);
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
