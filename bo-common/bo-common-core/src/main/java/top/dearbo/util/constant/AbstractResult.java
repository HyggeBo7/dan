package top.dearbo.util.constant;

import top.dearbo.base.bean.BaseResult;
import top.dearbo.util.lang.ObjectUtil;

/**
 * @version 1.0
 * @author: Bo
 * @fileName: Result
 * @createDate: 2020-04-03 09:44.
 * @description: 通用的处理返回结果
 */
public abstract class AbstractResult<T> implements BaseResult {
    private static final long serialVersionUID = -1620945809386169396L;
    protected transient Boolean serializeNull;

    public Boolean getSerializeNull() {
        return serializeNull;
    }

    public void setSerializeNull(Boolean serializeNull) {
        this.serializeNull = serializeNull;
    }

    /**
     * 获取默认成功标识code
     *
     * @return Integer
     */
    public Integer getDefaultSuccessCode() {
        return SUCCESS_CODE;
    }

    @Override
    public boolean isSuccess() {
        Integer code = getCode();
        return code != null && code.equals(getDefaultSuccessCode());
    }

    @Override
    public boolean isSerializeNullField() {
        return ObjectUtil.booleanIsNotFalse(serializeNull);
    }

    abstract T getData();
}
