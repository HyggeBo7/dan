package top.dearbo.util.network.exception;

import top.dearbo.util.network.HttpUtils;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

/**
 * @version 1.0
 * @author: Bo
 * @fileName: HttpException
 * @createDate: 2019-08-21 13:42.
 * @description:
 */
public class HttpException {

    public static HttpUtils.ResultResponse handleException(Throwable e) {
        if (e instanceof ConnectException) {
            return new HttpUtils.ResultResponse(HttpError.NETWORK_ERROR, e.getMessage(), "连接失败!", null);
        } else if (e instanceof SocketTimeoutException) {
            return new HttpUtils.ResultResponse(HttpError.TIMEOUT_ERROR, e.getMessage(), "连接超时!", null);
        } else if (e instanceof UnknownHostException) {
            return new HttpUtils.ResultResponse(HttpError.UNKNOWNHOST_ERROR, e.getMessage(), "无法解析该域名!", null);
        } else if (e instanceof NullPointerException) {
            return new HttpUtils.ResultResponse(HttpError.NULLPOINTER_EXCEPTION, e.getMessage(), "NullPointerException!", null);
        } else if (e instanceof ClassCastException) {
            return new HttpUtils.ResultResponse(HttpError.CAST_ERROR, e.getMessage(), "类型转换错误!", null);
        }
        return new HttpUtils.ResultResponse(HttpError.UNKNOWN, e.getMessage(), "未知错误!", null);
    }

    /**
     * 约定异常
     */
    public static class HttpError {
        /**
         * 未知错误
         */
        public static final int UNKNOWN = 1000;

        /**
         * 解析错误
         */
        public static final int PARSE_ERROR = UNKNOWN + 1;
        /**
         * 网络错误
         */
        public static final int NETWORK_ERROR = PARSE_ERROR + 1;

        /**
         * 证书出错
         */
        public static final int SSL_ERROR = NETWORK_ERROR + 1;

        /**
         * 连接超时
         */
        public static final int TIMEOUT_ERROR = SSL_ERROR + 1;

        /**
         * 调用错误
         */
        public static final int INVOKE_ERROR = TIMEOUT_ERROR + 1;
        /**
         * 类转换错误
         */
        public static final int CAST_ERROR = INVOKE_ERROR + 1;
        /**
         * 请求取消
         */
        public static final int REQUEST_CANCEL = CAST_ERROR + 1;
        /**
         * 未知主机错误
         */
        public static final int UNKNOWNHOST_ERROR = REQUEST_CANCEL + 1;

        /**
         * 空指针错误
         */
        public static final int NULLPOINTER_EXCEPTION = UNKNOWNHOST_ERROR + 1;
    }

}
