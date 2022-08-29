package top.dearbo.util.constant;

/**
 * @version 1.0
 * @author: Bo
 * @fileName: ResultGeneric
 * @createDate: 2020-05-28 16:41.
 * @description: 通用的解析返回结果
 */
public class ResultGeneric<T> extends AbstractResult<T> {
	private Integer code;
	private String msg;
	private T data;

	public ResultGeneric() {

	}

	public ResultGeneric(T data) {
		this(data, true);
	}

	public ResultGeneric(T data, boolean serializeNull) {
		this(SUCCESS_ENUM.getKey(), SUCCESS_ENUM.getValue(), data, serializeNull);
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

	public ResultGeneric<T> setData(T data) {
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
