package top.dearbo.web.springmvc;

import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import top.dearbo.base.bean.BaseResult;
import top.dearbo.util.data.JacksonUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

/**
 * 自定义Jackson不序列化null值字段
 * <pre>
 *  @Bean
 *  public MappingJackson2HttpMessageConverter mappingJacksonHttpMessageConverter() {
 * 		return new CustomMappingJackson2HttpMessageConverter();
 *  }
 * </pre>
 *
 * @author wb
 * @date 2022-07-06.
 */
public class CustomMappingJackson2HttpMessageConverter extends MappingJackson2HttpMessageConverter {
	/**
	 * 该转换器的支持类型：application/json
	 * MediaType contentType = outputMessage.getHeaders().getContentType();
	 */
	//private List<MediaType> supportedMediaTypes = Collections.singletonList(MediaType.APPLICATION_JSON);
	@Override
	protected void writeInternal(Object object, Type type, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
		if (object != null && object instanceof BaseResult) {
			try {
				String result = getSerializationData(object, (BaseResult) object);
				if (result != null) {
					byte[] bytes = result.getBytes(StandardCharsets.UTF_8);
					OutputStream out = outputMessage.getBody();
					out.write(bytes);
					out.flush();
					return;
				}
			} catch (Exception ignored) {
			}
		}
		super.writeInternal(object, type, outputMessage);
	}

	/**
	 * null 走默认序列化
	 *
	 * @param baseResult
	 * @return String
	 */
	public String getSerializationData(Object original, BaseResult baseResult) {
		if (!baseResult.resultSerializeNullField()) {
			return JacksonUtils.toJsonIgnoreNull(baseResult);
		}
		return null;
	}

}
