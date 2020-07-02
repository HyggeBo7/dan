package top.dearbo.util.constant;

import top.dearbo.util.data.JsonUtil;
import top.dearbo.util.enums.CommonStatusEnum;

/**
 * @version 1.0
 * @author: Bo
 * @fileName: AjaxResult
 * @createDate: 2020-05-28 16:41.
 * @description: 返回结果, data为Object
 */
public class AjaxResult extends AbstractResult<Object> {
    private static final long serialVersionUID = -2436668057513117243L;
    private Integer code;
    private String msg;
    private Object data;
    /**
     * 操作失败
     */
    private static final int NORMAL_ERROR = CommonStatusEnum.NORMAL_ERROR.value;

    private AjaxResult() {
        //json反序列化时会执行当前构造函数,避免反序列化时会设置默认值
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
        this.data = data;
        this.code = code;
        this.msg = msg;
    }

    public AjaxResult(int code, String msg, Object data, Boolean serializeNull) {
        this(code, msg, data);
        this.serializeNull = serializeNull;
    }

    //=========成功=========

    public static AjaxResult ok() {
        return ok(null, false);
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

    //=========失败=========

    public static AjaxResult failed() {
        return failed("failed");
    }

    public static AjaxResult failed(String msg) {
        return failed(msg, null, false);
    }

    public static AjaxResult failed(String msg, Object data) {
        return failed(msg, data, true);
    }

    public static AjaxResult failed(String msg, Object data, boolean serializeNull) {
        return new AjaxResult(FAIL_CODE, msg, data, serializeNull);
    }

    //=========操作=========

    /**
     * 用于修改、新增操作直接返回
     *
     * @param row 执行成功行数
     * @return code:大于0返回：1,否则0
     */
    public static AjaxResult operate(int row) {
        return operate(row, null);
    }

    public static AjaxResult operate(int row, Object data) {
        return operate(row, data, row > 0 ? "操作成功!" : "操作失败!");
    }

    public static AjaxResult operate(int row, Object data, String successMsg, String errorMsg) {
        return operate(row, data, row > 0 ? successMsg : errorMsg);
    }

    public static AjaxResult operate(int row, Object data, String msg) {
        return new AjaxResult(row > 0 ? SUCCESS_CODE : NORMAL_ERROR, msg, data, true);
    }

    public static AjaxResult restResult(int code, String msg) {
        return restResult(code, msg, null, false);
    }

    public static AjaxResult restResult(int code, String msg, Object data) {
        return restResult(code, msg, data, true);
    }

    public static AjaxResult restResult(int code, String msg, Object data, Boolean serializeNull) {
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

    /*public String getMsg() {
        return msg;
    }*/

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

}
