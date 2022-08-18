package top.dearbo.util.constant;

import top.dearbo.base.bean.BaseResult;
import top.dearbo.util.data.JsonUtil;

import java.beans.Transient;

/**
 * @version 1.0
 * @author: Bo
 * @fileName: Result
 * @createDate: 2020-04-03 09:44.
 * @description: 通用的处理返回结果
 */
public abstract class AbstractResult<T> implements BaseResult {
	protected transient Boolean serializeNull;

	@Transient
	public Boolean getSerializeNull() {
		return serializeNull;
	}

	public void setSerializeNull(Boolean serializeNull) {
		this.serializeNull = serializeNull;
	}

	@Override
	public boolean resultSuccess() {
		Integer code = resultCode();
		return code != null && code.equals(defaultSuccessCode());
	}

	@Override
	public boolean resultSerializeNullField() {
		return serializeNull == null || serializeNull;
	}

	/**
	 * 获取默认成功标识code
	 *
	 * @return Integer
	 */
	public Integer defaultSuccessCode() {
		return SUCCESS_CODE;
	}

	public boolean isSuccess() {
		return resultSuccess();
	}

	public String toJson() {
		return JsonUtil.toJson(this);
	}

	public String toDataJson() {
		T data = getData();
		if (data != null && data instanceof String) {
			return data.toString();
		}
		return JsonUtil.toJson(data);
	}

	/**
	 * 参数data
	 *
	 * @return t
	 */
	protected abstract T getData();

}
