package com.dan.common.util.pdf;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

/**
 * @fileName: FreemarkerUtils.java
 * @author: Dan
 * @createDate: 2018/3/29 12:24
 * @description: freemarker工具类
 */
public class FreemarkerUtils {

    /**
     * 使用传入的Map对象，渲染指定的freemarker模板
     * @param baseDir   模板目录-根目录
     * @param fileName  文件名
     * @param globalMap 数据绑定
     * @return html字符串
     */
    public static String loadFtlHtml(File baseDir, String fileName, Map globalMap) {
        if (baseDir == null || !baseDir.isDirectory() || StringUtils.isBlank(fileName)) {
            throw new IllegalArgumentException("模板目录不存在或模板名称不存在!");
        }
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_23);
        try {
            cfg.setDirectoryForTemplateLoading(baseDir);
            cfg.setDefaultEncoding("UTF-8");
            cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);//.RETHROW
            cfg.setClassicCompatible(true);
            Template temp = cfg.getTemplate(fileName);

            StringWriter stringWriter = new StringWriter();
            temp.process(globalMap, stringWriter);

            return stringWriter.toString();
        } catch (IOException | TemplateException e) {
            e.printStackTrace();
            throw new RuntimeException(fileName + " 模板文件不存在!");
        }
    }

    public static String loadFtlHtml(String baseDir, String fileName, Map globalMap) {
        return loadFtlHtml(new File(baseDir), fileName, globalMap);
    }
}
