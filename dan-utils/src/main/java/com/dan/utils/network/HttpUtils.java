package com.dan.utils.network;

import com.dan.utils.JsonUtil;
import com.dan.utils.entity.BaseSerializable;
import com.dan.utils.exception.AppException;
import com.dan.utils.network.common.HttpStatusCode;
import com.dan.utils.network.exception.HttpException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

/**
 * @fileName: HttpClientUtil
 * @author: Dan
 * @createDate: 2019-01-24 8:47.
 * @description: https、http(get,post)
 */
public class HttpUtils {

    private static final Logger logger = LoggerFactory.getLogger(HttpUtils.class);

    /**
     * 默认编码
     */
    private static final String DEFAULT_ENCODING = "UTF-8";
    /**
     * Https请求
     */
    private static final String HTTPS = "https";

    /**
     * http
     */
    private static final String HTTP = "http";
    private static final String METHOD_GET = "GET";
    private static final String METHOD_POST = "POST";
    private static final int SUCCESS_CODE = 200;
    private static final String ACCEPT = "Accept";
    private static final String ACCEPT_CONTENT_TYPE = "application/json, text/plain, */*";
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String CONTENT_TYPE_1 = "content-type";
    private static final String CONTENT_TYPE_JSON = "application/json";
    //private static final String DEFAULT_CONTENT_TYPE = "application/x-www-form-urlencoded";
    private static final int DEFAULT_CONNECT_TIMEOUT = 1000 * 10;
    private static final int DEFAULT_READ_TIMEOUT = 1000 * 10;
    private int connectTimeout;
    private int readTimeout;
    /**
     * 是否设置通用属性
     */
    private boolean usePropertyFlag = true;

    private HttpUtils() {

    }

    public static HttpUtils createRequest() {
        return new HttpUtils();
    }

    public ResultResponse doPost(String requestUrl) {

        return wrapRequest(requestUrl, null, null, DEFAULT_ENCODING, METHOD_POST);
    }

    public ResultResponse doGet(String requestUrl) {

        return wrapRequest(requestUrl, null, null, DEFAULT_ENCODING, METHOD_GET);
    }

    public ResultResponse doPost(String requestUrl, Map<String, Object> paramMap) {

        return wrapRequest(requestUrl, paramMap, null, DEFAULT_ENCODING, METHOD_POST);
    }

    public ResultResponse doGet(String requestUrl, Map<String, Object> paramMap) {

        return wrapRequest(requestUrl, paramMap, null, DEFAULT_ENCODING, METHOD_GET);
    }

    public ResultResponse doPost(String requestUrl, Map<String, Object> paramMap, Map<String, String> headerMap) {

        return wrapRequest(requestUrl, paramMap, headerMap, DEFAULT_ENCODING, METHOD_POST);
    }

    public ResultResponse doGet(String requestUrl, Map<String, Object> paramMap, Map<String, String> headerMap) {

        return wrapRequest(requestUrl, paramMap, headerMap, DEFAULT_ENCODING, METHOD_GET);
    }

    public ResultResponse doPost(String requestUrl, Map<String, Object> paramMap, Map<String, String> headerMap, String encoding) {

        return wrapRequest(requestUrl, paramMap, headerMap, encoding, METHOD_POST);
    }

    public ResultResponse doGet(String requestUrl, Map<String, Object> paramMap, Map<String, String> headerMap, String encoding) {

        return wrapRequest(requestUrl, paramMap, headerMap, encoding, METHOD_GET);
    }

    public ResultResponse doPostJson(String requestUrl, String paramJson) {

        return doPostJson(requestUrl, paramJson, null);
    }

    public ResultResponse doPostJson(String requestUrl, String paramJson, Map<String, String> headerMap) {

        return doPostJson(requestUrl, paramJson, headerMap, DEFAULT_ENCODING);
    }

    public ResultResponse doPostJson(String requestUrl, String paramJson, Map<String, String> headerMap, String encoding) {
        if (headerMap == null) {
            headerMap = new HashMap<>();
        }
        String contentType = StringUtils.isBlank(headerMap.get(CONTENT_TYPE)) ? headerMap.get(CONTENT_TYPE_1) : headerMap.get(CONTENT_TYPE);
        if (StringUtils.isBlank(contentType)) {
            headerMap.put(CONTENT_TYPE, CONTENT_TYPE_JSON);
        }
        if (StringUtils.isBlank(headerMap.get(ACCEPT))) {
            headerMap.put(ACCEPT, ACCEPT_CONTENT_TYPE);
        }
        return wrapRequest(requestUrl, null, headerMap, encoding, METHOD_POST, paramJson);
    }

    private ResultResponse wrapRequest(String requestUrl, Map<String, Object> paramMap, Map<String, String> headerMap, String encoding, String requestMethod) {
        return wrapRequest(requestUrl, paramMap, headerMap, encoding, requestMethod, null);
    }

    /**
     * 设置http/https,post/get
     */
    private ResultResponse wrapRequest(String requestUrl, Map<String, Object> paramMap, Map<String, String> headerMap, String encoding, String requestMethod, String paramJson) {
        if (StringUtils.isBlank(requestUrl)) {
            logger.info("请求地址不能为空!requestUrl:【{}】", requestUrl);
            AppException.throwEx("请求地址不能为空!requestUrl:" + requestUrl);
        }
        requestMethod = StringUtils.isBlank(requestMethod) ? METHOD_POST : requestMethod;
        boolean methodPostFlag = true;
        if (requestMethod.toUpperCase().equals(METHOD_GET)) {
            methodPostFlag = false;
        }
        encoding = StringUtils.isBlank(encoding) ? DEFAULT_ENCODING : encoding;
        boolean httpsFlag = false;
        if (requestUrl.startsWith(HTTPS)) {
            httpsFlag = true;
        }
        return request(requestUrl, methodPostFlag, paramMap, headerMap, encoding, httpsFlag, paramJson);
    }

    private ResultResponse request(String requestUrl, boolean methodPostFlag, Map<String, Object> paramMap, Map<String, String> headerMap, String encoding, boolean httpsFlag, String paramJson) {
        InputStream inputStream = null;
        PrintWriter out = null;
        BufferedReader in = null;
        HttpURLConnection connection = null;
        try {
            if (!methodPostFlag && paramMap != null) {
                requestUrl += "?" + genUrlParam(paramMap);
            }
            URL url = new URL(requestUrl);
            // 打开和URL之间的连接
            if (httpsFlag) {
                connection = (HttpsURLConnection) url.openConnection();
                setHttps((HttpsURLConnection) connection);
            } else {
                connection = (HttpURLConnection) url.openConnection();
            }
            connection.setConnectTimeout(DEFAULT_CONNECT_TIMEOUT);
            connection.setReadTimeout(DEFAULT_READ_TIMEOUT);

            // 设置请求方式（GET/POST）
            if (methodPostFlag) {
                // 发送POST请求必须设置如下两行
                connection.setDoOutput(true);
                connection.setDoInput(true);
                connection.setRequestMethod(METHOD_POST);
            } else {
                connection.setRequestMethod(METHOD_GET);
            }

            connection.setUseCaches(false);

            //设置头部
            if (headerMap != null && headerMap.size() > 0) {
                for (Map.Entry<String, String> headerItem : headerMap.entrySet()) {
                    connection.setRequestProperty(headerItem.getKey(), headerItem.getValue());
                }
            }
            // 设置通用的请求属性
            if (usePropertyFlag) {
                if (headerMap == null || headerMap.get(ACCEPT) == null) {
                    connection.addRequestProperty("Accept", "*/*");
                }
                connection.addRequestProperty("Connection", "keep-alive");
                //  Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)
                connection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64)");
            }

            // 当paramMap不为null时向输出流写数据
            if (methodPostFlag) {
                boolean paramFlag = null != paramMap && paramMap.size() > 0 || StringUtils.isNotBlank(paramJson);
                if (paramFlag) {
                    // 获取URLConnection对象对应的输出流
                    out = new PrintWriter(connection.getOutputStream());
                    // 发送请求参数
                    if (StringUtils.isNotBlank(paramJson)) {
                        out.print(paramJson);
                    }
                    if (paramMap != null && paramMap.size() > 0) {
                        out.print(genUrlParam(paramMap));
                    }
                    // flush输出流的缓冲
                    out.flush();
                }
            }
            int responseCode = connection.getResponseCode();
            ResultResponse resultResponse = new ResultResponse(responseCode, connection.getResponseMessage());
            if (responseCode == SUCCESS_CODE) {
                // 从输入流读取返回内容
                inputStream = connection.getInputStream();
                in = new BufferedReader(new InputStreamReader(inputStream, encoding));
                String str = null;
                StringBuffer resultBuffer = new StringBuffer();
                while ((str = in.readLine()) != null) {
                    resultBuffer.append(str);
                }
                resultResponse.setData(resultBuffer.toString());
            }
            return resultResponse;
        } catch (IOException ce) {
            //ce.printStackTrace();
            logger.error("url:【{}】,msg:【{}】", requestUrl, ce.getMessage(), ce);
            //AppException.throwEx(ce);
            return HttpException.handleException(ce);
        } finally {
            if (out != null) {
                out.close();
            }
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (connection != null) {
                connection.disconnect();
                connection = null;
            }
        }
    }

    private void setHttps(HttpsURLConnection connection) {
        // 创建SSLContext对象，并使用我们指定的信任管理器初始化
        TrustManager[] tm = {new MyX509TrustManager()};
        try {
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new SecureRandom());
            // 从上述SSLContext对象中得到SSLSocketFactory对象
            SSLSocketFactory ssf = sslContext.getSocketFactory();
            connection.setSSLSocketFactory(ssf);
        } catch (NoSuchAlgorithmException | KeyManagementException | NoSuchProviderException e) {
            e.printStackTrace();
            logger.error("https设置异常!msg:【{}】", e.getMessage(), e);
            AppException.throwEx(e);
        }

    }

    /**
     * 拼接Url
     *
     * @param paramMap 查询参数
     */
    private String genUrlParam(Map<String, Object> paramMap) {
        if (paramMap == null || paramMap.size() == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Object> item : paramMap.entrySet()) {
            sb.append(item.getKey());
            sb.append("=");
            sb.append(item.getValue());
            sb.append("&");
        }
        return sb.toString().substring(0, sb.length() - 1);
    }

    /**
     * url编码
     *
     * @param str     字符串
     * @param charset 编码
     * @throws UnsupportedEncodingException
     */
    public String urlEncoder(String str, String charset) throws UnsupportedEncodingException {
        if (StringUtils.isBlank(str)) {
            str = "";
        }
        return URLEncoder.encode(str, charset);
    }

    public boolean isUsePropertyFlag() {
        return usePropertyFlag;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout < 1 ? DEFAULT_CONNECT_TIMEOUT : connectTimeout;
    }

    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout < 0 ? DEFAULT_READ_TIMEOUT : readTimeout;
    }

    public static class ResultResponse extends BaseSerializable {

        private int status;
        private String msg;
        private String message;
        private String data;

        public ResultResponse() {

        }

        public ResultResponse(int status, String message) {
            this.status = status;
            this.msg = HttpStatusCode.getHttpStatusMsg(status);
            this.message = message;
        }

        public ResultResponse(int status, String message, String data) {
            this.status = status;
            this.msg = HttpStatusCode.getHttpStatusMsg(status);
            this.message = message;
            this.data = data;
        }

        public ResultResponse(int status, String message, String msg, String data) {
            this.status = status;
            this.msg = msg;
            this.message = message;
            this.data = data;
        }


        @Override
        public String toString() {
            return JsonUtil.toJson(this);
        }

        public boolean isSuccess() {
            return status == SUCCESS_CODE;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.msg = HttpStatusCode.getHttpStatusMsg(status);
            this.status = status;
        }

        public String getMsg() {
            return msg;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }
    }

    /**
     * 信任管理器
     */
    class MyX509TrustManager implements X509TrustManager {


        /**
         * 检查客户端证书
         */
        @Override
        public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

        }

        /**
         * 检查服务器端证书
         */
        @Override
        public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

        }

        /**
         * 返回受信任的X509证书数组
         */
        @Override
        public X509Certificate[] getAcceptedIssuers() {
            //return new X509Certificate[0];
            return null;
        }
    }

}
