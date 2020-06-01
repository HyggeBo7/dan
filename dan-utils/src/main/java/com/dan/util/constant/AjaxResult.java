package com.dan.util.constant;

import com.dan.util.data.JsonUtil;
import com.dan.util.enums.CommonStatusEnum;
import com.dan.util.lang.ObjectUtil;

/**
 * @version 1.0
 * @author: Bo
 * @fileName: AjaxResult
 * @createDate: 2020-05-28 16:41.
 * @description: 返回结果, data为Object
 */
public class AjaxResult extends AbstractResult<Object> {
    private static final long serialVersionUID = 5364408400707486129L;
    private Integer code;
    private String msg;
    private Object data;
    private transient Boolean serializeNull;
    /**
     * 操作失败
     */
    private static final int NORMAL_ERROR = CommonStatusEnum.NORMAL_ERROR.value;

    public AjaxResult() {
        this(null);
    }

    public AjaxResult(Object data) {
        this(data, true);
    }

    public AjaxResult(Object data, boolean serializeNull) {
        this(SUCCESS_CODE, null, data, serializeNull);
    }

    public AjaxResult(int code, String msg) {
        this(code, msg, null);
    }

    public AjaxResult(int code, String msg, Object data) {
        this(code, msg, data, true);
    }

    public AjaxResult(int code, String msg, Object data, Boolean serializeNull) {
        this.data = data;
        this.code = code;
        this.msg = msg;
        this.serializeNull = ObjectUtil.booleanIsNotFalse(serializeNull);
    }

    public static AjaxResult ok() {
        return ok(null);
    }

    public static AjaxResult ok(Object data) {
        return ok(data, true);
    }

    public static AjaxResult ok(Object data, boolean serializeNull) {
        return ok(data, "success", serializeNull);
    }

    public static AjaxResult ok(Object data, String msg) {
        return ok(data, msg, true);
    }

    public static AjaxResult ok(Object data, String msg, boolean serializeNull) {
        return new AjaxResult(SUCCESS_CODE, msg, data, serializeNull);
    }

    public static AjaxResult failed(String msg) {
        return failed(null, msg, true);
    }

    public static AjaxResult failed(Object data) {
        return failed(data, true);
    }

    public static AjaxResult failed(Object data, boolean serializeNull) {
        return failed(data, "failed", serializeNull);
    }

    public static AjaxResult failed(Object data, String msg, boolean serializeNull) {
        return new AjaxResult(FAIL_CODE, msg, data, serializeNull);
    }

    /**
     * 用于修改、新增操作直接返回
     *
     * @param row 执行成功行数
     * @return code:大于0返回：1,否则0
     */
    public static AjaxResult update(int row) {
        return update(row, null);
    }

    public static AjaxResult update(int row, Object data) {
        return update(row, data, row > 0 ? "操作成功!" : "操作失败!");
    }

    public static AjaxResult update(int row, Object data, String successMsg, String errorMsg) {
        return update(row, data, row > 0 ? successMsg : errorMsg);
    }

    public static AjaxResult update(int row, Object data, String msg) {
        return new AjaxResult(row > 0 ? SUCCESS_CODE : NORMAL_ERROR, msg, data, true);
    }

    public static AjaxResult restResult(Object data, int code, String msg) {
        return restResult(data, code, msg, true);
    }

    public static AjaxResult restResult(Object data, int code, String msg, Boolean serializeNull) {
        return new AjaxResult(code, msg, data, serializeNull);
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
