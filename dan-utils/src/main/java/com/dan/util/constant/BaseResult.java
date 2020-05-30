package com.dan.util.constant;

import java.io.Serializable;

/**
 * @version 1.0
 * @author: Bo
 * @fileName: Result
 * @createDate: 2020-04-03 09:44.
 * @description: 通用的处理返回结果
 */
public abstract class BaseResult<T> implements Serializable {

    private static final long serialVersionUID = -5525734041446252217L;

    public abstract Integer getCode();

    public abstract void setCode(Integer code);

    public abstract String getMessage();

    public abstract void setMessage(String msg);

    public abstract T getData();

    public abstract void setData(T data);

    public abstract boolean isSuccess();
}
