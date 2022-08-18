package top.dearbo.util.xt;

import org.apache.commons.lang3.StringUtils;
import top.dearbo.util.lang.StringUtil;

import java.util.regex.Pattern;

public class ValidateUtils {

    /**
     * 正则表达式匹配中文
     */
    public final static String RE_CHINESE = "[\u4E00-\u9FFF]";

    /**
     * 英文字母 、数字和下划线
     */
    public final static Pattern GENERAL = Pattern.compile("^\\w+$");
    /**
     * 数字
     */
    public final static Pattern NUMBER = Pattern.compile("\\d+");
    /**
     * 分组
     */
    public final static Pattern GROUP_VAR = Pattern.compile("\\$(\\d+)");
    /**
     * IP v4
     */
    public final static Pattern IPV4 = Pattern.compile("\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\b");
    /**
     * 货币
     */
    public final static Pattern MONEY = Pattern.compile("^(\\d+(?:\\.\\d+)?)$");
    /**
     * 邮件
     */
    public final static Pattern EMAIL = Pattern.compile("(\\w|.)+@\\w+(\\.\\w+){1,2}");
    /**
     * 移动电话
     */
    public final static Pattern MOBILE = Pattern.compile("1\\d{10}");
    /**
     * 身份证号码
     */
    public final static Pattern CITIZEN_ID = Pattern.compile("[1-9]\\d{5}[1-2]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}(\\d|X|x)");
    /**
     * 邮编
     */
    public final static Pattern ZIP_CODE = Pattern.compile("\\d{6}");
    /**
     * 邮编
     */
    public final static Pattern BIRTHDAY = Pattern.compile("(\\d{4})(/|-|\\.)(\\d{1,2})(/|-|\\.)(\\d{1,2})日?$");
    /**
     * URL
     */
    public final static Pattern URL = Pattern.compile("(https://|http://)?([\\w-]+\\.)+[\\w-]+(/[\\w-]\\.?%&=]*)?");

    /**
     * 中文字、英文字母、数字和下划线
     */
    public final static Pattern GENERAL_WITH_CHINESE = Pattern.compile("^[\\u0391-\\uFFE5\\w]+$");
    /**
     * UUID
     */
    public final static Pattern UUID = Pattern.compile("^[0-9a-z]{8}-[0-9a-z]{4}-[0-9a-z]{4}-[0-9a-z]{4}-[0-9a-z]{12}$");

    /**
     * xml
     */
    public final static Pattern XML_TAG = Pattern.compile("<([^/]\\S*?)>(.*?)</(\\S*?)>");

    /**
     * 验证是否为空<br>
     * 对于String类型判定是否为empty(null 或 "")<br>
     *
     * @param value 值
     * @return 是否为空
     */
    public static <T> boolean isEmpty(T value) {
        return (null == value || (value instanceof String && StringUtils.isEmpty((String) value)));
    }

    /**
     * 通过正则表达式验证
     *
     * @param regex 正则
     * @param value 值
     * @return 是否匹配正则
     */
    public static boolean isByRegex(String regex, String value) {
        return isMatch(regex, value);
    }

    /**
     * 通过正则表达式验证
     *
     * @param pattern 正则模式
     * @param value   值
     * @return 是否匹配正则
     */
    public static boolean isByRegex(Pattern pattern, String value) {
        return isMatch(pattern, value);
    }

    /**
     * 给定内容是否匹配正则
     *
     * @param regex   正则
     * @param content 内容
     * @return 正则为null或者""则不检查，返回true，内容为null返回false
     */
    public static boolean isMatch(String regex, String content) {
        if (StringUtils.isBlank(content)) {
            //提供null的字符串为不匹配
            return false;
        }

        if (StringUtils.isBlank(regex)) {
            //正则不存在则为全匹配
            return true;
        }

        return Pattern.matches(regex, content);
    }

    /**
     * 给定内容是否匹配正则
     *
     * @param pattern 模式
     * @param content 内容
     * @return 正则为null或者""则不检查，返回true，内容为null返回false
     */
    public static boolean isMatch(Pattern pattern, String content) {
        if (content == null || pattern == null) {
            //提供null的字符串为不匹配
            return false;
        }
        return pattern.matcher(content).matches();
    }

    /**
     * 验证是否为英文字母 、数字和下划线
     *
     * @param value 值
     * @return 是否为英文字母 、数字和下划线
     */
    public static boolean isGeneral(String value) {
        return isByRegex(GENERAL, value);
    }

    /**
     * 验证是否为给定长度范围的英文字母 、数字和下划线
     *
     * @param value 值
     * @param min   最小长度，负数自动识别为0
     * @param max   最大长度，0或负数表示不限制最大长度
     * @return 是否为给定长度范围的英文字母 、数字和下划线
     */
    public static boolean isGeneral(String value, int min, int max) {
        String reg = "^\\w{" + min + "," + max + "}$";
        if (min < 0) {
            min = 0;
        }
        if (max <= 0) {
            reg = "^\\w{" + min + ",}$";
        }
        return isByRegex(reg, value);
    }

    /**
     * 验证是否为给定最小长度的英文字母 、数字和下划线
     *
     * @param value 值
     * @param min   最小长度，负数自动识别为0
     * @return 是否为给定最小长度的英文字母 、数字和下划线
     */
    public static boolean isGeneral(String value, int min) {
        return isGeneral(value, min, 0);
    }

    /**
     * 验证该字符串是否是数字
     *
     * @param value 字符串内容
     * @return 是否是数字
     */
    public static boolean isNumber(String value) {
        if (StringUtils.isBlank(value)) {
            return false;
        }
        return isByRegex(NUMBER, value);
    }

    /**
     * 验证是否为货币
     *
     * @param value 值
     * @return 是否为货币
     */
    public static boolean isMoney(String value) {
        return isByRegex(MONEY, value);
    }

    /**
     * 验证是否为邮政编码（中国）
     *
     * @param value 值
     * @return 是否为邮政编码（中国）
     */
    public static boolean isZipCode(String value) {
        return isByRegex(ZIP_CODE, value);
    }

    /**
     * 验证是否为可用邮箱地址
     *
     * @param value 值
     * @return 否为可用邮箱地址
     */
    public static boolean isEmail(String value) {
        return isByRegex(EMAIL, value);
    }

    /**
     * 验证是否为手机号码（中国）
     *
     * @param value 值
     * @return 是否为手机号码（中国）
     */
    public static boolean isMobile(String value) {
        return isByRegex(MOBILE, value);
    }

    /**
     * 验证是否为身份证号码（18位中国）<br>
     * 出生日期只支持到到2999年
     *
     * @param value 值
     * @return 是否为身份证号码（18位中国）
     */
    public static boolean isCitizenId(String value) {
        return isByRegex(CITIZEN_ID, value);
    }

    /**
     * 验证是否为生日<br>
     *
     * @param value 值
     * @return 是否为生日
     */
    public static boolean isBirthday(String value) {
        if (isByRegex(BIRTHDAY, value)) {
            int year = Integer.parseInt(value.substring(0, 4));
            int month = Integer.parseInt(value.substring(5, 7));
            int day = Integer.parseInt(value.substring(8, 10));

            if (month < 1 || month > 12) {
                return false;
            }
            if (day < 1 || day > 31) {
                return false;
            }
            if ((month == 4 || month == 6 || month == 9 || month == 11) && day == 31) {
                return false;
            }
            if (month == 2) {
                boolean isleap = (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0));
                if (day > 29 || (day == 29 && !isleap)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 验证是否为IPV4地址
     *
     * @param value 值
     * @return 是否为IPV4地址
     */
    public static boolean isIpv4(String value) {
        return isByRegex(IPV4, value);
    }

    /**
     * 验证是否为URL
     *
     * @param value 值
     * @return 是否为URL
     */
    public static boolean isUrl(String value) {
        return isByRegex(URL, value);
    }

    /**
     * 验证是否为汉字
     *
     * @param value 值
     * @return 是否为汉字
     */
    public static boolean isChinese(String value) {
        return isByRegex("^" + RE_CHINESE + "+$", value);
    }

    /**
     * 验证是否为中文字、英文字母、数字和下划线
     *
     * @param value 值
     * @return 是否为中文字、英文字母、数字和下划线
     */
    public static boolean isGeneralWithChinese(String value) {
        return isByRegex(GENERAL_WITH_CHINESE, value);
    }

    /**
     * 验证是否为UUID
     *
     * @param value 值
     * @return 是否为UUID
     */
    public static boolean isUUID(String value) {
        return isByRegex(UUID, value);
    }

    /**
     * 校验银行卡卡号
     */
    public static boolean checkBankCard(String bankCard) {
        bankCard = StringUtil.removeStrBlank(bankCard);
        if (StringUtils.isBlank(bankCard) || bankCard.length() < 15 || bankCard.length() > 19) {
            return false;
        }
        char bit = getBankCardCheckCode(bankCard.substring(0, bankCard.length() - 1));
        if (bit == 'N') {
            return false;
        }
        return bankCard.charAt(bankCard.length() - 1) == bit;
    }

    /**
     * 从不含校验位的银行卡卡号采用 Luhm 校验算法获得校验位,不通过返回N
     * 1、从卡号最后一位数字开始，逆向将奇数位(1、3、5等等)相加。
     * 2、从卡号最后一位数字开始，逆向将偶数位数字，先乘以2(如果乘积为两位数，将个位十位数字相加，即将其减去9)，再求和。
     * 3、将奇数位总和加上偶数位总和，结果应该可以被10整除。
     *
     * @param nonCheckCodeBankCard 卡号
     * @return char
     */
    private static char getBankCardCheckCode(String nonCheckCodeBankCard) {
        if (StringUtils.isBlank(nonCheckCodeBankCard) || !nonCheckCodeBankCard.matches("\\d+")) {
            // 如果传的不是数据返回N
            return 'N';
        }
        char[] chs = nonCheckCodeBankCard.toCharArray();
        int luhmSum = 0;
        for (int i = chs.length - 1, j = 0; i >= 0; i--, j++) {
            int k = chs[i] - '0';
            if (j % 2 == 0) {
                k *= 2;
                k = k / 10 + k % 10;
            }
            luhmSum += k;
        }
        return (luhmSum % 10 == 0) ? '0' : (char) ((10 - luhmSum % 10) + '0');
    }

}
