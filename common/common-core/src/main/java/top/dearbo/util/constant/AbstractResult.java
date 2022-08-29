package top.dearbo.util.constant;

import top.dearbo.base.bean.BaseResult;
import top.dearbo.util.data.JsonUtil;

/**
 * 通用的处理返回结果
 *
 * @author wb
 * @date 2020-04-03
 */
public abstract class AbstractResult<T> implements BaseResult {
    protected transient Boolean serializeNull;

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
        return SUCCESS_ENUM.getKey();
    }

    public boolean isSuccess() {
        return resultSuccess();
    }

    public String toJson() {
        return JsonUtil.toJson(this);
    }

    public String toDataJson() {
        T data = getData();
        if (data != null && data instanceof String) {
            return data.toString();
        }
        return JsonUtil.toJson(data);
    }

    /**
     * 参数data
     *
     * @return t
     */
    protected abstract T getData();

}
