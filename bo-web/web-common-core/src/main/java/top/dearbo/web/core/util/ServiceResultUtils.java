package top.dearbo.web.core.util;

import top.dearbo.util.constant.AjaxResult;
import top.dearbo.util.data.JsonUtil;
import top.dearbo.util.exception.AppException;
import top.dearbo.util.xt.Pagination;

import java.util.ArrayList;
import java.util.List;

/**
 * @version 1.0
 * @author: Bo
 * @fileName: ServiceResultUtils
 * @createDate: 2020-07-23 15:40.
 * @description:
 */
public class ServiceResultUtils {

    /**
     * 获取成功的结果,如果不成功或者code不是正常状态,直接抛异常返回(结果为对象)
     *
     * @param ajaxResult 请求其他微服务返回结果
     * @param <T>        data转换的类
     * @param errorMsg   失败/错误提示
     * @return class
     */
    public static <T> T getSuccessData(AjaxResult ajaxResult, final Class<T> cls, String errorMsg) {
        Object data = checkResultData(ajaxResult, errorMsg);
        String dataStr = data instanceof String ? data.toString() : JsonUtil.toJson(data);
        return JsonUtil.fromJson(dataStr, cls);
    }

    /**
     * 获取成功的结果,如果不成功或者code不是正常状态,直接抛异常返回(结果为List 对象)
     */
    public static <T> List<T> getSuccessDataList(AjaxResult ajaxResult, final Class<T> cls, String errorMsg) {
        Object data = checkResultData(ajaxResult, errorMsg);
        String dataStr = data instanceof String ? data.toString() : JsonUtil.toJson(data);
        return JsonUtil.fromListJson(dataStr, cls);
    }

    /**
     * 获取分页成功结果(Pagination<List<cls>>)
     *
     * @param ajaxResult 结果
     * @param cls        class
     * @param errorMsg   错误提示
     * @param <T>        cls 泛型
     * @return List
     */
    public static <T> List<T> getSuccessDataPageList(AjaxResult ajaxResult, final Class<T> cls, String errorMsg) {
        Object data = checkResultData(ajaxResult, errorMsg);
        String dataStr = data instanceof String ? data.toString() : JsonUtil.toJson(data);
        Pagination<T> o = JsonUtil.fromGenericJson(dataStr, Pagination.class, cls);
        return o == null ? new ArrayList<>() : o.getData();
    }

    public static Object checkResultData(AjaxResult ajaxResult, String errorMsg) {
        if (ajaxResult == null || !ajaxResult.resultSuccess() || ajaxResult.getData() == null) {
            AppException.throwEx(ajaxResult == null ? errorMsg : ajaxResult.resultMessage());
        }
        return ajaxResult.getData();
    }

}
