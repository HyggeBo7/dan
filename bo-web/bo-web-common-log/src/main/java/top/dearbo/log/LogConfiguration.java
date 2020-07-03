package top.dearbo.log;

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @version 1.0
 * @author: Bo
 * @fileName: LogAutoConfiguration
 * @createDate: 2020-07-01 18:36.
 * @description: 日志开启
 * @ConditionalOnWebApplication 要是web应用才会注入
 */
@EnableAsync
@ConditionalOnWebApplication
@Configuration(proxyBeanMethods = false)
public class LogConfiguration {

}
