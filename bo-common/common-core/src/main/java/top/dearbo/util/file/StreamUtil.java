package top.dearbo.util.file;

import org.apache.commons.lang3.StringUtils;
import org.mozilla.universalchardet.UniversalDetector;
import top.dearbo.util.exception.AppException;

import java.io.*;

/**
 * @fileName: StreamUtil
 * @author: bo
 * @createDate: 2019-06-11 10:34.
 * @description: 流
 */
public class StreamUtil {

	/**
	 * 默认编码
	 */
	private static final String DEFAULT_ENCODING = "UTF-8";

	/**
	 * 获取byte编码类型
	 *
	 * @param bytes 文件bytes数组
	 * @return 编码类型
	 */
	public static String getByteEncoding(byte[] bytes, String defaultEncoding) {
		UniversalDetector detector = new UniversalDetector(null);
		detector.handleData(bytes, 0, bytes.length);
		detector.dataEnd();
		String encoding = detector.getDetectedCharset();
		detector.reset();
		if (StringUtils.isBlank(encoding)) {
			encoding = defaultEncoding;
		}
		return encoding;
	}

	/**
	 * 将InputStream转换为字符串
	 *
	 * @param is InputStream
	 * @return String
	 */
	public static String inputStreamToArrayString(InputStream is) throws IOException {
		return inputStreamToArrayString(is, DEFAULT_ENCODING);
	}

	/**
	 * 将InputStream转换为字符串(会自动识别编码,识别不了默认 UTF-8 )
	 *
	 * @param is InputStream
	 * @return String
	 */
	public static String inputStreamToArrayString(InputStream is, String defaultEncoding) throws IOException {
		ByteArrayOutputStream boa = new ByteArrayOutputStream();
		int len;
		byte[] buffer = new byte[1024];
		while ((len = is.read(buffer)) != -1) {
			boa.write(buffer, 0, len);
		}
		byte[] result = boa.toByteArray();
		String currentEncoding = getByteEncoding(result, StringUtils.isBlank(defaultEncoding) ? DEFAULT_ENCODING : defaultEncoding);
		return new String(result, currentEncoding);
	}

	public static String inputStreamToReaderString(InputStream inputStream, String encoding) throws IOException {
		return inputStreamToReaderString(inputStream, encoding, false);
	}

	/**
	 * 流转字符
	 *
	 * @param inputStream 流
	 * @param encoding    编码
	 * @return String
	 * @throws IOException
	 */
	public static String inputStreamToReaderString(InputStream inputStream, String encoding, boolean closeStream) throws IOException {
		if (inputStream == null) {
			return null;
		}
		BufferedReader in = null;
		StringBuilder resultBuffer = new StringBuilder();
		try {
			in = new BufferedReader(new InputStreamReader(inputStream, StringUtils.isBlank(encoding) ? DEFAULT_ENCODING : encoding));
			String str;
			while ((str = in.readLine()) != null) {
				resultBuffer.append(str);
			}
			return resultBuffer.toString();
		} finally {
			if (closeStream && null != in) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 复制InputStream
	 *
	 * @param is InputStream
	 * @return
	 */
	public static InputStream cloneInputStream(InputStream is) {
		if (null == is) {
			AppException.throwEx("无法获取文件流，文件不可用！");
		}
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len;
			while ((len = is.read(buffer)) > -1) {
				baos.write(buffer, 0, len);
			}
			baos.flush();
			return new ByteArrayInputStream(baos.toByteArray());
		} catch (IOException e) {
			throw new AppException("无法复制当前文件流！", e);
		}
	}
}
