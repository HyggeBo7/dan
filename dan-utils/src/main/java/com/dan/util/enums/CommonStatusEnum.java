package com.dan.util.enums;

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
     * 服务器内部错误
     */
    SERVER_ERROR(-500, "非预期异常"),
    /**
     * 无权限
     */
    NO_PERMISSION(-400, "无权限"),
    /**
     * 登录过期
     */
    LOGIN_EXPIRED(-401, "认证过期");

    CommonStatusEnum(int value, String msg) {
        this.value = value;
        this.msg = msg;
    }

    public int value;
    public String msg;
}
