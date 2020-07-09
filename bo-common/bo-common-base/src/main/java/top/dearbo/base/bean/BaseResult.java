package top.dearbo.base.bean;


import top.dearbo.base.enums.CommonStatusEnum;

import java.io.Serializable;

/**
 * @fileName: BaseSerializable
 * @author: Dan
 * @createDate: 2019-01-24 13:06.
 * @description: 序列化
 */
public interface BaseResult extends Serializable {

    /**
     * 成功code
     */
    int SUCCESS_CODE = CommonStatusEnum.SUCCESS.value;
    /**
     * 失败code
     */
    int FAIL_CODE = CommonStatusEnum.FAIL.value;

    /**
     * 是否序列化null字段
     *
     * @return true:序列化null字段
     */
    boolean isSerializeNullField();

    /**
     * 结果标识是否成功
     *
     * @return boolean
     */
    boolean isSuccess();

    /**
     * 获取编码
     *
     * @return Integer
     */
    Integer getCode();

    /**
     * 获取提示内容
     *
     * @return String
     */
    String getMessage();

}
