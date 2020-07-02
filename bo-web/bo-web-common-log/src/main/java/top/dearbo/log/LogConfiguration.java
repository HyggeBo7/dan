package top.dearbo.log;

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import top.dearbo.log.aspect.SysLogAspect;

/**
 * @version 1.0
 * @author: Bo
 * @fileName: LogAutoConfiguration
 * @createDate: 2020-07-01 18:36.
 * @description: 日志开启
 */
@EnableAsync
@ConditionalOnWebApplication
@Configuration(proxyBeanMethods = false)
public class LogConfiguration {

    @Bean
    public SysLogAspect sysLogAspect() {
        return new SysLogAspect();
    }

    /*@Bean
    @Order(-1)
    public SysLogConfig sysLogConfig() {
        return new DefaultSysLogConfig();
    }*/
}
