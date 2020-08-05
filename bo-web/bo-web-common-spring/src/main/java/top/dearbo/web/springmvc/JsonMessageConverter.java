package top.dearbo.web.springmvc;

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
import java.nio.charset.StandardCharsets;
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
    private List<MediaType> supportedMediaTypes = Collections.singletonList(MediaType.APPLICATION_JSON);

    /**
     * 不处理请求json参数@RequestBody
     *
     * @param aClass    class
     * @param mediaType 类型
     * @return false
     */
    @Override
    public boolean canRead(Class aClass, MediaType mediaType) {
        /*if (mediaType == null) {
            return true;
        }
        for (MediaType supportedMediaType : getSupportedMediaTypes()) {
            if (supportedMediaType.includes(mediaType)) {
                return true;
            }
        }*/
        return false;
    }

    @Override
    public Object read(Class aClass, HttpInputMessage httpInputMessage) throws IOException, HttpMessageNotReadableException {
        return null;
    }

    @Override
    public List<MediaType> getSupportedMediaTypes() {
        return supportedMediaTypes;
    }

    /**
     * 根据类型判断是否处理(当前只处理json)
     *
     * @param aClass    class
     * @param mediaType 类型
     * @return true/false
     */
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
    public void write(Object o, MediaType mediaType, HttpOutputMessage httpOutputMessage) throws IOException, HttpMessageNotWritableException {
        OutputStream out = httpOutputMessage.getBody();
        byte[] bytes = jsonCommonRender.getJsonResult(o).getBytes(StandardCharsets.UTF_8);
        out.write(bytes);
        out.flush();
    }

}
