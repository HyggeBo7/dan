package top.dearbo.base.enums;

/**
 * @author Bo
 * @date 2020/5/28
 */
public enum CommonStatusEnum {
    /**
     * 成功
     */
    SUCCESS(1, "成功"),
    /**
     * 操作失败
     */
    NORMAL_ERROR(0, "操作失败"),
    /**
     * 逻辑错误
     */
    FAIL(-1, "逻辑错误"),
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
    PARAM_FAIL(-10, "参数错误"),
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
     * 服务器内部错误
     */
    SERVER_ERROR(-500, "服务器错误"),
    /**
     * 无权限
     */
    NO_PERMISSION(-400, "无权限"),
    /**
     * 登录过期
     */
    LOGIN_EXPIRED(-401, "认证过期"),
    /**
     * 认证失败
     */
    LOGIN_FAIL(-402, "认证失败");

    CommonStatusEnum(int value, String msg) {
        this.value = value;
        this.msg = msg;
    }

    private final int value;
    private final String msg;

    /**
     * 根据name获取code
     *
     * @param name 如：SUCCESS
     * @return code
     */
    public static Integer getCode(String name) {
        for (CommonStatusEnum item : values()) {
            if (item.name().equals(name)) {
                return item.value;
            }
        }
        return null;
    }

    /**
     * 根据name获取msg
     *
     * @param name name
     * @return msg
     */
    public static String getMessage(String name) {
        for (CommonStatusEnum item : values()) {
            if (item.name().equals(name)) {
                return item.msg;
            }
        }
        return name;
    }

    /**
     * 根据code获取msg
     *
     * @param code code
     * @return msg
     */
    public static String getMessage(Integer code) {
        if (code != null) {
            for (CommonStatusEnum item : values()) {
                if (code.equals(item.value)) {
                    return item.msg;
                }
            }
        }
        return null;
    }

    public int getValue() {
        return value;
    }

    public String getMsg() {
        return msg;
    }
}
