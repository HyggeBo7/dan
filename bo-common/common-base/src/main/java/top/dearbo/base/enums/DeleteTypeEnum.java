package top.dearbo.base.enums;

import top.dearbo.base.bean.BaseKeyValueEnum;

/**
 * @author wb
 * @date 2022/03/21
 */
public enum DeleteTypeEnum implements BaseKeyValueEnum {

	/**
	 * 存在
	 */
	NORMAL(1, "正常"),

	REMOVE(0, "已删除");

	DeleteTypeEnum(Integer code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	private final Integer code;

	private final String msg;

	public Integer getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}

	@Override
	public Integer getKey() {
		return code;
	}

	@Override
	public String getValue() {
		return msg;
	}

	@Override
	public String getName() {
		return this.name();
	}
}
