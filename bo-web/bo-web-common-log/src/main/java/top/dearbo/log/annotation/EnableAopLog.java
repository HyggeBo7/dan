package top.dearbo.log.annotation;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @version 1.0
 * @author: Bo
 * @fileName: EnableAopLog
 * @createDate: 2020-07-02 09:35.
 * @description: 开启Aop日志
 * value可通过 web.config.aopLog.enabled 配置是否开启Aop
 * value和valueProperty方式选择其一
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({EnableAopLogImportSelector.class})
public @interface EnableAopLog {

    /**
     * 是否开启日志
     *
     * @return boolean
     */
    boolean value() default true;

    /**
     * 是否异步执行
     *
     * @return boolean
     */
    boolean async() default true;

    /**
     * 是否开启日志-从配置资源文件里面获取值
     *
     * @return String
     */
    String valueProperty() default "";

    /**
     * valueProperty 获取值为空是,取当前默认值
     *
     * @return boolean
     */
    boolean defaultValueProperty() default false;

}
