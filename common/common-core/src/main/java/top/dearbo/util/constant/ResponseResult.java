package top.dearbo.util.constant;

import top.dearbo.base.enums.ResultCodeEnum;

/**
 * @version 1.0
 * @author: Bo
 * @fileName: ResponseResult
 * @createDate: 2020-05-28 16:41.
 * @description: 返回结果, data为 T
 */
public class ResponseResult<T> extends AbstractResult<T> {
	private Integer code;
	private String msg;
	private T data;
	/**
	 * 操作失败
	 */
	private static final ResultCodeEnum NORMAL_ERROR_ENUM = ResultCodeEnum.NORMAL_ERROR;

	public ResponseResult() {
		//json反序列化时会执行当前构造函数,避免反序列化时会设置默认值
	}

	public ResponseResult(T data) {
		this(data, true);
	}

	public ResponseResult(T data, Boolean serializeNull) {
		this(SUCCESS_ENUM.getKey(), SUCCESS_ENUM.getValue(), data);
		this.serializeNull = serializeNull;
	}

	public ResponseResult(int code, String msg) {
		this(code, msg, null);
	}

	public ResponseResult(int code, String msg, T data) {
		this.data = data;
		this.code = code;
		this.msg = msg;
	}

	public ResponseResult(int code, String msg, T data, Boolean serializeNull) {
		this(code, msg, data);
		this.serializeNull = serializeNull;
	}

	public ResponseResult<T> serializeNotNull() {
		return serializeNulls(false);
	}

	public ResponseResult<T> serializeNulls(boolean serializeNull) {
		this.serializeNull = serializeNull;
		return this;
	}

	//=========成功=========

	public static ResponseResult<Void> ok() {
		return ok(null);
	}

	public static <T> ResponseResult<T> ok(T data) {
		return ok(data, SUCCESS_ENUM.getValue());
	}

	public static <T> ResponseResult<T> ok(T data, String msg) {
		return new ResponseResult<>(SUCCESS_ENUM.getKey(), msg, data);
	}

	//=========失败=========

	public static ResponseResult<Void> failed() {
		return failed(FAIL_ENUM.getValue());
	}

	public static ResponseResult<Void> failed(String msg) {
		ResponseResult<Void> failed = failed(msg, null);
		failed.setSerializeNull(false);
		return failed;
	}

	public static <T> ResponseResult<T> failed(String msg, T data) {
		return new ResponseResult<>(FAIL_ENUM.getKey(), msg, data, data != null);
	}

	//=========操作=========

	/**
	 * 用于修改、新增操作直接返回
	 *
	 * @param row 执行成功行数
	 * @return code:大于0返回：1,否则0
	 */
	public static ResponseResult<Void> operate(int row) {
		return operate(row > 0);
	}

	public static ResponseResult<Void> operate(int row, String errorMsg) {
		return operate(row > 0, errorMsg);
	}

	public static ResponseResult<Void> operate(int row, String successMsg, String errorMsg) {
		return operate(row > 0, successMsg, errorMsg);
	}

	public static <T> ResponseResult<T> operate(int row, String successMsg, String errorMsg, T data) {
		return operate(row > 0, successMsg, errorMsg, data);
	}

	public static ResponseResult<Void> operate(boolean successFlag) {
		return operate(successFlag, "操作失败");
	}

	public static ResponseResult<Void> operate(boolean successFlag, String errorMsg) {
		return operate(successFlag, "操作成功", errorMsg);
	}

	public static ResponseResult<Void> operate(boolean successFlag, String successMsg, String errorMsg) {
		return operate(successFlag, successMsg, errorMsg, null);
	}

	public static <T> ResponseResult<T> operate(boolean successFlag, String successMsg, String errorMsg, T data) {
		return new ResponseResult<>(successFlag ? SUCCESS_ENUM.getKey() : NORMAL_ERROR_ENUM.getKey(), successFlag ? successMsg : errorMsg, successFlag ? data : null).serializeNulls(successFlag && data != null);
	}

	//===========自定义操作===========

	public static ResponseResult<Void> restResult(ResultCodeEnum resultCodeEnum) {
		return restResult(resultCodeEnum.getKey(), resultCodeEnum.getValue(), null, false);
	}

	public static <T> ResponseResult<T> restResult(ResultCodeEnum resultCodeEnum, T data) {
		return restResult(resultCodeEnum.getKey(), resultCodeEnum.getValue(), data, true);
	}

	public static ResponseResult<Void> restResult(int code, String msg) {
		return restResult(code, msg, null, false);
	}

	public static <T> ResponseResult<T> restResult(int code, String msg, T data) {
		return restResult(code, msg, data, true);
	}

	public static <T> ResponseResult<T> restResult(int code, String msg, T data, Boolean serializeNull) {
		return new ResponseResult<>(code, msg, data, serializeNull);
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
	public T getData() {
		return data;
	}


	public ResponseResult<T> setData(T data) {
		this.data = data;
		return this;
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
