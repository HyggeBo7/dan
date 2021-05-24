package top.dearbo.util.constant;

import top.dearbo.base.bean.AbstractResult;
import top.dearbo.base.enums.CommonStatusEnum;
import top.dearbo.util.data.JsonUtil;

/**
 * @version 1.0
 * @author: Bo
 * @fileName: AjaxResult
 * @createDate: 2020-05-28 16:41.
 * @description: 返回结果, data为Object
 */
public class AjaxResult extends AbstractResult<Object> {
    private static final long serialVersionUID = -8459064716012212751L;
    private Integer code;
    private String msg;
    private Object data;
    /**
     * 操作失败
     */
    private static final int NORMAL_ERROR = CommonStatusEnum.NORMAL_ERROR.getValue();

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
        return ok(null, true);
    }

    public static AjaxResult ok(Object data) {
        return ok(data, true);
    }

    public static AjaxResult ok(Object data, boolean serializeNull) {
        return ok(data, "成功", serializeNull);
    }

    public static AjaxResult ok(Object data, String msg) {
        return ok(data, msg, true);
    }

    public static AjaxResult ok(Object data, String msg, boolean serializeNull) {
        return new AjaxResult(SUCCESS_CODE, msg, data, serializeNull);
    }

    //=========失败=========

    public static AjaxResult failed() {
        return failed("失败");
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
        return operate(row, data, true);
    }

    public static AjaxResult operate(int row, Object data, Boolean serializeNull) {
        return operate(row, data, "操作成功!", "操作失败!", serializeNull);
    }

    public static AjaxResult operate(int row, Object data, String successMsg, String errorMsg) {
        return operate(row, data, successMsg, errorMsg, true);
    }

    public static AjaxResult operate(int row, String successMsg, String errorMsg) {
        return operate(row, null, successMsg, errorMsg, false);
    }

    public static AjaxResult operate(int row, Object data, String successMsg, String errorMsg, Boolean serializeNull) {
        boolean success = row > 0;
        return new AjaxResult(success ? SUCCESS_CODE : NORMAL_ERROR, success ? successMsg : errorMsg, data, serializeNull);
    }

    //===========自定义操作===========

    public static AjaxResult restResult(CommonStatusEnum commonStatusEnum) {
        return restResult(commonStatusEnum.getValue(), commonStatusEnum.getMsg(), null, false);
    }

    public static AjaxResult restResult(CommonStatusEnum commonStatusEnum, Object data) {
        return restResult(commonStatusEnum.getValue(), commonStatusEnum.getMsg(), data, true);
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

    @Override
    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public Integer resultCode() {
        return code;
    }

    @Override
    public String resultMessage() {
        return msg;
    }
}
