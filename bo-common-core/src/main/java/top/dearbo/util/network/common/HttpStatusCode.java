package top.dearbo.util.network.common;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Dan on 2019/1/17 14:55
 */
public class HttpStatusCode {

    private static final Map<Integer, String> HTTP_STATUS_CODE_MAP = new ConcurrentHashMap<>();

    static {
        HTTP_STATUS_CODE_MAP.put(0, "服务器无法访问!");
        HTTP_STATUS_CODE_MAP.put(100, "正在等待其余部分!");
        HTTP_STATUS_CODE_MAP.put(101, "切换协议!");
        HTTP_STATUS_CODE_MAP.put(200, "请求成功!");
        HTTP_STATUS_CODE_MAP.put(201, "请求成功并且服务器创建了新的资源!");
        HTTP_STATUS_CODE_MAP.put(202, "服务器已接受请求，但尚未处理!");
        HTTP_STATUS_CODE_MAP.put(203, "非授权信息!");
        HTTP_STATUS_CODE_MAP.put(204, "无内容!");
        HTTP_STATUS_CODE_MAP.put(205, "重置内容!");
        HTTP_STATUS_CODE_MAP.put(206, "部分内容!");
        HTTP_STATUS_CODE_MAP.put(300, "多种选择!");
        HTTP_STATUS_CODE_MAP.put(301, "永久移动!");
        HTTP_STATUS_CODE_MAP.put(302, "临时重定向!");
        HTTP_STATUS_CODE_MAP.put(303, "查看其他位置!");
        HTTP_STATUS_CODE_MAP.put(304, "内容未修改!");
        HTTP_STATUS_CODE_MAP.put(305, "使用代理!");
        HTTP_STATUS_CODE_MAP.put(307, "临时重定向!");
        HTTP_STATUS_CODE_MAP.put(400, "错误的请求!");
        HTTP_STATUS_CODE_MAP.put(401, "未授权!");
        HTTP_STATUS_CODE_MAP.put(402, "Payment Required");
        HTTP_STATUS_CODE_MAP.put(403, "服务器拒绝请求!");
        HTTP_STATUS_CODE_MAP.put(404, "请求不存在!");
        HTTP_STATUS_CODE_MAP.put(405, "禁用请求!");
        HTTP_STATUS_CODE_MAP.put(406, "无法使用请求的内容特性响应请求的网页!");
        HTTP_STATUS_CODE_MAP.put(407, "需要代理授权!");
        HTTP_STATUS_CODE_MAP.put(408, "服务器请求超时!");
        HTTP_STATUS_CODE_MAP.put(409, "冲突!");
        HTTP_STATUS_CODE_MAP.put(410, "请求的资源已永久删除!");
        HTTP_STATUS_CODE_MAP.put(411, "需要有效长度!");
        HTTP_STATUS_CODE_MAP.put(412, "未满足前提条件!");
        HTTP_STATUS_CODE_MAP.put(413, "请求实体过大!");
        HTTP_STATUS_CODE_MAP.put(414, "请求的 URI 过长!");
        HTTP_STATUS_CODE_MAP.put(415, "不支持的媒体类型!");
        HTTP_STATUS_CODE_MAP.put(416, "请求范围不符合要求!");
        HTTP_STATUS_CODE_MAP.put(417, "未满足期望值!");
        HTTP_STATUS_CODE_MAP.put(500, "服务器内部错误!");
        HTTP_STATUS_CODE_MAP.put(501, "尚未实施!");
        HTTP_STATUS_CODE_MAP.put(502, "错误网关!");
        HTTP_STATUS_CODE_MAP.put(503, "服务不可用!");
        HTTP_STATUS_CODE_MAP.put(504, "网关超时!");
        HTTP_STATUS_CODE_MAP.put(505, "HTTP 版本不受支持!");
    }

    public static String getHttpStatusMsg(Integer code) {
        if (HTTP_STATUS_CODE_MAP.containsKey(code)) {
            return HTTP_STATUS_CODE_MAP.get(code);
        }
        return "未知错误!";
    }
}
