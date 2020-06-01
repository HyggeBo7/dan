package top.dearbo.util.xt;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.apache.commons.lang3.StringUtils;

public class PinYinUtil {

    /**
     * pinyin4j格式类
     */
    private HanyuPinyinOutputFormat format;

    public PinYinUtil() {
        format = new HanyuPinyinOutputFormat();
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        format.setCaseType(HanyuPinyinCaseType.UPPERCASE);
    }

    /**
     * 对单个字进行转换
     */
    public String getPinYinFirst(String pinYinStr, String defaultChar) {
        if (StringUtils.isNotBlank(pinYinStr)) {
            char c = pinYinStr.trim().charAt(0);
            return getCharPinYin(c, defaultChar);
        }
        return defaultChar;
    }

    /**
     * 获取首字母
     */
    public String getPinYinFirstChar(String pinYinStr, String defaultChar) {
        String pinYinFirst = getPinYinFirst(pinYinStr, defaultChar);
        if (StringUtils.isNotBlank(pinYinFirst) && pinYinFirst.length() > 1) {
            return pinYinFirst.substring(0, 1);
        }
        return defaultChar;
    }

    /**
     * 对单个字进行转换
     *
     * @param pinYinStr 需转换的汉字字符串
     * @return 拼音字符串数组
     */
    private String getCharPinYin(char pinYinStr, String defaultChar) {
        String[] pinyin = null;
        try {
            //执行转换
            pinyin = PinyinHelper.toHanyuPinyinStringArray(pinYinStr, format);

        } catch (BadHanyuPinyinOutputFormatCombination e) {
            e.printStackTrace();
        }

        //pinyin4j规则，当转换的符串不是汉字，就返回null
        if (pinyin == null || pinyin.length == 0) {
            return defaultChar;
        }

        //多音字会返回一个多音字拼音的数组，pinyiin4j并不能有效判断该字的读音
        return pinyin[0];
    }

    /**
     * 对单个字进行转换
     *
     * @param pinYinStr
     * @return
     */
    public String getPinYin(String pinYinStr) {
        StringBuffer sb = new StringBuffer();
        String tempStr = null;
        //循环字符串
        for (int i = 0; i < pinYinStr.length(); i++) {
            tempStr = getCharPinYin(pinYinStr.charAt(i), null);
            if (tempStr == null) {
                //非汉字直接拼接
                sb.append(pinYinStr.charAt(i));
            } else {
                sb.append(tempStr + " ");
            }
        }
        return sb.toString();
    }

}