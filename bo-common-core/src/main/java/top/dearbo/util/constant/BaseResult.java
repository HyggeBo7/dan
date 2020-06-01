package top.dearbo.util.constant;

import java.io.Serializable;

/**
 * @fileName: BaseSerializable
 * @author: Dan
 * @createDate: 2019-01-24 13:06.
 * @description: 序列化
 */
public interface BaseResult extends Serializable {

    /**
     * 是否序列化null字段
     *
     * @return true:序列化null字段
     */
    boolean isSerializeNullField();

}
