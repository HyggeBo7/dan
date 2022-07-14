package top.dearbo.log.config;

import top.dearbo.util.data.JsonUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @version 1.0
 * @author: Bo
 * @fileName: SysLogConfig
 * @createDate: 2020-07-01 17:08.
 * @description: 配置类
 */
public interface SysLogConfig {

    /**
     * 获取用户名称
     *
     * @param request HttpServletRequest
     * @return String
     */
    default String getUserName(HttpServletRequest request) {
        Object userName = request.getAttribute("userName");
        return userName == null ? null : userName.toString();
    }

    /**
     * 获取用户id
     *
     * @param request HttpServletRequest
     * @return String
     */
    default Integer getUserId(HttpServletRequest request) {
        Object userId = request.getAttribute("userId");
        return userId == null ? null : Integer.parseInt(userId.toString());
    }

    /**
     * 是否储存日志,默认获取用户名为空时不保存
     *
     * @param request HttpServletRequest
     * @return boolean
     */
    default boolean checkLog(HttpServletRequest request) {
        return request.getAttribute("userName") != null;
    }

    /**
     * 获取返回结果
     *
     * @param resultData 返回结果
     * @return string
     */
    default String getResultData(Object resultData) {
        if (resultData == null) {
            return null;
        }
        return resultData instanceof String ? resultData.toString() : JsonUtil.toJson(resultData);
    }

    /**
     * 排除保存参数的key
     *
     * @return 排除
     */
    List<String> excludeParamKey();

    /**
     * 获取操作类型
     *
     * @param request HttpServletRequest
     * @return String
     */
    String getType(HttpServletRequest request);

    /**
     * 获取模块(默认截取请求api第一位)
     *
     * @param request HttpServletRequest
     * @return String
     */
    String getSubject(HttpServletRequest request);

}
