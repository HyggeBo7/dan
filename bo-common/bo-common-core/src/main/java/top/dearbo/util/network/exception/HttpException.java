package top.dearbo.util.network.exception;

import top.dearbo.util.constant.ResultGeneric;
import top.dearbo.util.network.HttpClientPoolUtil;
import top.dearbo.util.network.HttpUtils;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.URISyntaxException;
import java.net.UnknownHostException;

/**
 * @version 1.0
 * @author: Bo
 * @fileName: HttpException
 * @createDate: 2019-08-21 13:42.
 * @description:
 */
public class HttpException {

	public static ResultGeneric<String> handleException(Throwable e) {
		if (e instanceof ConnectException) {
			return new ResultGeneric<>(HttpError.NETWORK_ERROR, "连接失败!", e.getMessage());
		} else if (e instanceof SocketTimeoutException) {
			return new ResultGeneric<>(HttpError.TIMEOUT_ERROR, "连接超时!", e.getMessage());
		} else if (e instanceof UnknownHostException) {
			return new ResultGeneric<>(HttpError.UNKNOWNHOST_ERROR, "无法解析该域名!", e.getMessage());
		} else if (e instanceof NullPointerException) {
			return new ResultGeneric<>(HttpError.NULLPOINTER_EXCEPTION, "NullPointerException!", e.getMessage());
		} else if (e instanceof ClassCastException) {
			return new ResultGeneric<>(HttpError.CAST_ERROR, "类型转换错误!", e.getMessage());
		} else if (e instanceof URISyntaxException) {
			return new ResultGeneric<>(HttpError.PARSE_ERROR, "示无法将字符串解析为URI!", e.getMessage());
		}
		return new ResultGeneric<>(HttpError.UNKNOWN, "未知错误!", e.getMessage());
	}

	public static HttpUtils.ResultResponse httpHandleException(Throwable e) {
		ResultGeneric<String> resultGeneric = handleException(e);
		return new HttpUtils.ResultResponse(resultGeneric.getCode(), resultGeneric.getData(), resultGeneric.getMsg(), null);
	}

	public static HttpClientPoolUtil.ResultResponse httpClientHandleException(Throwable e) {
		ResultGeneric<String> resultGeneric = handleException(e);
		return new HttpClientPoolUtil.ResultResponse(resultGeneric.getCode(), resultGeneric.getData(), resultGeneric.getMsg(), null);
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
