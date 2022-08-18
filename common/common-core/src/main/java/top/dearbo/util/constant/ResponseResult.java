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
	private static final Integer NORMAL_ERROR = ResultCodeEnum.NORMAL_ERROR.getKey();

	public ResponseResult() {
		//json反序列化时会执行当前构造函数,避免反序列化时会设置默认值
	}

	public ResponseResult(T data) {
		this(data, true);
	}

	public ResponseResult(T data, boolean serializeNull) {
		this(SUCCESS_CODE, null, data, serializeNull);
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

	//=========成功=========

	public static ResponseResult<Void> ok() {
		return ok(null, true);
	}

	public static <T> ResponseResult<T> ok(T data) {
		return ok(data, true);
	}

	public static <T> ResponseResult<T> ok(T data, boolean serializeNull) {
		return ok(data, "成功", serializeNull);
	}

	public static <T> ResponseResult<T> ok(T data, String msg) {
		return ok(data, msg, true);
	}

	public static <T> ResponseResult<T> ok(T data, String msg, boolean serializeNull) {
		return new ResponseResult<>(SUCCESS_CODE, msg, data, serializeNull);
	}

	//=========失败=========

	public static ResponseResult<Void> failed() {
		return failed("失败");
	}

	public static ResponseResult<Void> failed(String msg) {
		return failed(msg, null, false);
	}

	public static <T> ResponseResult<T> failed(String msg, T data) {
		return failed(msg, data, true);
	}

	public static <T> ResponseResult<T> failed(String msg, T data, boolean serializeNull) {
		return new ResponseResult<>(FAIL_CODE, msg, data, serializeNull);
	}

	//=========操作=========

	/**
	 * 用于修改、新增操作直接返回
	 *
	 * @param row 执行成功行数
	 * @return code:大于0返回：1,否则0
	 */
	public static ResponseResult<Void> operate(int row) {
		return operate(row, null);
	}

	public static <T> ResponseResult<T> operate(int row, T data) {
		return operate(row, data, true);
	}

	public static <T> ResponseResult<T> operate(int row, T data, Boolean serializeNull) {
		return operate(row, data, "操作成功!", "操作失败!", serializeNull);
	}

	public static <T> ResponseResult<T> operate(int row, T data, String successMsg, String errorMsg) {
		return operate(row, data, successMsg, errorMsg, true);
	}

	public static ResponseResult<Void> operate(int row, String successMsg, String errorMsg) {
		return operate(row, null, successMsg, errorMsg, false);
	}

	public static <T> ResponseResult<T> operate(int row, T data, String successMsg, String errorMsg, Boolean serializeNull) {
		boolean success = row > 0;
		return new ResponseResult<>(success ? SUCCESS_CODE : NORMAL_ERROR, success ? successMsg : errorMsg, data, serializeNull);
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

	public void setData(T data) {
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
