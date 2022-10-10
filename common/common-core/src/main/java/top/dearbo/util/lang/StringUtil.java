package top.dearbo.util.lang;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Bo on 2017/10/23.
 */
public class StringUtil extends StringUtils {

    //private static final Logger logger = LoggerFactory.getLogger(StringUtil.class);

    /**
     * 转换字符 1-9，A-I
     *
     * @param num
     * @return
     */
    public static String changeStr(int num) {
        if (num == 0) {
            return "";
        } else {
            char c = (char) (num + 64);
            return String.valueOf(c);
        }
    }

    /**
     * 最后一位，替换字符 1-9替换ABC 0替换 空
     */
    public static String replaceLastStr(String str) {
        if (StringUtils.isEmpty(str)) {
            return "";
        }
        //截取最后一位字符
        String lastStr = str.substring(str.length() - 1);

        //删除最后一位字符
        str = str.substring(0, str.length() - 1);

        try {
            int num = Integer.parseInt(lastStr);
            //替换字符
            String toChar = changeStr(num);
            return str + toChar;
        } catch (NumberFormatException e) {
            System.out.println("最后一位不是数字...[ " + lastStr + " ]");
        }
        return str + lastStr;
    }

    /**
     * 根据键值对填充字符串，如("hello ${name}",{name:"xiaoming"})
     * 输出：
     *
     * @param content
     * @param map
     * @return
     */
    public static String renderString(String content, Map<String, Object> map) {
        if (map != null && map.size() > 0) {
            Set<Map.Entry<String, Object>> sets = map.entrySet();
            for (Map.Entry<String, Object> entry : sets) {
                String regex = "\\$\\{" + entry.getKey() + "}";
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(content);
                content = matcher.replaceAll(judgeTypeString(entry.getValue()));
            }
        }
        return content;
    }

    public static Object judgeConversionType(Object param) {
        if (param == null) {
            return "";
        }
        if (param instanceof Short) {
            return Short.parseShort(param.toString());
        } else if (param instanceof Integer) {
            return Integer.parseInt(param.toString());
        } else if (param instanceof BigDecimal) {
            return new BigDecimal(param.toString());
        } else if (param instanceof Double) {
            String numberStr = String.valueOf(param);
            if (numberStr.contains(".") || numberStr.contains("e") || numberStr.contains("E")) {
                Double v = Double.parseDouble(numberStr);
                if (numberStr.endsWith(".0")) {
                    return v.longValue();
                }
            }
            return Double.parseDouble(numberStr);
        } else if (param instanceof Float) {
            return Float.parseFloat(param.toString());
        } else if (param instanceof Long) {
            return Long.parseLong(param.toString());
        } else if (param instanceof Boolean) {
            return Boolean.parseBoolean(param.toString());
        }
        return param;
    }

    /**
     * 判断类型-String类型返回结果加单引号
     */
    public static String judgeTypeString(Object param) {
        if (param == null) {
            return "";
        }
        if (param instanceof String) {
            return "'" + param + "'";
        } else {
            return String.valueOf(param);
        }
    }

    /**
     * 在指定位置插入字符
     *
     * @param str      原字符串
     * @param dec      要插入的字符串
     * @param position 位置
     * @return
     */
    public static String insertStringInParticularPosition(String str, String dec, int position) {
        StringBuffer stringBuffer = new StringBuffer(str);
        return stringBuffer.insert(position, dec).toString();
    }

    public static String removeStrBlank(String str) {
        return removeStrBlank(str, "");
    }

    public static String removeStrBlank(String str, String replacement) {
        String regex = "\\s*|\t|\r|\n";
        return removeStrBlank(str, replacement, regex);
    }

    public static String removeStrBlank(String str, String replacement, String regex) {
        if (StringUtils.isNotBlank(str)) {
            Pattern pattern = Pattern.compile(regex);
            Matcher m = pattern.matcher(str);
            str = m.replaceAll(replacement);
        }
        return str;
    }

    /**
     * 生成很多个字符
     *
     * @param length 长度
     * @param str    字符
     */
    public static String createAsterisk(int length, String str) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < length; i++) {
            stringBuffer.append(str);
        }
        return stringBuffer.toString();
    }

    /**
     * 生成很多个*号
     *
     * @param length 长度
     */
    public static String createAsterisk(int length) {
        return createAsterisk(length, "*");
    }

    /**
     * 计算位数 一个中文汉字算两位，一个英文字母算一位
     *
     * @param str 字符
     * @return 字符串长度
     */
    public static int calculatePlaces(String str) {
        return calculatePlaces(str, 2.0, 1.0);
    }

    /**
     * 计算位数
     *
     * @param str        字符串
     * @param strSize    字符长度
     * @param numberSize 不是字符长度
     * @return 字符总长度
     */
    public static int calculatePlaces(String str, Double strSize, Double numberSize) {
        if (StringUtils.isBlank(str)) {
            return 0;
        }
        Double m = 0.0;
        char[] arr = str.toCharArray();
        for (char c : arr) {
            //中文字符
            if ((c >= 0x0391 && c <= 0xFFE5)) {
                m += strSize;
            } else if (c <= 0x00FF) {
                //英文字符
                m += numberSize;
            } else {
                m += 1;
            }
        }
        return m.intValue();
    }

    /**
     * 判断是否字母
     *
     * @param zm 字符
     * @return 字母：true
     */
    public static boolean isLetter(char zm) {
        return isUpperCaseLetter(zm) || isLowerCaseLetter(zm);
    }

    /**
     * 判断是否大写字母 65
     *
     * @param zm 字母
     * @return 是：true
     */
    public static boolean isUpperCaseLetter(char zm) {
        return zm >= 0x0041 && zm <= 0x005A;
    }

    /**
     * 判断是否小写字母 97
     *
     * @param zm 字母
     * @return 是：true
     */
    public static boolean isLowerCaseLetter(char zm) {
        return zm >= 0x0061 && zm <= 0x007A;
    }

    /**
     * 判断是否数字
     *
     * @param sz 字符
     * @return 数字：true
     */
    public static boolean isNumeral(char sz) {
        return (sz >= 0x0030 && sz <= 0x0039);
    }

    /**
     * 判断是否汉字
     *
     * @param hz 字符
     * @return 汉字：true
     */
    public static boolean isChinese(char hz) {
        return (hz >= 0x4E00 && hz <= 0x9FA5);
    }

    /**
     * 判断是否空格
     *
     * @param hz
     * @return
     */
    public static boolean isSpace(char hz) {
        return hz == 32;
    }

    /**
     * 首字母转小写
     *
     * @param str 字符串
     * @return 转换后
     */
    public static String firstToLowerCase(String str) {
        if (StringUtils.isNotBlank(str)) {
            char[] chars = str.toCharArray();
            //如果是大写字母
            if (isUpperCaseLetter(chars[0])) {
                chars[0] += 32;
                return String.valueOf(chars);
            }
        }
        return str;
    }

    /**
     * 首字母转大写
     *
     * @param str 字符串
     * @return 转换后
     */
    public static String firstToUpperCase(String str) {
        if (StringUtils.isNotBlank(str)) {
            char[] chars = str.toCharArray();
            //如果是小写字母
            if (isLowerCaseLetter(chars[0])) {
                chars[0] -= 32;
                return String.valueOf(chars);
            }
        }
        return str;
    }

    /**
     * 截取字符去除前后空
     *
     * @param str    字符
     * @param length 长度
     * @return String
     */
    public static String truncateToTrim(String str, int length) {
        return truncate(trim(str), length);
    }

    /*public static void main(String[] args) {
        String s = "safsghgj\n\n\n\n\n\tfdhfghdfg\tdsfgsdfgs\t";
        System.out.println("====="+s);
        String s1 = removeStrBlank(s,"/");
        System.out.println("++++++"+s1);

        System.out.println(truncateToTrim("a ",5));
    }*/


}
