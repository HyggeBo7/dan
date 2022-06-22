package top.dearbo.util.lang;

import java.math.BigDecimal;

/**
 * 数字转换中文
 *
 * @author wb
 */
public class NumberUtil {

	/**
	 * 中文数字
	 */
	private static final String[] CN_NUM = {"零", "一", "二", "三", "四", "五", "六", "七", "八", "九"};

	/**
	 * 中文数字单位
	 */
	private static final String[] CN_UNIT = {"", "十", "百", "千", "万", "十", "百", "千", "亿", "十", "百", "千"};

	/**
	 * 特殊字符：负
	 */
	private static final String CN_NEGATIVE = "负";

	/**
	 * 特殊字符：点
	 */
	private static final String CN_POINT = "点";

	/**
	 * 数字转中文
	 * 可以带单位
	 *
	 * @param intNum   值
	 * @param zeroFlag 0是否返回
	 * @param unit     单位
	 * @return 中文数字+单位
	 */
	public static String numToChineseUnit(int intNum, boolean zeroFlag, String unit) {
		String value = numToChinese(intNum, true, zeroFlag);
		if (!value.isEmpty()) {
			return unit == null ? value : value + unit;
		}
		return value;
	}

	/**
	 * int 转 中文数字,支持到int最大值
	 *
	 * @param intNum           要转换的整型数
	 * @param removeTenOneFlag 是否去掉一，一十五 > 十五，一十一万 -> 十一万
	 * @param zeroFlag         值为0是否返回
	 * @return 中文数字
	 */
	public static String numToChinese(int intNum, boolean removeTenOneFlag, boolean zeroFlag) {
		if (zeroFlag && intNum == 0) {
			return CN_NUM[intNum];
		}
		StringBuilder sb = new StringBuilder();
		boolean isNegative = false;
		if (intNum < 0) {
			isNegative = true;
			intNum *= -1;
		}
		int count = 0;
		while (intNum > 0) {
			sb.insert(0, CN_NUM[intNum % 10] + CN_UNIT[count]);
			intNum = intNum / 10;
			count++;
		}
		if (isNegative) {
			sb.insert(0, CN_NEGATIVE);
		}
		String value = sb.toString();
		if (!value.isEmpty()) {
			value = value.replaceAll("零[千百十]", "零").replaceAll("零+万", "万")
					.replaceAll("零+亿", "亿").replaceAll("亿万", "亿零")
					.replaceAll("零+", "零").replaceAll("零$", "");
			if (removeTenOneFlag && value.startsWith("一十")) {
				//去掉一，一十五 > 十五，一十一万 -> 十一万
				return value.substring(1);
			}
			return value;
		}
		return value;
	}

	/**
	 * bigDecimal 转 中文数字
	 * 整数部分只支持到int的最大值
	 *
	 * @param bigDecimalNum 要转换的BigDecimal数
	 * @return 中文数字
	 */
	public static String decimalToChinese(BigDecimal bigDecimalNum) {
		if (bigDecimalNum == null) {
			return CN_NUM[0];
		}
		StringBuilder sb = new StringBuilder();
		//将小数点后面的零给去除
		String numStr = bigDecimalNum.abs().stripTrailingZeros().toPlainString();
		String[] split = numStr.split("\\.");
		sb.append(numToChinese(Integer.parseInt(split[0]), true, true));
		//如果传入的数有小数，则进行切割，将整数与小数部分分离
		if (split.length == 2) {
			//有小数部分
			sb.append(CN_POINT);
			for (char aChar : split[1].toCharArray()) {
				int index = Integer.parseInt(String.valueOf(aChar));
				sb.append(CN_NUM[index]);
			}
		}
		//判断传入数字为正数还是负数
		int sigNum = bigDecimalNum.signum();
		if (sigNum == -1) {
			sb.insert(0, CN_NEGATIVE);
		}
		return sb.toString();
	}

	/*public static void main(String[] args) {
		int[] cnArr = new int[]{1010, 100000000, 100000001, 1000000001, 0, 1, 5, 10, 15, 29, 38, 100, 101, 199, 1000, 1001, 1520, 11111, 110011, 112211, 1100111, 1122111, 11221111, 11001111, 111111111};
		for (int num : cnArr) {
			System.out.println(num + " 转换：" + NumberUtil.numToChineseUnit(num, true, "场"));
		}
	}*/
}

