package com.dan.web.common.web;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/1/26.
 */
public class HttpWebClient {

    public static final Logger logger = LoggerFactory.getLogger(HttpWebClient.class);

    /**
     * 每个路由的默认最大连接数
     */
    public static final int DEFAULT_MAX_PER_ROUTE = 50;
    /**
     * 连接池里的最大连接数
     */
    public static final int DEFAULT_MAX_TOTAL = 100;

    public static final String DEFAULT_ENCODING = "UTF-8";

    public static final CloseableHttpClient httpClient;

    static {
        PoolingHttpClientConnectionManager poolingClientConnectionManager = new PoolingHttpClientConnectionManager();
        poolingClientConnectionManager.setDefaultMaxPerRoute(DEFAULT_MAX_PER_ROUTE);
        poolingClientConnectionManager.setMaxTotal(DEFAULT_MAX_TOTAL);

        httpClient = HttpClients.custom().setConnectionManager(poolingClientConnectionManager).build();
    }

    /**
     * 带参数的Http-Post请求
     */
    public static String post(String remoteUrl, Map<String, String> params) {

        if (StringUtils.isEmpty(remoteUrl)) {
            throw new RuntimeException("post remoteUrl not null !");
        }

        logger.info("http client post method, url：{}, params:{}", remoteUrl, params);

        String result = null;
        try {
            HttpPost httpPost = new HttpPost(remoteUrl);
            List<NameValuePair> nameValuePairList = null;
            if (params != null && params.size() > 0) {
                nameValuePairList = new ArrayList<NameValuePair>();

                for (Iterator<Map.Entry<String, String>> entryIterator = params.entrySet().iterator(); entryIterator.hasNext(); ) {
                    Map.Entry<String, String> entry = entryIterator.next();
                    String key = entry.getKey();
                    String value = entry.getValue();
                    nameValuePairList.add(new BasicNameValuePair(key, value));

                }

                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairList, DEFAULT_ENCODING));
            }

            HttpClient httpClient = getHttpClient();
            HttpResponse response = httpClient.execute(httpPost);
            result = EntityUtils.toString(response.getEntity());

            logger.info("http client post method, response:{}", result);
        } catch (Exception e) {
            logger.error("http client post error , e {}", e);
            throw new RuntimeException("connect server error!");
        }
        return result;
    }

    /**
     * 无参数的Http-Post请求
     */
    public static String post(String remoteUrl) {
        return post(remoteUrl, null);
    }

    /**
     * 无参数或remoteUrl已带参数的Http-Post请求
     */
    public static String get(String url) {
        return get(url, null);
    }

    public static String get(String url, Map<String, String> headers) {
        String result = "";
        HttpGet httpGet = new HttpGet(url);

        if (headers != null && headers.size() > 0) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                httpGet.setHeader(entry.getKey(), entry.getValue());
            }
        }

        try {
            HttpResponse response = getHttpClient().execute(httpGet);
            result = EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            logger.error("execute get error , url : {}, exception : {}", url, e);
        } finally {
            httpGet.releaseConnection();
        }
        return result;
    }

    public static String jsonGet(String url) {
        String result = "";
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("X-Requested-With", "XMLHttpRequest");
        try {
            HttpResponse response = getHttpClient().execute(httpGet);
            result = EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            logger.error("execute get error , url : {}, exception : {}", url, e);
        } finally {
            httpGet.releaseConnection();
        }
        return result;
    }

    public static InputStream downLoadFile(String url) {
        InputStream input = null;
        HttpGet httpGet = new HttpGet(url);
        try {
            HttpResponse response = getHttpClient().execute(httpGet);
            input = response.getEntity().getContent();
        } catch (IOException e) {
            logger.error("execute get error , url : {}, exception : {}", url, e);
        } finally {
            httpGet.releaseConnection();
        }
        return input;
    }

    public static HttpClient getHttpClient() {
        return httpClient;
    }

}
