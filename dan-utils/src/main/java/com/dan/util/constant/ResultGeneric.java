package com.dan.util.constant;

import com.dan.util.data.JsonUtil;
import com.dan.util.lang.ObjectUtil;

/**
 * @version 1.0
 * @author: Bo
 * @fileName: ResultGeneric
 * @createDate: 2020-05-28 16:41.
 * @description: 通用的解析返回结果
 */
public class ResultGeneric<T> extends AbstractResult<T> {
    private static final long serialVersionUID = -5532590172221388351L;
    private Integer code;
    private String msg;
    private T data;
    private transient Boolean serializeNull;

    public ResultGeneric() {
        this(null);
    }

    public ResultGeneric(T data) {
        this(data, true);
    }

    public ResultGeneric(T data, boolean serializeNull) {
        this(SUCCESS_CODE, null, data, serializeNull);
    }

    public ResultGeneric(int code, String msg) {
        this(code, msg, null);
    }

    public ResultGeneric(int code, String msg, T data) {
        this(code, msg, data, true);
    }

    public ResultGeneric(int code, String msg, T data, Boolean serializeNull) {
        this.data = data;
        this.code = code;
        this.msg = msg;
        this.serializeNull = ObjectUtil.booleanIsNotFalse(serializeNull);
    }

    public String toJson() {
        return JsonUtil.toJson(this);
    }

    public String toDataJson() {
        return this.data == null ? null : JsonUtil.toJson(this.data);
    }

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return msg;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Boolean getSerializeNull() {
        return serializeNull;
    }

    public void setSerializeNull(Boolean serializeNull) {
        this.serializeNull = serializeNull;
    }

    @Override
    public boolean isSerializeNullField() {
        return serializeNull;
    }
}
