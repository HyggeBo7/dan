package top.dearbo.util.network.common;

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

    public Proxy setProxy(Proxy proxy) {
        if (proxy != null) {
            globalProxy = proxy;
        }
        return globalProxy;
    }

    public Proxy getProxy() {
        return globalProxy;
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
