package com.dan.utils.jwt;

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
 * Created by ShengHaiJiang on 6/29/15.
 */
public class AESUtil {

    private static Logger logger = LoggerFactory.getLogger(AESUtil.class);

    private static String DEFAULT_CHARSET = "UTF-8";
    private static String AES = "AES";
    private static String SECURE_TYPE = "SHA1PRNG";

    /**
     * 加密, 顺序是 AES BASE64 URLEncoder
     *
     * @param content
     * @param encryptKey
     * @return
     */
    public static String encryptAES(String content, String encryptKey) {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance(AES);
            SecureRandom secureRandom = SecureRandom.getInstance(SECURE_TYPE);
            secureRandom.setSeed(encryptKey.getBytes(DEFAULT_CHARSET));
            keyGen.init(128, secureRandom);

            Cipher cipher = Cipher.getInstance(AES);
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(keyGen.generateKey().getEncoded(), AES));
            byte[] data = cipher.doFinal(content.getBytes(DEFAULT_CHARSET));

            return URLEncoder.encode(new String(Base64.encodeBase64(data), DEFAULT_CHARSET), DEFAULT_CHARSET);
        } catch (Exception e) {
            logger.error("AES encrypt error", e);
            throw new RuntimeException("AES encrypt error", e);
        }
    }

    /**
     * 解密
     *
     * @param content    内容
     * @param decryptKey key
     */
    public static String decryptAES(String content, String decryptKey) {
        try {
            byte[] encryptBytes = Base64.decodeBase64(URLDecoder.decode(content, DEFAULT_CHARSET).getBytes(DEFAULT_CHARSET));
            KeyGenerator keyGen = KeyGenerator.getInstance(AES);
            SecureRandom secureRandom = SecureRandom.getInstance(SECURE_TYPE);
            secureRandom.setSeed(decryptKey.getBytes(DEFAULT_CHARSET));
            keyGen.init(128, secureRandom);

            Cipher cipher = Cipher.getInstance(AES);
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(keyGen.generateKey().getEncoded(), AES));
            byte[] data = cipher.doFinal(encryptBytes);

            return new String(data, DEFAULT_CHARSET);
        } catch (Exception e) {
            logger.error("AES decrypt error", e);
            throw new RuntimeException("AES decrypt error", e);
        }

    }

    public static void main(String[] args) {
        String content = "bo";
        String decryptKey = "dan";
        String encryptAES = AESUtil.encryptAES(content, decryptKey);
        String decryptAES = AESUtil.decryptAES(encryptAES, decryptKey);
        System.out.println("decryptAES:" + decryptAES + ",encryptAES:" + encryptAES);
    }

}
