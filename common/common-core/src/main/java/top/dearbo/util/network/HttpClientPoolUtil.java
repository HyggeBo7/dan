package top.dearbo.util.network;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.*;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.StandardHttpRequestRetryHandler;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.dearbo.util.exception.AppException;
import top.dearbo.util.file.StreamUtil;
import top.dearbo.util.network.common.HttpGlobalConfig;
import top.dearbo.util.network.common.HttpStatusCode;
import top.dearbo.util.network.exception.HttpException;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.*;


/**
 * HttpClient 工具类,带线程池
 *
 * @author wb
 * @date 2021/06/09 20:16:49.
 */
public class HttpClientPoolUtil {

    private static final Logger logger = LoggerFactory.getLogger(HttpClientPoolUtil.class);

    private static final int DEFAULT_CONNECT_TIMEOUT = 1000 * 10;
    private static final int DEFAULT_READ_TIMEOUT = 1000 * 10;
    private static final int DEFAULT_SOCKET_TIMEOUT = 1000 * 10 * 2;
    /**
     * 结果转String类型
     */
    private boolean resultToStringFlag = true;
    /**
     * 是否自动关闭
     */
    private boolean disconnectFlag = true;
    /**
     * 表示成功的编码
     */
    private int successCode = HttpCommonUtil.DEFAULT_SUCCESS_CODE;
    /**
     * 是否对请求参数key进行编码(默认需要)
     */
    private boolean encodeParamKeyFlag = true;
    /**
     * 是否对请求参数value进行编码(默认需要)
     */
    private boolean encodeParamValueFlag = true;
    /**
     * 参数编码
     */
    private String paramCharset = HttpCommonUtil.DEFAULT_ENCODING;
    /**
     * 返回结果编码
     */
    private String resultCharset;
    /**
     * 默认cookie字段
     */
    private String defaultHeaderCookieField = "Set-Cookie";

    private RequestConfig requestConfig = null;

    private CloseableHttpClient httpClient = null;

    /**
     * 当前代理类
     */
    private HttpHost proxy;
    /**
     * 是否使用全局代理
     */
    private boolean globalProxyFlag;

    /**
     * 是否设置通用属性
     */
    private boolean usePropertyFlag = true;

    private volatile static CloseableHttpClient defaultHttpClient;

    static {
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        // 总连接池数量
        connectionManager.setMaxTotal(300);
        // 可为每个域名设置单独的连接池数量
        connectionManager.setDefaultMaxPerRoute(100);
        // setConnectTimeout：设置建立连接的超时时间
        // setConnectionRequestTimeout：从连接池中拿连接的等待超时时间
        // setSocketTimeout：发出请求后等待对端应答的超时时间
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(DEFAULT_CONNECT_TIMEOUT)
                .setConnectionRequestTimeout(DEFAULT_READ_TIMEOUT)
                .setSocketTimeout(DEFAULT_SOCKET_TIMEOUT)
                .build();
        // 重试处理器，StandardHttpRequestRetryHandler
        HttpRequestRetryHandler retryHandler = new StandardHttpRequestRetryHandler();
        defaultHttpClient = HttpClients.custom().setConnectionManager(connectionManager).setDefaultRequestConfig(requestConfig)
                .setRetryHandler(retryHandler).build();
    }

    private HttpClientPoolUtil() {

    }

    public static HttpClientPoolUtil createRequest() {
        return new HttpClientPoolUtil();
    }

    public ResultResponse doPost(String requestUrl) {
        return doPost(requestUrl, null, null);
    }

    public ResultResponse doGet(String requestUrl) {
        return doGet(requestUrl, null, null);
    }

    public ResultResponse doPost(String requestUrl, Map<String, Object> paramMap) {
        return doPost(requestUrl, paramMap, null);
    }

    public ResultResponse doGet(String requestUrl, Map<String, Object> paramMap) {
        return doGet(requestUrl, paramMap, null);
    }

    public ResultResponse doPost(String requestUrl, Map<String, Object> paramMap, Map<String, String> headerMap) {
        return wrapRequest(requestUrl, paramMap, headerMap, HttpCommonUtil.METHOD_POST, null);
    }

    public ResultResponse doGet(String requestUrl, Map<String, Object> paramMap, Map<String, String> headerMap) {
        return wrapRequest(requestUrl, paramMap, headerMap, HttpCommonUtil.METHOD_GET, null);
    }

    public ResultResponse doPost(String requestUrl, Map<String, Object> paramMap, Map<String, String> headerMap, String resultEncoding) {
        setResultCharset(resultEncoding);
        return wrapRequest(requestUrl, paramMap, headerMap, HttpCommonUtil.METHOD_POST, null);
    }

    public ResultResponse doGet(String requestUrl, Map<String, Object> paramMap, Map<String, String> headerMap, String resultEncoding) {
        setResultCharset(resultEncoding);
        return wrapRequest(requestUrl, paramMap, headerMap, HttpCommonUtil.METHOD_GET, null);
    }

    public ResultResponse doPostJson(String requestUrl, String paramJson) {

        return doPostJson(requestUrl, paramJson, null);
    }

    public ResultResponse doPostJson(String requestUrl, String paramJson, Map<String, String> headerMap) {

        return doPostJson(requestUrl, paramJson, headerMap, null);
    }

    public ResultResponse doPostJson(String requestUrl, String paramJson, Map<String, String> headerMap, String resultEncoding) {
        if (headerMap == null) {
            headerMap = new HashMap<>(16);
        }
        String contentType = StringUtils.isBlank(headerMap.get(HttpCommonUtil.CONTENT_TYPE)) ? headerMap.get(HttpCommonUtil.CONTENT_TYPE_1) : headerMap.get(HttpCommonUtil.CONTENT_TYPE);
        if (StringUtils.isBlank(contentType)) {
            headerMap.put(HttpCommonUtil.CONTENT_TYPE, HttpCommonUtil.CONTENT_TYPE_JSON);
        }
        if (StringUtils.isBlank(headerMap.get(HttpCommonUtil.ACCEPT))) {
            headerMap.put(HttpCommonUtil.ACCEPT, HttpCommonUtil.ACCEPT_CONTENT_TYPE);
        }
        setResultCharset(resultEncoding);
        return wrapRequest(requestUrl, null, headerMap, HttpCommonUtil.METHOD_POST, paramJson);
    }

    /**
     * 设置http/https,post/get
     */
    private ResultResponse wrapRequest(String requestUrl, Map<String, Object> paramMap, Map<String, String> headerMap, String requestMethod, String paramJson) {
        if (StringUtils.isBlank(requestUrl)) {
            logger.info("请求地址不能为空!requestUrl:【{}】", requestUrl);
            AppException.throwEx("请求地址不能为空!requestUrl:" + requestUrl);
        }
        requestMethod = StringUtils.isBlank(requestMethod) ? HttpCommonUtil.METHOD_POST : requestMethod;
        boolean methodPostFlag = !requestMethod.equalsIgnoreCase(HttpCommonUtil.METHOD_GET);
        boolean httpsFlag = requestUrl.startsWith(HttpCommonUtil.HTTPS);
        //当前是否有开启全局代理
        if (proxy == null && globalProxyFlag) {
            proxy = HttpGlobalConfig.getInstance().getHttpHostProxy();
        }
        return request(requestUrl, methodPostFlag, paramMap, headerMap, httpsFlag, paramJson);
    }

    private ResultResponse request(String requestUrl, boolean methodPostFlag, Map<String, Object> paramMap, Map<String, String> headerMap, boolean httpsFlag, String paramJson) {
        CloseableHttpResponse response = null;
        String paramCharset = getParamCharset();
        String resultEncoding = getResultCharset();
        try {
            HttpUriRequest httpRequest;
            List<NameValuePair> paramList = null;
            if (null != paramMap && !paramMap.isEmpty()) {
                paramList = new ArrayList<>();
                for (Map.Entry<String, Object> param : paramMap.entrySet()) {
                    paramList.add(new BasicNameValuePair(param.getKey(), param.getValue().toString()));
                }
            }
            if (methodPostFlag) {
                HttpPost httpPost = new HttpPost(requestUrl);
                if (null != paramList) {
                    HttpEntity httpEntity = new UrlEncodedFormEntity(paramList, paramCharset);
                    httpPost.setEntity(httpEntity);
                }
                if (StringUtils.isNotEmpty(paramJson)) {
                    StringEntity httpEntity = new StringEntity(paramJson, paramCharset);
                    httpEntity.setContentEncoding(paramCharset);
                    httpEntity.setContentType("text/json");
                    httpPost.setEntity(httpEntity);
                }
                if (requestConfig != null) {
                    httpPost.setConfig(requestConfig);
                }
                httpRequest = httpPost;
            } else {
                URIBuilder uriBuilder = new URIBuilder(requestUrl, Charset.forName(paramCharset));
                if (null != paramList) {
                    uriBuilder.setParameters(paramList);
                }
                HttpGet httpGet = new HttpGet(uriBuilder.build());
                if (requestConfig != null) {
                    httpGet.setConfig(requestConfig);
                }
                httpRequest = httpGet;
            }
            // 设置通用的请求属性
            if (usePropertyFlag) {
                if (headerMap == null) {
                    headerMap = new HashMap<>(2);
                }
                headerMap.putIfAbsent(HttpCommonUtil.ACCEPT, "*/*");
                headerMap.putIfAbsent(HttpCommonUtil.USER_AGENT, "Mozilla/5.0 (Windows NT 6.1; Win64; x64)");
            }
            //设置头部
            if (headerMap != null && headerMap.size() > 0) {
                for (Map.Entry<String, String> headerItem : headerMap.entrySet()) {
                    httpRequest.setHeader(headerItem.getKey(), headerItem.getValue());
                }
            }
            if (proxy != null) {
                if (httpClient != null) {
                    response = httpClient.execute(proxy, httpRequest);
                } else {
                    response = defaultHttpClient.execute(proxy, httpRequest);
                }
            } else {
                if (httpClient != null) {
                    response = httpClient.execute(httpRequest);
                } else {
                    response = defaultHttpClient.execute(httpRequest);
                }
            }
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            ResultResponse resultResponse = new ResultResponse(statusCode, statusLine.getReasonPhrase());
            resultResponse.setHeaderCookieField(defaultHeaderCookieField);
            resultResponse.setResultResponse(response);
            if (statusCode == getSuccessCode()) {
                HttpEntity entity = response.getEntity();
                if (resultToStringFlag) {
                    // 从输入流读取返回内容
                    resultResponse.setData(toInputStreamConvertString(entity, resultEncoding));
                }
            } else {
                resultResponse.setErrorData(toInputStreamConvertString(response.getEntity(), resultEncoding));
            }
            return resultResponse;
        } catch (IOException | URISyntaxException ce) {
            logger.error("url:【{}】,msg:【{}】", requestUrl, ce.getMessage(), ce);
            return HttpException.httpClientHandleException(ce);
        } finally {
            if (disconnectFlag && response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String toInputStreamConvertString(HttpEntity entity, String encoding) throws IOException {
        if (entity == null) {
            return null;
        }
        InputStream inputStream = entity.getContent();
        if (inputStream == null) {
            return null;
        }
        try {
            //如果指定编码使用指定编码
            if (StringUtils.isNotBlank(encoding)) {
                return StreamUtil.inputStreamToReaderString(inputStream, encoding);
            }
            //没有指定编码先从请求头获取
            ContentType contentType = ContentType.get(entity);
            if (contentType != null) {
                Charset charset = contentType.getCharset();
                if (charset != null) {
                    return StreamUtil.inputStreamToReaderString(inputStream, charset.name());
                }
            }
            //根据流获取编码
            return StreamUtil.inputStreamToArrayString(inputStream, encoding);
        } finally {
            try {
                inputStream.close();
            } catch (Exception ignored) {

            }
        }
    }

    public boolean isUsePropertyFlag() {
        return usePropertyFlag;
    }

    public HttpClientPoolUtil setUsePropertyFlag(boolean usePropertyFlag) {
        this.usePropertyFlag = usePropertyFlag;
        return this;
    }

    public String getParamCharset() {
        return StringUtils.isNotBlank(paramCharset) ? paramCharset : HttpCommonUtil.DEFAULT_ENCODING;
    }

    public HttpClientPoolUtil setParamCharset(String paramCharset) {
        if (StringUtils.isNotBlank(paramCharset)) {
            this.paramCharset = paramCharset;
        }
        return this;
    }

    public String getResultCharset() {
        return resultCharset;
    }

    public HttpClientPoolUtil setResultCharset(String resultCharset) {
        if (resultCharset != null) {
            this.resultCharset = resultCharset;
        }
        return this;
    }

    public HttpClientPoolUtil setResultToString(boolean flag) {
        this.resultToStringFlag = flag;
        return this;
    }

    public HttpClientPoolUtil setDisconnectFlag(boolean disconnectFlag) {
        this.disconnectFlag = disconnectFlag;
        return this;
    }

    public HttpClientPoolUtil setHeaderCookieField(String headerCookieField) {
        if (headerCookieField != null && headerCookieField.length() > 0) {
            this.defaultHeaderCookieField = headerCookieField;
        }
        return this;
    }

    public HttpHost getProxy() {
        return proxy;
    }

    public HttpClientPoolUtil setProxy(HttpHost proxy) {
        this.proxy = proxy;
        return this;
    }

    public boolean isGlobalProxyFlag() {
        return globalProxyFlag;
    }

    public HttpClientPoolUtil setGlobalProxyFlag(boolean globalProxyFlag) {
        this.globalProxyFlag = globalProxyFlag;
        return this;
    }

    public int getSuccessCode() {
        return successCode;
    }

    public HttpClientPoolUtil setSuccessCode(int successCode) {
        this.successCode = successCode;
        return this;
    }

    public boolean isEncodeParamKeyFlag() {
        return encodeParamKeyFlag;
    }

    public HttpClientPoolUtil setEncodeParamKeyFlag(boolean encodeParamKeyFlag) {
        this.encodeParamKeyFlag = encodeParamKeyFlag;
        return this;
    }

    public boolean isEncodeParamValueFlag() {
        return encodeParamValueFlag;
    }

    public HttpClientPoolUtil setEncodeParamValueFlag(boolean encodeParamValueFlag) {
        this.encodeParamValueFlag = encodeParamValueFlag;
        return this;
    }

    public RequestConfig getRequestConfig() {
        return requestConfig;
    }

    public HttpClientPoolUtil setRequestConfig(RequestConfig requestConfig) {
        this.requestConfig = requestConfig;
        return this;
    }

    public CloseableHttpClient getHttpClient() {
        return httpClient;
    }

    public HttpClientPoolUtil setHttpClient(CloseableHttpClient httpClient) {
        this.httpClient = httpClient;
        return this;
    }

    public static class ResultResponse implements Serializable {
        private static final long serialVersionUID = -884562198636894001L;
        private int status;
        private String msg;
        private String message;
        private String data;
        private transient String errorData;
        private transient String headerCookieField;
        private transient CloseableHttpResponse resultResponse;

        private ResultResponse() {

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
            return String.format("{\"status\":%s,\"message\":'%s',\"msg\":'%s',\"data\":'%s',\"errorData\":'%s'}", status, message, msg, data, errorData);
        }

        public boolean isSuccess() {
            return status == HttpCommonUtil.DEFAULT_SUCCESS_CODE;
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

        public String getErrorData() {
            return errorData;
        }

        public void setErrorData(String errorData) {
            this.errorData = errorData;
        }

        public CloseableHttpResponse getResultResponse() {
            return resultResponse;
        }

        public void setResultResponse(CloseableHttpResponse resultResponse) {
            this.resultResponse = resultResponse;
        }

        public Map<String, Header> getHeaders() {
            if (resultResponse == null) {
                return new LinkedHashMap<>(2);
            }
            Header[] allHeaders = resultResponse.getAllHeaders();
            if (allHeaders != null && allHeaders.length > 0) {
                // 请求头待处理
                Map<String, Header> headerFields = new LinkedHashMap<>(allHeaders.length);
                for (Header header : allHeaders) {
                    headerFields.put(header.getName(), header);
                }
                return headerFields;
            }
            return new LinkedHashMap<>(2);
        }

        /**
         * 在Header里获取所有cookie
         * 可能会包含:[Domain,Expires,Path,HttpOnly]等信息
         * 处理后的:cookies()
         * headerCookieField:默认Set-Cookie
         *
         * @return List
         */
        public Header getHeaderCookies() {
            return getHeaderCookies(getHeaderCookieField());
        }

        public Header getHeaderCookies(String headerCookieField) {
            if (headerCookieField == null || headerCookieField.isEmpty() || resultResponse == null) {
                return null;
            }
            return getHeaders().get(headerCookieField);
        }

        /**
         * 获取cookie,Header:name,value
         *
         * @return map(key, value)
         */
        public Map<String, String> getCookies() {
            Header headerCookie = getHeaderCookies();
            if (headerCookie == null) {
                return Collections.emptyMap();
            }
            HeaderElement[] cookies = headerCookie.getElements();
            Map<String, String> cookieMap = new LinkedHashMap<>(cookies.length);
            for (HeaderElement cookieData : cookies) {
                cookieMap.put(cookieData.getName(), cookieData.getValue());
            }
            return cookieMap;
        }

        /**
         * cookie转成String
         *
         * @return String
         */
        public String getCookiesToString() {
            Header headerCookie = getHeaderCookies();
            if (headerCookie == null) {
                return null;
            }
            StringBuilder stringBuilder = new StringBuilder();
            HeaderElement[] cookies = headerCookie.getElements();
            if (cookies != null && cookies.length > 0) {
                for (HeaderElement cookieData : cookies) {
                    stringBuilder.append(cookieData.getName()).append("=").append(cookieData.getValue()).append(';');
                }
            }
            return stringBuilder.toString();
        }

        public void close() {
            if (resultResponse != null) {
                try {
                    resultResponse.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        public String getHeaderCookieField() {
            return headerCookieField;
        }

        public void setHeaderCookieField(String headerCookieField) {
            if (StringUtils.isNotBlank(headerCookieField)) {
                this.headerCookieField = headerCookieField;
            }
        }
    }

}
