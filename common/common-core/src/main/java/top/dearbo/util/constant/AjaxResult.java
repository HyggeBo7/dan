package top.dearbo.util.constant;

import org.apache.commons.lang3.BooleanUtils;
import top.dearbo.base.bean.BaseResult;
import top.dearbo.base.enums.ResultCodeEnum;

import java.util.LinkedHashMap;

/**
 * @version 2.0
 * @author: Bo
 * @createDate: 2020-05-28 16:41.
 * @description: 返回结果, data为Object
 */
public class AjaxResult extends LinkedHashMap<String, Object> implements BaseResult {

	/**
	 * 状态码
	 */
	public static final String CODE_TAG = "code";

	/**
	 * 返回内容
	 */
	public static final String MSG_TAG = "msg";

	/**
	 * 数据对象
	 */
	public static final String DATA_TAG = "data";

	protected transient Boolean serializeNull;

	public AjaxResult() {
		//json反序列化时会执行当前构造函数,避免反序列化时会设置默认值
	}

	public AjaxResult(Object data) {
		this(data, true);
	}

	public AjaxResult(Object data, Boolean serializeNull) {
		this(ResultCodeEnum.SUCCESS.getCode(), ResultCodeEnum.SUCCESS.getValue(), data, serializeNull);
	}

	public AjaxResult(int code, String msg) {
		this(code, msg, null, false);
	}

	public AjaxResult(int code, String msg, Object data) {
		this(code, msg, data, true);
	}

	public AjaxResult(int code, String msg, Object data, Boolean serializeNull) {
		super.put(CODE_TAG, code);
		super.put(MSG_TAG, msg);
		if (data != null || BooleanUtils.isTrue(serializeNull)) {
			super.put(DATA_TAG, data);
		}
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
		return new AjaxResult(ResultCodeEnum.SUCCESS.getCode(), ResultCodeEnum.SUCCESS.getValue(), data);
	}

	public static AjaxResult success(Object data, String msg) {
		return new AjaxResult(ResultCodeEnum.SUCCESS.getCode(), msg, data);
	}

	//=========失败=========

	public static AjaxResult failed() {
		return failed(ResultCodeEnum.FAIL.getValue());
	}

	public static AjaxResult failed(String msg) {
		return new AjaxResult(ResultCodeEnum.FAIL.getCode(), msg, null, false);
	}

	public static AjaxResult failed(String msg, Object data) {
		return new AjaxResult(ResultCodeEnum.FAIL.getCode(), msg, data, true);
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
		return new AjaxResult(successFlag ? ResultCodeEnum.SUCCESS.getCode() : ResultCodeEnum.FAIL.getCode(), successFlag ? successMsg : errorMsg, successFlag ? data : null).serializeNulls(successFlag && data != null);
	}

	//===========自定义操作===========

	public static AjaxResult restResult(ResultCodeEnum resultCodeEnum) {
		return new AjaxResult(resultCodeEnum.getKey(), resultCodeEnum.getValue(), null, false);
	}

	public static AjaxResult restResult(ResultCodeEnum resultCodeEnum, Object data) {
		return new AjaxResult(resultCodeEnum.getKey(), resultCodeEnum.getValue(), data, true);
	}

	public static AjaxResult restResult(int code, String msg) {
		return new AjaxResult(code, msg, null, false);
	}

	public static AjaxResult restResult(int code, String msg, Object data) {
		return new AjaxResult(code, msg, data, true);
	}

	public Integer getCode() {
		Object code = get(CODE_TAG);
		return code == null ? null : (Integer) code;
	}

	public AjaxResult setCode(Integer code) {
		super.put(CODE_TAG, code);
		return this;
	}

	public String getMsg() {
		Object msg = get(MSG_TAG);
		return msg == null ? null : (String) msg;
	}

	public AjaxResult setMsg(String msg) {
		super.put(MSG_TAG, msg);
		return this;
	}

	public Object getData() {
		return get(DATA_TAG);
	}

	public AjaxResult setData(Object data) {
		super.put(DATA_TAG, data);
		return this;
	}

	/**
	 * 方便链式调用
	 *
	 * @param key   键
	 * @param value 值
	 * @return 数据对象
	 */
	@Override
	public AjaxResult put(String key, Object value) {
		super.put(key, value);
		return this;
	}

	@Override
	public boolean resultSerializeNullField() {
		return BooleanUtils.isTrue(serializeNull);
	}

	@Override
	public boolean resultSuccess() {
		Integer code = getCode();
		return ResultCodeEnum.SUCCESS.getCode().equals(code) || ResultCodeEnum.SUCCESS_200.getCode().equals(code);
	}

	@Override
	public Integer resultCode() {
		return getCode();
	}

	@Override
	public String resultMessage() {
		return getMsg();
	}
}
