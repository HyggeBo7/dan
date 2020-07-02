package top.dearbo.util.jwt;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.SecureRandom;

/**
 * Created by Bo on 6/29/15.
 */
public class AESUtil {

    private static Logger logger = LoggerFactory.getLogger(AESUtil.class);
    private static final String DEFAULT_KEY = "AESDearBoKey";

    private static String DEFAULT_CHARSET = "UTF-8";
    private static String AES = "AES";
    private static String SECURE_TYPE = "SHA1PRNG";

    /**
     * 加密
     *
     * @param content 内容
     * @return 加密后的
     */
    public static String encryptAES(String content) throws Exception {
        return encryptAES(content, DEFAULT_KEY);
    }

    /**
     * 加密, 顺序是 AES BASE64 URLEncoder
     *
     * @param content    内容
     * @param encryptKey key
     * @return
     */
    public static String encryptAES(String content, String encryptKey) throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance(AES);
        SecureRandom secureRandom = SecureRandom.getInstance(SECURE_TYPE);
        secureRandom.setSeed(encryptKey.getBytes(DEFAULT_CHARSET));
        keyGen.init(128, secureRandom);

        Cipher cipher = Cipher.getInstance(AES);
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(keyGen.generateKey().getEncoded(), AES));
        byte[] data = cipher.doFinal(content.getBytes(DEFAULT_CHARSET));
        return URLEncoder.encode(new String(Base64.encodeBase64(data), DEFAULT_CHARSET), DEFAULT_CHARSET);
    }

    /**
     * 解密
     *
     * @param content 内容
     * @return 解密结果
     */
    public static String decryptAES(String content) throws Exception {
        return decryptAES(content, DEFAULT_KEY);
    }

    /**
     * 解密
     *
     * @param content    内容
     * @param decryptKey key
     */
    public static String decryptAES(String content, String decryptKey) throws Exception {
        byte[] encryptBytes = Base64.decodeBase64(URLDecoder.decode(content, DEFAULT_CHARSET).getBytes(DEFAULT_CHARSET));
        KeyGenerator keyGen = KeyGenerator.getInstance(AES);
        SecureRandom secureRandom = SecureRandom.getInstance(SECURE_TYPE);
        secureRandom.setSeed(decryptKey.getBytes(DEFAULT_CHARSET));
        keyGen.init(128, secureRandom);

        Cipher cipher = Cipher.getInstance(AES);
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(keyGen.generateKey().getEncoded(), AES));
        byte[] data = cipher.doFinal(encryptBytes);

        return new String(data, DEFAULT_CHARSET);
    }

    /*public static void main(String[] args) throws Exception {
        String content = "HAHA中文唉";
        String encryptAES = AESUtil.encryptAES(content);
        String decryptAES = AESUtil.decryptAES(encryptAES);
        System.out.println("decryptAES:" + decryptAES + ",encryptAES:" + encryptAES);
    }*/

}
