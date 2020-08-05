package top.dearbo.base.bean;

/**
 * @version 1.0
 * @author: Bo
 * @fileName: Result
 * @createDate: 2020-04-03 09:44.
 * @description: 通用的处理返回结果
 */
public abstract class AbstractResult<T> implements BaseResult {
    private static final long serialVersionUID = 535712466918126917L;
    protected transient Boolean serializeNull;

    public Boolean getSerializeNull() {
        return serializeNull;
    }

    public void setSerializeNull(Boolean serializeNull) {
        this.serializeNull = serializeNull;
    }

    @Override
    public boolean resultSuccess() {
        Integer code = resultCode();
        return code != null && code.equals(defaultSuccessCode());
    }

    @Override
    public boolean resultSerializeNullField() {
        return serializeNull == null || serializeNull;
    }

    /**
     * 获取默认成功标识code
     *
     * @return Integer
     */
    public Integer defaultSuccessCode() {
        return SUCCESS_CODE;
    }

    public boolean isSuccess() {
        return resultSuccess();
    }

    /**
     * 参数data
     *
     * @return t
     */
    protected abstract T getData();
}
