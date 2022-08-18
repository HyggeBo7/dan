package top.dearbo.web.springmvc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.dearbo.web.springmvc.exception.GlobalWebExceptionHandler;
import top.dearbo.web.springmvc.exception.WebExceptionHandlerResolver;

/**
 * web 自动装载bean
 * ConditionalOnMissingBean 存在就不注入
 */
@Configuration
@ConditionalOnWebApplication
public class WebAutoConfiguration {
	private static final Logger log = LoggerFactory.getLogger(WebAutoConfiguration.class);

	@Bean
	@ConditionalOnMissingBean(GlobalWebExceptionHandler.class)
	public GlobalWebExceptionHandler globalWebExceptionHandler() {
		log.info("Initializing AutoConfig GlobalWebExceptionHandler");
		return new GlobalWebExceptionHandler();
	}

	@Bean
	@ConditionalOnMissingBean(WebExceptionHandlerResolver.class)
	public WebExceptionHandlerResolver webExceptionHandlerResolver() {
		log.info("Initializing AutoConfig WebExceptionHandlerResolver");
		return new WebExceptionHandlerResolver();
	}

}