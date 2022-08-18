package top.dearbo.base.bean;


import top.dearbo.base.enums.ResultCodeEnum;

import java.beans.Transient;
import java.io.Serializable;

/**
 * @fileName: BaseSerializable
 * @author: bo
 * @createDate: 2019-01-24 13:06.
 * @description: 序列化
 * ps:自定义方法不要加is、get等前缀
 */
public interface BaseResult extends Serializable {

    /**
     * 成功code
     */
    Integer SUCCESS_CODE = ResultCodeEnum.SUCCESS.getKey();
    /**
     * 失败code
     */
    Integer FAIL_CODE = ResultCodeEnum.FAIL.getKey();

    /**
     * 是否序列化null字段
     *
     * @return true:序列化null字段
     */
    @Transient
    boolean resultSerializeNullField();

    /**
     * 结果标识是否成功
     *
     * @return boolean
     */
    @Transient
    boolean resultSuccess();

    /**
     * 获取编码
     *
     * @return Integer
     */
    @Transient
    Integer resultCode();

    /**
     * 获取提示内容
     *
     * @return String
     */
    @Transient
    String resultMessage();

}
