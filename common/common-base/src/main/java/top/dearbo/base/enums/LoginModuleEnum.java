package top.dearbo.base.enums;


import top.dearbo.base.bean.BaseEnum;

/**
 * 登录模块枚举
 *
 * @author wb
 * @date 2022/08/11 10:41:50.
 */
public enum LoginModuleEnum implements BaseEnum<String, String> {

	/**
	 * pc版
	 */
	PC("pc", "PC"),
	PDA("pda", "PDA"),
	PAD("pad", "PAD"),
	ANDROID("android", "安卓"),
	IOS("ios", "IOS"),
	WE_CHAT("we_chat", "微信"),
	WE_CHAT_APP("we_chat_app", "微信小程序"),
	OTHER("other", "其他");

	LoginModuleEnum(String key, String value) {
		this.key = key;
		this.value = value;
	}

	public final String value;
	public final String key;

	/**
	 * 获取模块-redis前缀,默认pc
	 *
	 * @param keyValue 模块
	 * @return redis 前缀
	 */
	public static String getModuleToRedisPrefix(String keyValue) {
		if (isNotBlank(keyValue)) {
			LoginModuleEnum[] values = values();
			String toLowerCaseValue = keyValue.toLowerCase();
			for (LoginModuleEnum value : values) {
				if (value.key.equals(toLowerCaseValue)) {
					return value.key + ":";
				}
			}
		}
		return PC.key + ":";
	}

	public static String getModule(String keyValue) {
		if (isNotBlank(keyValue)) {
			String toLowerCaseValue = keyValue.toLowerCase();
			LoginModuleEnum[] values = values();
			for (LoginModuleEnum value : values) {
				if (value.key.equals(toLowerCaseValue)) {
					return value.key;
				}
			}
		}
		return PC.key;
	}

	private static boolean isNotBlank(String value) {
		return value != null && !value.trim().isEmpty();
	}

	public String getValue() {
		return value;
	}

	@Override
	public String getName() {
		return this.name();
	}

	public String getKey() {
		return key;
	}
}
