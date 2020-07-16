package top.dearbo.util.jwt;

import org.apache.commons.codec.binary.StringUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * @version 1.0
 * @author: Bo
 * @fileName: Des3Util
 * @createDate: 2020-07-16 17:24.
 * @description:
 */
public class Des3Util {

    /**
     * 3DES加密,结果Base64返回
     * 加密模式：ECB
     * 填充模式：PKCS5Padding
     *
     * @param key  key的长度要24位,DES是8,这里的3DES,所以 长度=3*8=24
     * @param data 数据
     * @return String
     */
    public static String encrypt3Des(String key, String data) {
        byte[] keyByte = StringUtils.getBytesUtf8(key);
        byte[] dataByte = StringUtils.getBytesUtf8(data);
        try {
            Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(keyByte, "DESede"));
            byte[] bytes = cipher.doFinal(dataByte);
            //对结果Base64
            return Base64.getEncoder().encodeToString(bytes);
            //gzip压缩
            //return Base64.getEncoder().encodeToString(GZIPUtil.compressByte(bytes));
        } catch (IllegalBlockSizeException | BadPaddingException | NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 3DES解密
     *
     * @param key  key
     * @param data 加密的内容
     * @return String
     */
    public static String decrypt3Des(String key, String data) {
        //因为加密时结果Base64了，所以这里需要Base64解
        byte[] dataByte = Base64.getDecoder().decode(data);
        //gzip解压缩
        //dataByte = GZIPUtil.unCompressByte(dataByte);
        byte[] keyByte = StringUtils.getBytesUtf8(key);
        try {
            Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(keyByte, "DESede"));
            return StringUtils.newStringUtf8(cipher.doFinal(dataByte));
        } catch (IllegalBlockSizeException | BadPaddingException | NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        String str = "assdsdsaedweqw";
        String key = "9oyKs7cVo1yYzkuisP9bhA==";
        String encrypt3DES = Des3Util.encrypt3Des(key, str);
        String decrypt3DES = Des3Util.decrypt3Des(key, encrypt3DES);
        System.out.println("=============加密：" + encrypt3DES + ",解密：" + decrypt3DES);
    }
}
