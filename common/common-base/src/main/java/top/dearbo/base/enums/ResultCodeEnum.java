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
	 * 逻辑错误
	 */
	NORMAL_ERROR(0, "逻辑错误"),
	/**
	 * 操作失败
	 */
	FAIL(-1, "操作失败"),
	/**
	 * 记录已存在
	 */
	RECORD_EXISTS(-2, "记录已存在"),
	/**
	 * 存在关联数据
	 */
	RELATED_DATA(-3, "存在关联数据"),
	/**
	 * 参数错误
	 */
	PARAM_FAIL(-9, "参数错误"),
	/**
	 * 参数校验失败
	 */
	VALIDATE_PARAM_FAIL(-10, "参数校验失败"),
	/**
	 * 非预期异常
	 */
	UNEXPECTED_FAIL(-11, "非预期异常"),
	/**
	 * 非法请求
	 */
	REQUEST_ILLEGAL(-12, "非法请求"),
	/**
	 * 临时限制访问
	 */
	REQUEST_PROHIBIT(-13, "禁止访问"),
	/**
	 * 未知异常
	 */
	UNKNOWN(-99, "未知异常"),
	/**
	 * 服务器内部错误
	 */
	SERVER_ERROR(-500, "系统异常,请稍后再试"),
	/**
	 * 无权限
	 */
	NO_PERMISSION(-400, "访问的资源未授权"),
	/**
	 * 登录过期
	 */
	LOGIN_EXPIRED(-401, "认证过期"),
	/**
	 * 认证失败
	 */
	LOGIN_FAIL(-402, "认证失败");

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
