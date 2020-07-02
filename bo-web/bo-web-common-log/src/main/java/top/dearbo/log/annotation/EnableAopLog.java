package top.dearbo.log.annotation;

import org.springframework.context.annotation.Import;
import top.dearbo.log.LogConfiguration;

import java.lang.annotation.*;

/**
 * @version 1.0
 * @author: Bo
 * @fileName: EnableAopLog
 * @createDate: 2020-07-02 09:35.
 * @description: 开启Aop日志
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({LogConfiguration.class})
public @interface EnableAopLog {
}
