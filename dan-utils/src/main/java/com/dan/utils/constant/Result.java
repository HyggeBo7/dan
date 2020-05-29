package com.dan.utils.constant;

import com.dan.utils.JsonUtil;
import com.dan.utils.enums.CommonStatusEnum;
import com.dan.utils.lang.ObjectUtil;

/**
 * @version 1.0
 * @author: Bo
 * @fileName: Result
 * @createDate: 2020-05-28 16:41.
 * @description: 返回结果
 */
public class Result<T> implements BaseSerializable {
    private Integer code;
    private String msg;
    private Object data;
    private transient Boolean serializeNull;

    public Result(T data, int code, String msg, Boolean serializeNull) {
        this.data = data;
        this.code = code;
        this.msg = msg;
        this.serializeNull = ObjectUtil.booleanIsNotFalse(serializeNull);
    }

    public static <T> Result<T> ok() {
        return ok(null);
    }

    public static <T> Result<T> ok(T data) {
        return ok(data, true);
    }

    public static <T> Result<T> ok(T data, boolean serializeNull) {
        return ok(data, "success", serializeNull);
    }

    public static <T> Result<T> ok(T data, String msg) {
        return ok(data, msg, true);
    }

    public static <T> Result<T> ok(T data, String msg, boolean serializeNull) {
        return restResult(data, CommonStatusEnum.SUCCESS.value, msg, serializeNull);
    }

    public static <T> Result<T> failed(String msg) {
        return failed(null, msg, true);
    }

    public static <T> Result<T> failed(T data) {
        return failed(data, true);
    }

    public static <T> Result<T> failed(T data, boolean serializeNull) {
        return failed(data, "failed", serializeNull);
    }

    public static <T> Result<T> failed(T data, String msg, boolean serializeNull) {
        return restResult(data, CommonStatusEnum.FAIL.value, msg, serializeNull);
    }

    /**
     * 用于修改、新增操作直接返回
     *
     * @param row 执行成功行数
     * @param <T> data
     * @return code:大于0返回：1,否则0
     */
    public static <T> Result<T> update(int row) {
        return update(row, null);
    }

    public static <T> Result<T> update(int row, T data) {
        return update(row, data, row > 0 ? "操作成功!" : "操作失败!");
    }

    public static <T> Result<T> update(int row, T data, String successMsg, String errorMsg) {
        return update(row, data, row > 0 ? successMsg : errorMsg);
    }

    public static <T> Result<T> update(int row, T data, String msg) {
        return new Result<>(data, row > 0 ? CommonStatusEnum.SUCCESS.value : CommonStatusEnum.NORMAL_ERROR.value, msg, true);
    }

    public static <T> Result<T> restResult(T data, int code, String msg) {
        return restResult(data, code, msg, true);
    }

    public static <T> Result<T> restResult(T data, int code, String msg, Boolean serializeNull) {
        return new Result<>(data, code, msg, serializeNull);
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

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
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
