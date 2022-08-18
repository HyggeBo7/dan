package top.dearbo.util.xt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * properties util
 *
 * @author xuxueli 2015-6-22 22:36:46
 */
public class PropertiesUtil {
	private static Logger logger = LoggerFactory.getLogger(PropertiesUtil.class);
	public static final String DEFAULT_CONFIG = "config.properties";

	/**
	 * load prop file
	 *
	 * @param propertyFileName
	 * @return
	 */
	public static Properties loadProperties(String propertyFileName) {
		Properties prop = new Properties();
		InputStream in = null;
		try {
			// 方式1：配置更新不需要重启JVM
			URL url = Thread.currentThread().getContextClassLoader().getResource(propertyFileName);
			assert url != null;
			in = Files.newInputStream(Paths.get(URLDecoder.decode(url.getPath(), "utf-8")));
			// in = loder.getResourceAsStream(propertyFileName); // 方式2：配置更新需重启JVM
			prop.load(in);
		} catch (IOException e) {
			logger.error("load {} error!", propertyFileName);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					logger.error("close {} error!", propertyFileName);
				}
			}
		}
		return prop;
	}

	/**
	 * load prop value of string
	 *
	 * @param key
	 * @return
	 */
	public static String getString(Properties prop, String key) {
		return prop.getProperty(key);
	}

	/**
	 * load prop value of int
	 *
	 * @param key
	 * @return
	 */
	public static int getInt(Properties prop, String key) {
		return Integer.parseInt(getString(prop, key));
	}

	/**
	 * load prop value of boolean
	 *
	 * @param prop
	 * @param key
	 * @return
	 */
	public static boolean getBoolean(Properties prop, String key) {
		return Boolean.valueOf(getString(prop, key));
	}


	/**
	 * 写入properties信息
	 *
	 * @param filePath       路径
	 * @param parameterName  key
	 * @param parameterValue 值
	 */
	public static void writeProperties(String filePath, String parameterName, String parameterValue) {
		Properties prop = new Properties();

		try {
			filePath = URLDecoder.decode(filePath, "utf-8");
			InputStream fis = new FileInputStream(filePath);
			//从输入流中读取属性列表（键和元素对）
			prop.load(fis);
			//调用 Hashtable 的方法 put。使用 getProperty 方法提供并行性,强制要求为属性的键和值使用字符串。返回值是 Hashtable 调用 put 的结果。
			OutputStream fos = new FileOutputStream(filePath);
			prop.setProperty(parameterName, parameterValue);
			//以适合使用 load 方法加载到 Properties 表中的格式，将此 Properties 表中的属性列表（键和元素对）写入输出流
			prop.store(fos, "Update '" + parameterName + "' value");
		} catch (IOException e) {
			logger.error("Visit " + filePath + " for updating " + parameterName + " value error");
		}
	}


}
