package top.dearbo.web.core.util.result;

import org.apache.commons.lang3.StringUtils;
import top.dearbo.util.lang.DateUtil;

import java.util.Date;

/**
 * @author wb
 */
public class ParamUtils {

	public static boolean verifyParam(Short value) {
		return value != null && value > 0;
	}

	public static boolean verifyParam(Integer value) {
		return value != null && value > 0;
	}

	public static boolean verifyParam(Long value) {
		return value != null && value > 0L;
	}

	public static boolean verifyParam(String value) {
		return StringUtils.isNotBlank(value);
	}

	public static boolean verifyParam(Date value) {
		return value != null;
	}

	public static boolean verifyParam(Boolean value) {
		return value != null;
	}

	public static Date getStartTime(Date date) {
		return DateUtil.getStartDate(date);
	}

	public static Date getEndTime(Date date) {
		return DateUtil.getEndDate(date);
	}

}
