package top.dearbo.util.lang;

import org.apache.commons.lang3.StringUtils;
import top.dearbo.base.bean.BaseEnum;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Bo
 * @date 2020/5/28
 */
public class EnumUtil {

	/*public static void main(String[] args) {
		CommonStatusEnum[] values = CommonStatusEnum.values();
		CommonStatusEnum commonStatusEnum1 = EnumUtil.getEnumByName(values, "SUCCESS");
		CommonStatusEnum commonStatusEnum2 = EnumUtil.getEnumByKey(values, 1);
		CommonStatusEnum commonStatusEnum3 = EnumUtil.getEnumByValue(values, "成功");
		CommonStatusEnum commonStatusEnum4 = EnumUtil.getEnumByName(CommonStatusEnum.class, "FAIL");
		CommonStatusEnum commonStatusEnum5 = EnumUtil.getEnumByKey(CommonStatusEnum.class, -1);
		CommonStatusEnum commonStatusEnum6 = EnumUtil.getEnumByValue(CommonStatusEnum.class, "逻辑错误");
		System.out.println("=============");
	}*/

	public static <E extends BaseEnum<?, ?>> E getEnumByName(Class<E> clazz, String enumName) {
		if (StringUtils.isNotBlank(enumName) && clazz.isEnum()) {
			return getEnumByName(clazz.getEnumConstants(), enumName);
		}
		return null;
	}

	public static <E extends BaseEnum<K, ?>, K> E getEnumByKey(Class<E> clazz, K enumKey) {
		if (enumKey != null && clazz.isEnum()) {
			return getEnumByKey(clazz.getEnumConstants(), enumKey);
		}
		return null;
	}

	public static <E extends BaseEnum<?, V>, V> E getEnumByValue(Class<E> clazz, V enumValue) {
		if (enumValue != null && clazz.isEnum()) {
			return getEnumByValue(clazz.getEnumConstants(), enumValue);
		}
		return null;
	}

	public static <T extends BaseEnum<?, ?>> T getEnumByName(T[] enumData, String enumName) {
		if (StringUtils.isNotBlank(enumName)) {
			for (T item : enumData) {
				if (item.getName().equals(enumName)) {
					return item;
				}
			}
		}
		return null;
	}

	public static <T extends BaseEnum<?, V>, V> T getEnumByValue(T[] enumData, V enumValue) {
		if (enumValue != null) {
			for (T item : enumData) {
				if (item.getValue().equals(enumValue)) {
					return item;
				}
			}
		}
		return null;
	}

	public static <T extends BaseEnum<K, ?>, K> T getEnumByKey(T[] enumData, K enumKey) {
		if (enumKey != null) {
			for (T item : enumData) {
				if (enumKey.equals(item.getKey())) {
					return item;
				}
			}
		}
		return null;
	}

	public static <T extends BaseEnum<K, ?>, K> List<T> getEnumByKeys(T[] enumData, List<K> enumKeys) {
		List<T> enums = new ArrayList<>();
		if (enumKeys != null && !enumKeys.isEmpty()) {
			for (K enumKey : enumKeys) {
				T t = getEnumByKey(enumData, enumKey);
				if (t != null) {
					enums.add(t);
				}
			}
		}
		return enums;
	}

}
