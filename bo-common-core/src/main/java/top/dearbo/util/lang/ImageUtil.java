package top.dearbo.util.lang;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

/**
 * @version 1.0
 * @author: Bo
 * @fileName: ImageUtils
 * @createDate: 2019-10-15 09:15.
 * @description: 图片工具类
 */
public class ImageUtil {

    public final static String BASE64_PREFIX = "data:image/png;base64,";

    /**
     * base转byte
     *
     * @param base64String 字符串
     * @return byte[]
     */
    public static byte[] base64ToByte(String base64String) {
        if (StringUtils.isBlank(base64String)) {
            return null;
        }
        if (base64String.startsWith(BASE64_PREFIX)) {
            base64String = base64String.substring(BASE64_PREFIX.length());
        }
        return Base64.decodeBase64(base64String);
    }

    /**
     * base64转InputStream
     *
     * @param base64String base字符串
     * @return InputStream
     */
    public static ByteArrayInputStream base64ToInputStream(String base64String) {
        byte[] bytes = base64ToByte(base64String);
        return bytes == null ? null : new ByteArrayInputStream(bytes);
    }

    /**
     * base64转jpg文件
     *
     * @param base64String base64
     * @param file         文件
     * @return 成功true, 否则false
     */
    public static boolean base64ToFileJpg(String base64String, File file) throws IOException {
        return base64ToFile(base64String, file, "jpg");
    }

    /**
     * base64转png文件
     */
    public static boolean base64ToFilePng(String base64String, File file) throws IOException {
        return base64ToFile(base64String, file, "png");
    }

    public static boolean base64ToFile(String base64String, File file, String formatName) throws IOException {
        if (file == null) {
            return false;
        }
        ByteArrayInputStream byteArrayInputStream = base64ToInputStream(base64String);
        if (byteArrayInputStream == null) {
            return false;
        }
        BufferedImage bi1 = ImageIO.read(byteArrayInputStream);
        ImageIO.write(bi1, formatName, file);
        return true;
    }

    /**
     * byte[]转base64
     *
     * @param bytes byte[]
     * @return base64字符
     */
    public static String byteToBase64(byte[] bytes) {
        if (bytes == null) {
            return null;
        }

        return Base64.encodeBase64String(bytes);
    }
}
