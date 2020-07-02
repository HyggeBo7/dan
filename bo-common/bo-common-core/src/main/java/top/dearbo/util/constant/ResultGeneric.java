package top.dearbo.util.constant;

import top.dearbo.util.data.JsonUtil;

/**
 * @version 1.0
 * @author: Bo
 * @fileName: ResultGeneric
 * @createDate: 2020-05-28 16:41.
 * @description: 通用的解析返回结果
 */
public class ResultGeneric<T> extends AbstractResult<T> {
    private static final long serialVersionUID = -8335931245613540709L;
    private Integer code;
    private String msg;
    private T data;

    private ResultGeneric() {

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
        this.data = data;
        this.code = code;
        this.msg = msg;
    }

    public ResultGeneric(int code, String msg, T data, Boolean serializeNull) {
        this(code, msg, data);
        this.serializeNull = serializeNull;
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

}
