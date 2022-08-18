package top.dearbo.log.annotation;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import top.dearbo.log.LogConfiguration;
import top.dearbo.log.aspect.SysLogAspect;

import java.util.ArrayList;
import java.util.List;

/**
 * @version 1.0
 * @author: Bo
 * @fileName: EnableAopLogImportSelector
 * @createDate: 2020-07-03 11:17.
 * @description: 判断是否开启
 */
public class EnableAopLogImportSelector implements ImportSelector, EnvironmentAware {

    private final String annotationClass = EnableAopLog.class.getName();
    private volatile Environment environment;

    @Override
    public String[] selectImports(AnnotationMetadata annotationMetadata) {
        AnnotationAttributes annotationAttributes = AnnotationAttributes.fromMap(annotationMetadata.getAnnotationAttributes(annotationClass, true));
        if (annotationAttributes != null) {
            boolean propertyEnabled = false;
            boolean valueEnabled = false;
            //获取valueProperty,如果不为空,就去配置文件查找
            String valueProperty = annotationAttributes.getString("valueProperty");
            if (StringUtils.isNotBlank(valueProperty)) {
                String property = environment.getProperty(valueProperty);
                propertyEnabled = property == null ? annotationAttributes.getBoolean("defaultValueProperty") : BooleanUtils.toBoolean(property);
            } else {
                valueEnabled = environment.getProperty("global.config.aopLog.enabled", Boolean.class, Boolean.TRUE);
                if (valueEnabled) {
                    valueEnabled = annotationAttributes.getBoolean("value");
                }
            }
            if (propertyEnabled || valueEnabled) {
                List<String> importsList = new ArrayList<>();
                importsList.add(SysLogAspect.class.getName());
                //是否开启异步执行
                boolean async = annotationAttributes.getBoolean("importAsync");
                if (async) {
                    importsList.add(LogConfiguration.class.getName());
                }
                return importsList.toArray(new String[0]);
            }
        }
        return new String[0];
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

}
