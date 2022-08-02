package top.dearbo.base.enums;

import top.dearbo.base.bean.BaseKeyValueEnum;

/**
 * 是否删除：正常：0，已删除：1
 *
 * @author wb
 * @date 2022/03/21
 */
public enum DeleteTypeEnum implements BaseKeyValueEnum {

	/**
	 * 存在
	 */
	NORMAL(0, "正常", false),

	REMOVE(1, "已删除", true);

	DeleteTypeEnum(Integer code, String msg, Boolean deleteFlag) {
		this.code = code;
		this.msg = msg;
		this.deleteFlag = deleteFlag;
	}

	private final Integer code;

	private final String msg;

	private final Boolean deleteFlag;

	public Integer getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}

	public Boolean getDeleteFlag() {
		return deleteFlag;
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
