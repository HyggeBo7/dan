package com.dan.utils.entity;

import com.dan.utils.JsonUtil;

/**
 * Created by Administrator on 2016/1/26.
 */
public class AjaxResultGeneric<T> extends BaseSerializable {

    public static final Integer SUCCESS = 1;
    public static final Integer PARAM_ERROR = -1;
    public static final Integer SERVER_ERROR = -500;

    private Integer code;
    private String msg;
    private T data;
    private Boolean sNulls = true;

    public AjaxResultGeneric() {
        code = SUCCESS;
    }

    public AjaxResultGeneric(T data) {
        this(SUCCESS, null, data);
    }

    public AjaxResultGeneric(T data, boolean serializeNulls) {
        this(SUCCESS, null, data, serializeNulls);
    }

    public AjaxResultGeneric(Integer code, String msg) {
        this(code, msg, null);
    }

    public AjaxResultGeneric(Integer code, String msg, T data) {
        this(code, msg, data, true);
    }

    public AjaxResultGeneric(Integer code, String msg, T data, boolean serializeNulls) {
        if (!serializeNulls) {
            if (null == msg) {
                msg = "";
            }
        }
        this.code = code;
        this.msg = msg;
        this.data = data;
        this.sNulls = serializeNulls;
    }

    @Override
    public String toString() {
        return "AjaxResultT{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                ", sNulls=" + sNulls +
                '}';
    }

    public String toJson() {
        return JsonUtil.toJson(this);
    }

    public String toDataJson() {
        return this.data == null ? "" : JsonUtil.toJson(this.data);
    }

    public Integer getCode() {
        return code;
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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Boolean getsNulls() {
        return sNulls;
    }
}
