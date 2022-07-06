/**
 * @author joker
 * @date 创建时间：2018年5月14日 上午11:11:51
 */
package top.dearbo.util.jwt;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author: bo
 * @date 创建时间：2018年5月14日 上午11:11:51
 */
public class Md5Util {

    private static final String ENCRYPTION_ALGORITHM = "MD5";

    /**
     * MD5加密
     *
     * @param password
     * @author: bo
     * @date 创建时间：2018年3月5日 下午7:26:17
     */
    public static String md5Encrypt(String password) {
        try {
            // 得到一个信息摘要器
            MessageDigest digest = MessageDigest.getInstance(ENCRYPTION_ALGORITHM);
            byte[] result = digest.digest(password.getBytes());
            StringBuffer buffer = new StringBuffer();
            // 把每一个byte 做一个与运算 0xff;
            for (byte b : result) {
                // 与运算,加盐
                int number = b & 0xff;
                String str = Integer.toHexString(number);
                if (str.length() == 1) {
                    buffer.append("0");
                }
                buffer.append(str);
            }

            // 标准的md5加密后的结果
            return buffer.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * md5加密
     *
     * @param password
     */
    public static String encode(String password) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        try {
            byte[] btInput = password.getBytes(Charset.forName("UTF-8"));
            MessageDigest mdInst = MessageDigest.getInstance(ENCRYPTION_ALGORITHM);
            mdInst.update(btInput);
            byte[] md = mdInst.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
