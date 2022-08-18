package top.dearbo.util.network.common;

import org.apache.http.HttpHost;

import java.net.Proxy;

/**
 * @version 1.0
 * @author: Bo
 * @fileName: HttpGlobalConfig
 * @createDate: 2020-06-13 10:59.
 * @description: 全局配置
 */
public class HttpGlobalConfig {
	private volatile static HttpGlobalConfig httpGlobalConfig;
	private Proxy globalProxy;
	private HttpHost globalHttpHost;

	public Proxy setProxy(Proxy proxy) {
		if (proxy != null) {
			globalProxy = proxy;
		}
		return globalProxy;
	}

	public Proxy getProxy() {
		return globalProxy;
	}

	public HttpHost setHttpHostProxy(HttpHost proxy) {
		if (proxy != null) {
			globalHttpHost = proxy;
		}
		return globalHttpHost;
	}

	public HttpHost getHttpHostProxy() {
		return globalHttpHost;
	}

	public static HttpGlobalConfig getInstance() {
		if (httpGlobalConfig == null) {
			synchronized (HttpGlobalConfig.class) {
				if (httpGlobalConfig == null) {
					httpGlobalConfig = new HttpGlobalConfig();
				}
			}
		}
		return httpGlobalConfig;
	}

	private HttpGlobalConfig() {
	}

}
