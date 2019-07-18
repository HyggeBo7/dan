package com.dan.web.common.springmvc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.List;

/**
 * 返回json时使用, 支持json , jsonp, 需配合RequestContext 使用, 注意
 * <p>
 * Created by dan
 */
public class JsonMessageConverter implements HttpMessageConverter {

    private static final Logger logger = LoggerFactory.getLogger(JsonMessageConverter.class);

    private JsonCommonRender jsonCommonRender;

    public JsonMessageConverter() {
        this(new JsonCommonRender());
    }

    public JsonMessageConverter(JsonCommonRender jsonCommonRender) {
        this.jsonCommonRender = jsonCommonRender;
    }

    /**
     * 该转换器的支持类型：application/json
     */
    private List<MediaType> supportedMediaTypes = Collections.singletonList(MediaType.APPLICATION_JSON_UTF8);

    @Override
    public boolean canRead(Class aClass, MediaType mediaType) {
        if (mediaType == null) {
            return true;
        }
        for (MediaType supportedMediaType : getSupportedMediaTypes()) {
            if (supportedMediaType.includes(mediaType)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean canWrite(Class aClass, MediaType mediaType) {
        if (mediaType == null || MediaType.ALL.equals(mediaType)) {
            return true;
        }
        for (MediaType supportedMediaType : getSupportedMediaTypes()) {
            if (supportedMediaType.includes(mediaType)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<MediaType> getSupportedMediaTypes() {
        return supportedMediaTypes;
    }

    @Override
    public Object read(Class aClass, HttpInputMessage httpInputMessage) throws IOException, HttpMessageNotReadableException {

        return null;
    }

    @Override
    public void write(Object o, MediaType mediaType, HttpOutputMessage httpOutputMessage) throws IOException, HttpMessageNotWritableException {
        try {
            OutputStream out = httpOutputMessage.getBody();
            byte[] bytes = jsonCommonRender.getJsonResult(o).getBytes(Charset.forName("UTF-8"));
            out.write(bytes);
        } catch (IllegalStateException e) {
            logger.info("httpOutputMessage.getBody():" + e.getMessage());
            logger.error("httpOutputMessage.getBody():" + e);
        }
    }

}
