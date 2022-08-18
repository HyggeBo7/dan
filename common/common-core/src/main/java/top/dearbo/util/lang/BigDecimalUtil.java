package top.dearbo.util.lang;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * @author chenyikun
 * @Description:
 * @date 2021/5/28 15:29
 */
public class BigDecimalUtil {

	private static final BigDecimal DEFAULT_VALUE = BigDecimal.ZERO;
	private static final Integer INT_ZERO = 0;
	private static final Integer INT_FOUR = 4;

	private BigDecimalUtil() {
	}

	/**
	 * 验证一个数是否在目标值浮动值范围内(上下)
	 *
	 * @param value      值
	 * @param floatValue 浮动值(百分比)
	 * @return boolean
	 */
	public static boolean verifyFloatRange(BigDecimal value, BigDecimal targetValue, BigDecimal floatValue) {
		boolean verifyNull = verifyNull(value, targetValue, floatValue);
		if (verifyNull) {
			return false;
		}
		BigDecimal beginValue = getValueFloat(targetValue, floatValue, false);
		BigDecimal endValue = getValueFloat(targetValue, floatValue, true);
		return value.compareTo(beginValue) >= 0 && value.compareTo(endValue) < 1;
	}

	/**
	 * 获取一个数的浮动值
	 *
	 * @param value      原值
	 * @param floatValue 浮动百分比
	 * @param upDownFlag true:向上,false:向下
	 * @return 浮动后的值
	 */
	public static BigDecimal getValueFloat(BigDecimal value, BigDecimal floatValue, boolean upDownFlag) {
		if (value == null || value.compareTo(DEFAULT_VALUE) == 0) {
			return DEFAULT_VALUE;
		}
		if (floatValue == null || floatValue.compareTo(DEFAULT_VALUE) == 0) {
			return value;
		}
		BigDecimal multiply = value.multiply(floatValue);
		return upDownFlag ? value.add(multiply) : value.subtract(multiply);
	}

	/**
	 * 根据key累计value
	 *
	 * @param map
	 * @param key
	 * @param value
	 */
	public static <T> void addValueOfKey(Map<T, BigDecimal> map, T key, BigDecimal value) {
		if (!map.containsKey(key)) {
			map.put(key, DEFAULT_VALUE);
		}
		map.put(key, addBigDecimal(map.get(key), value));
	}

	/**
	 * 根据key累计value
	 *
	 * @param map
	 * @param key
	 * @param value
	 */
	public static <T> void addValueOfKey(Map<T, Set<String>> map, T key, String value) {
		Set<String> strings = map.computeIfAbsent(key, v -> new HashSet<>());
		strings.add(value);
	}

	/**
	 * 一个值为 null或者 0返回 0
	 *
	 * @param oneValue 值1
	 * @param values   n个值
	 * @return 结果
	 */
	public static BigDecimal multiplyBigDecimal(BigDecimal oneValue, BigDecimal... values) {
		return multiplyBigDecimal(oneValue, true, values);
	}

	/**
	 * n个数相乘,如果一个值存在 0或者 null,根据defaultZeroFlag判断
	 *
	 * @param oneValue        第一个值
	 * @param defaultZeroFlag 如果一个值为null是否返回 0
	 * @return 结果
	 */
	public static BigDecimal multiplyBigDecimal(BigDecimal oneValue, boolean defaultZeroFlag, BigDecimal... values) {
		if (oneValue == null || oneValue.compareTo(DEFAULT_VALUE) == INT_ZERO) {
			return oneValue == null && defaultZeroFlag ? DEFAULT_VALUE : oneValue;
		}
		Boolean verifyNullOrZero = verifyNullOrZero(values);
		if (BooleanUtils.isFalse(verifyNullOrZero)) {
			for (BigDecimal value : values) {
				oneValue = oneValue.multiply(value);
			}
			return oneValue;
		}
		if (verifyNullOrZero == null && !defaultZeroFlag) {
			return null;
		}
		return DEFAULT_VALUE;
	}


	/**
	 * 计算 n个BigDecimal的和
	 *
	 * @param nums
	 * @return (所有不为null的num的和)
	 */
	public static BigDecimal addBigDecimal(BigDecimal... nums) {
		BigDecimal result = DEFAULT_VALUE;
		for (BigDecimal num : nums) {
			if (num != null) {
				result = result.add(num);
			}
		}
		return result;
	}

	/**
	 * 计算BigDecimal的和,如果为null当成 0处理
	 *
	 * @param num1 值1
	 * @param num2 值2
	 * @return 结果
	 */
	public static BigDecimal addBigDecimalIgnoreNull(BigDecimal num1, BigDecimal num2) {
		return addBigDecimal(num1, num2);
	}

	/**
	 * 计算 BigDecimal的差
	 *
	 * @param num          为 null时默认为 0
	 * @param subtractNums
	 * @return
	 */
	public static BigDecimal subtractBigDecimal(BigDecimal num, BigDecimal... subtractNums) {
		num = initBigDecimal(num);
		for (BigDecimal subtractNum : subtractNums) {
			if (subtractNum != null) {
				num = num.subtract(subtractNum);
			}
		}
		return num;
	}

	/**
	 * 计算 n个BigDecimal的积
	 *
	 * @param flag         为 true时 null和 0参与计算 反之不参与计算
	 * @param subtractNums
	 * @return 计算值中有 null或者 0时 返回 0
	 */
	public static BigDecimal multiplyBigDecimal(boolean flag, BigDecimal... subtractNums) {
		BigDecimal result = DEFAULT_VALUE;
		for (BigDecimal subtractNum : subtractNums) {
			if (subtractNum == null || subtractNum.compareTo(DEFAULT_VALUE) == INT_ZERO) {
				if (flag) {
					continue;
				} else {
					return DEFAULT_VALUE;
				}
			}
			result = result.multiply(subtractNum);
		}
		return result;
	}

	public static BigDecimal divideBigDecimalIgnoreNull(BigDecimal beDivideNum, BigDecimal divideNum) {
		return divideBigDecimal(beDivideNum, divideNum);
	}

	/**
	 * 计算 BigDecimal的商 (保留4位小数, 四舍五入)
	 *
	 * @param beDivideNum 被除数 (为null时初始化为0)
	 * @param divideNums  除数
	 * @return 商 (除数为 0或者 null时返回 0)
	 */
	public static BigDecimal divideBigDecimal(BigDecimal beDivideNum, BigDecimal... divideNums) {
		beDivideNum = initBigDecimal(beDivideNum);
		for (BigDecimal divideNum : divideNums) {
			if (divideNum == null || divideNum.compareTo(DEFAULT_VALUE) == INT_ZERO) {
				return DEFAULT_VALUE;
			}
			beDivideNum = beDivideNum.divide(divideNum, INT_FOUR, RoundingMode.HALF_UP);
		}
		return beDivideNum;
	}

	/**
	 * 计算 BigDecimal的商 (四舍五入)
	 *
	 * @param num         小数保留位数
	 * @param beDivideNum 被除数
	 * @param divideNums  除数
	 * @return 商 (除数为 0或者 null时返回 0)
	 */
	public static BigDecimal divideBigDecimal(int num, BigDecimal beDivideNum, BigDecimal... divideNums) {
		beDivideNum = initBigDecimal(beDivideNum);
		for (BigDecimal divideNum : divideNums) {
			if (divideNum == null || divideNum.compareTo(DEFAULT_VALUE) == INT_ZERO) {
				return DEFAULT_VALUE;
			}
			beDivideNum = beDivideNum.divide(divideNum, num, RoundingMode.HALF_UP);
		}
		return beDivideNum;
	}

	/**
	 * 计算 BigDecimal的商 (四舍五入)
	 *
	 * @param beDivideNum 被除数
	 * @param divideNum   除数
	 * @param num         小数保留位数
	 * @return 商 (除数为 0时返回 0)
	 */
	public static BigDecimal divideBigDecimal(BigDecimal beDivideNum, BigDecimal divideNum, int num) {
		return divideBigDecimal(num, beDivideNum, divideNum);
	}

	/**
	 * 计算 BigDecimal的商 (四舍五入)
	 *
	 * @param beDivideNum 被除数
	 * @param divideNum   除数
	 * @param num         小数保留位数
	 * @param defaultNum  除数为 0时返回的值
	 * @return 商
	 */
	public static BigDecimal divideBigDecimalToDefault(BigDecimal beDivideNum, BigDecimal divideNum, int num, BigDecimal defaultNum) {
		if (divideNum == null || divideNum.compareTo(BigDecimal.ZERO) == INT_ZERO) {
			return defaultNum;
		}
		return divideBigDecimal(beDivideNum, divideNum, num);
	}

	/**
	 * 计算 BigDecimal的商 (默认保留4位小数)
	 *
	 * @param beDivideNum 被除数
	 * @param divideNum   除数
	 * @param defaultNum  除数为 0时返回的值
	 * @return 商
	 */
	public static BigDecimal divideBigDecimalToDefault(BigDecimal beDivideNum, BigDecimal divideNum, BigDecimal defaultNum) {
		if (divideNum == null || divideNum.compareTo(BigDecimal.ZERO) == INT_ZERO) {
			return defaultNum;
		}
		return divideBigDecimal(beDivideNum, divideNum, INT_FOUR);
	}

	/**
	 * 初始化 BigDecimal
	 *
	 * @return 入参为null时返回 0
	 */
	public static BigDecimal initBigDecimal(Integer num) {
		return num == null ? DEFAULT_VALUE : new BigDecimal(num);
	}

	/**
	 * 初始化 BigDecimal
	 *
	 * @return 入参为null时返回 0
	 */
	public static BigDecimal initBigDecimal(double num) {
		return new BigDecimal(Double.toString(num));
	}


	/**
	 * 初始化 BigDecimal
	 *
	 * @return 入参为null时返回 0
	 */
	public static BigDecimal initBigDecimal(BigDecimal num) {
		return num == null ? DEFAULT_VALUE : num;
	}

	/**
	 * 初始化 BigDecimal
	 *
	 * @return 入参为null时返回 0
	 */
	public static BigDecimal initBigDecimal(String numStr) {
		return StringUtils.isBlank(numStr) ? DEFAULT_VALUE : new BigDecimal(numStr);
	}

	/**
	 * 初始化 Integer
	 *
	 * @return 入参为null时返回 0
	 */
	public static Integer initInteger(String numStr) {
		return StringUtils.isBlank(numStr) ? INT_ZERO : Integer.parseInt(numStr);
	}

	/**
	 * 初始化 Short
	 *
	 * @return 入参为null时返回 0
	 */
	public static Short initShort(String numStr) {
		return StringUtils.isBlank(numStr) ? 0 : Short.parseShort(numStr);
	}


	/**
	 * 检验值是否null，有一个值是null,返回true
	 *
	 * @param values 值
	 * @return true/false
	 */
	public static boolean verifyNull(BigDecimal... values) {
		if (values != null && values.length > INT_ZERO) {
			for (BigDecimal value : values) {
				if (value == null) {
					return true;
				}
			}
			return false;
		}
		return true;
	}

	/**
	 * 检验值是否不为 null或者 0
	 *
	 * @param values 值
	 * @return 0->true,null->null,其他值->false
	 */
	public static Boolean verifyNullOrZero(BigDecimal... values) {
		if (values != null && values.length > INT_ZERO) {
			boolean zeroFlag = true;
			for (BigDecimal value : values) {
				if (value == null) {
					return null;
				}
				if (zeroFlag && value.compareTo(DEFAULT_VALUE) == INT_ZERO) {
					zeroFlag = false;
				}
			}
			return !zeroFlag;
		}
		return null;
	}

	/**
	 * 计算和
	 *
	 * @param values
	 * @return
	 */
	public static Integer sum(Collection<Integer> values) {
		int sum = 0;
		for (Integer value : values) {
			if (value != null) {
				sum += value;
			}
		}
		return sum;
	}

	/**
	 * 批量增加value
	 *
	 * @param map
	 * @param keys
	 * @param value
	 */
	public static <K, V> void addValueOfKeys(Map<K, Set<V>> map, List<K> keys, V value) {
		for (K key : keys) {
			Set<V> values = map.computeIfAbsent(key, v -> new HashSet<>());
			values.add(value);
		}
	}
}
