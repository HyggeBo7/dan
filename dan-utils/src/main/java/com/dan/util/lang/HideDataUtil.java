package com.dan.util.lang;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @fileName: HideDataUtil
 * @author: Dan
 * @createDate: 2018-12-27 17:09.
 * @description: 隐秘数据工具类
 */
public class HideDataUtil {

    /**
     * 隐藏并替换身份证号码
     *
     * @return java.lang.String
     */
    public static String hideIdCardNum(String idCardNum) {
        return hideIdCardNum(idCardNum, 1, 1);
    }

    /**
     * 用户身份证号码的打码隐藏加星号加*
     * <p>18位和非18位身份证处理均可成功处理</p>
     *
     * @param idCardNum 身份证号码
     * @param front     需要显示前几位
     * @param end       需要显示末几位
     * @return 处理完成的身份证
     */
    public static String hideIdCardNum(String idCardNum, int front, int end) {
        //身份证不能为空
        if (StringUtils.isBlank(idCardNum)) {
            return idCardNum;
        }
        //需要截取的长度不能大于身份证号长度
        if ((front + end) > idCardNum.length()) {
            return idCardNum;
        }
        //需要截取的不能小于0
        if (front < 0 || end < 0) {
            return idCardNum;
        }
        //计算*的数量
        int asteriskCount = idCardNum.length() - (front + end);
        String regex = "(\\w{" + String.valueOf(front) + "})(\\w+)(\\w{" + String.valueOf(end) + "})";
        return idCardNum.replaceAll(regex, "$1" + StringUtil.createAsterisk(asteriskCount) + "$3");
    }

    /**
     * 前六后四 隐藏银行卡号
     *
     * @param cardNo
     * @return java.lang.String
     */
    public static String hideCardNo(String cardNo) {
        if (StringUtils.isBlank(cardNo)) {
            return cardNo;
        }
        if (cardNo.length() > 10) {
            //前六后四
            StringBuilder stringBuilder = new StringBuilder();
            return stringBuilder.append(cardNo.substring(0, 6)).append("****")
                    .append(cardNo.substring(cardNo.length() - 4)).toString();
        } else {
            return cardNo;
        }
    }

    /**
     * 前三后四 隐藏手机号
     *
     * @param phoneNo
     * @return java.lang.String
     */
    public static String hidePhoneNo(String phoneNo) {
        if (StringUtils.isBlank(phoneNo)) {
            return phoneNo;
        }
        if (phoneNo.length() > 7) {
            //前3后四
            StringBuilder stringBuilder = new StringBuilder();
            return stringBuilder.append(phoneNo.substring(0, 3)).append("****").append(phoneNo.substring(phoneNo.length() - 4)).toString();
        } else {
            return phoneNo;
        }
    }

    /**
     * 隐藏并替换字符串中所有的手机号
     *
     * @param contentStr 字符串
     * @return java.lang.String
     */
    public static String hideAllPhoneNum(String contentStr) {
        if (StringUtils.isBlank(contentStr)) {
            return contentStr;
        }

        Matcher matcher = Pattern.compile("((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))\\d{8}").matcher(contentStr);
        StringBuffer sb = new StringBuffer();
        try {
            while (matcher.find()) {
                String phoneStr = matcher.group();
                phoneStr = phoneStr.substring(0, 3) + "****" + phoneStr.substring(7);
                matcher.appendReplacement(sb, phoneStr);
            }
            matcher.appendTail(sb);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return sb.toString();
    }

    /**
     * 隐藏并替换所有的身份证号码 (待完善)
     *
     * @return java.lang.String
     */
    public static String hideAllIdCardNum(String contentStr) {
        return hideAllIdCardNum(contentStr, 1, 1);
    }

    /**
     * 隐藏并替换所有的身份证号码,15位身份证不能更改 (待完善)
     *
     * @param contentStr 内容
     * @param before     前面保留位数
     * @param rear       后面保留位数
     * @return java.lang.String
     */
    public static String hideAllIdCardNum(String contentStr, int before, int rear) {
        if (StringUtils.isBlank(contentStr)) {
            return contentStr;
        }
        Pattern pattern = Pattern.compile("(\\d{6})(19|20)(\\d{2})(1[0-2]|0[1-9])(0[1-9]|[1-2][0-9]|3[0-1])(\\d{3})(\\d|X|x)");
        //Pattern pattern = Pattern.compile("(\\d{6})(18|19|20)?(\\d{2})([01]\\d)([0123]\\d)(\\d{3})(\\d|X|x)");
        Matcher matcher = pattern.matcher(contentStr);
        StringBuffer sb = new StringBuffer();
        try {
            while (matcher.find()) {
                String idCardStr = matcher.group();
                int len = idCardStr.length();
                int strLength = len - (before + rear);
                strLength = strLength <= 0 ? 1 : strLength;
                if (len >= 9) {
                    //idCardStr = idCardStr.replaceAll("(.{" + (len < 12 ? 3 : 6) + "})(.*)(.{4})", "$1" + "****" + "$3");
                    idCardStr = idCardStr.replaceAll("(.{" + before + "})(.*)(.{" + rear + "})", "$1" + StringUtil.createAsterisk(strLength) + "$3");
                }
                matcher.appendReplacement(sb, idCardStr);
            }
            matcher.appendTail(sb);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return sb.toString();
    }

}
