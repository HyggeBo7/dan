package top.dearbo.base.enums;

import top.dearbo.base.bean.BaseKeyValueEnum;

/**
 * @author Bo
 * @date 2020/5/28
 */
public enum ResultCodeEnum implements BaseKeyValueEnum {
	/**
	 * 成功
	 */
	SUCCESS(1, "成功"),
	SUCCESS_200(200, "成功"),
	/**
	 * 校验相关自定义错误
	 */
	NORMAL_ERROR(-100, "逻辑错误"),
	/**
	 * 业务处理操作失败
	 */
	FAIL(-101, "操作失败"),
	/**
	 * 数据处理操作失败
	 */
	OPERATE_FAIL(-102, "操作失败"),
	RECORD_EXISTS(-103, "记录已存在"),
	RELATED_DATA(-104, "存在关联数据"),
	VALIDATE_PARAM_FAIL(-105, "参数校验失败"),
	PARAM_FAIL(-106, "参数错误"),
	REQUEST_ILLEGAL(-107, "非法请求"),
	/**
	 * 系统异常以及警告
	 */
	SERVER_ERROR(500, "系统异常,请稍后再试"),
	BUSINESS_ERROR(-500, "系统内部错误"),
	WARN(-501, "警告"),
	UNEXPECTED_FAIL(-502, "非预期异常"),
	REQUEST_PROHIBIT(-503, "禁止访问"),
	UNKNOWN(-504, "未知异常"),
	/**
	 * 授权相关异常
	 */
	TOKEN_INVALID(401, "未授权的访问"),
	NO_PERMISSION(403, "无对应资源访问权限，请联系管理员"),
	NOT_FOUND(404, "资源未找到"),
	LOGIN_OTHER(-400, "用户已在其他地方登录"),
	TOKEN_EXPIRED(-401, "登录状态已过期，请重新登录"),
	LOGIN_FAIL(-402, "认证失败"),
	;

	ResultCodeEnum(Integer code, String value) {
		this.code = code;
		this.value = value;
	}

	private final Integer code;
	private final String value;

	public Integer getCode() {
		return code;
	}

	@Override
	public Integer getKey() {
		return code;
	}

	@Override
	public String getValue() {
		return value;
	}

	@Override
	public String getName() {
		return this.name();
	}

}
