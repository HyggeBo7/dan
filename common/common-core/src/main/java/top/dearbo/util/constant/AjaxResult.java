package top.dearbo.util.constant;

import top.dearbo.base.enums.ResultCodeEnum;

/**
 * @version 1.0
 * @author: Bo
 * @fileName: AjaxResult
 * @createDate: 2020-05-28 16:41.
 * @description: 返回结果, data为Object
 */
public class AjaxResult extends AbstractResult<Object> {
	private Integer code;
	private String msg;
	private Object data;

	public AjaxResult() {
		//json反序列化时会执行当前构造函数,避免反序列化时会设置默认值
	}

	public AjaxResult(Object data) {
		this(data, true);
	}

	public AjaxResult(Object data, Boolean serializeNull) {
		this(SUCCESS_CODE, SUCCESS_MSG, data);
		this.serializeNull = serializeNull;
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

	public AjaxResult serializeNotNull() {
		return serializeNulls(false);
	}

	public AjaxResult serializeNulls(boolean serializeNull) {
		this.serializeNull = serializeNull;
		return this;
	}

	//=========成功=========

	public static AjaxResult success() {
		return success(null);
	}

	public static AjaxResult success(Object data) {
		return new AjaxResult(SUCCESS_CODE, SUCCESS_MSG, data);
	}

	public static AjaxResult success(Object data, String msg) {
		return new AjaxResult(SUCCESS_CODE, msg, data);
	}

	//=========失败=========

	public static AjaxResult failed() {
		return failed(FAIL_MSG);
	}

	public static AjaxResult failed(String msg) {
		return failed(msg, null).serializeNulls(false);
	}

	public static AjaxResult failed(String msg, Object data) {
		return new AjaxResult(FAIL_CODE, msg, data, true);
	}

	//=========操作=========

	/**
	 * 用于修改、新增操作直接返回
	 *
	 * @param row 执行成功行数
	 * @return code:大于0返回：1,否则0
	 */
	public static AjaxResult operate(int row) {
		return operate(row > 0);
	}

	public static AjaxResult operate(int row, String errorMsg) {
		return operate(row > 0, errorMsg);
	}

	public static AjaxResult operate(int row, String successMsg, String errorMsg) {
		return operate(row > 0, successMsg, errorMsg);
	}

	public static AjaxResult operate(int row, String successMsg, String errorMsg, Object data) {
		return operate(row > 0, successMsg, errorMsg, data);
	}

	public static AjaxResult operate(boolean successFlag) {
		return operate(successFlag, "操作失败");
	}

	public static AjaxResult operate(boolean successFlag, String errorMsg) {
		return operate(successFlag, "操作成功", errorMsg);
	}

	public static AjaxResult operate(boolean successFlag, String successMsg, String errorMsg) {
		return operate(successFlag, successMsg, errorMsg, null);
	}

	public static AjaxResult operate(boolean successFlag, String successMsg, String errorMsg, Object data) {
		return new AjaxResult(successFlag ? SUCCESS_CODE : FAIL_CODE, successFlag ? successMsg : errorMsg, successFlag ? data : null).serializeNulls(successFlag && data != null);
	}

	//===========自定义操作===========

	public static AjaxResult restResult(ResultCodeEnum resultCodeEnum) {
		return new AjaxResult(resultCodeEnum.getKey(), resultCodeEnum.getValue(), null, false);
	}

	public static AjaxResult restResult(ResultCodeEnum resultCodeEnum, Object data) {
		return new AjaxResult(resultCodeEnum.getKey(), resultCodeEnum.getValue(), null, true);
	}

	public static AjaxResult restResult(int code, String msg) {
		return new AjaxResult(code, msg, null, false);
	}

	public static AjaxResult restResult(int code, String msg, Object data) {
		return new AjaxResult(code, msg, data, true);
	}

	public Integer getCode() {
		return code;
	}

	public AjaxResult setCode(Integer code) {
		this.code = code;
		return this;
	}

	public String getMsg() {
		return msg;
	}

	public AjaxResult setMsg(String msg) {
		this.msg = msg;
		return this;
	}

	@Override
	public Object getData() {
		return data;
	}

	public AjaxResult setData(Object data) {
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
