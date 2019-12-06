package com.dan.utils.xt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * Created by dan on 2017/3/16.
 */
public class JwtUtil {

    private static final String DEFAULT_KEY = "dear_bo_key";

    /**
     * 由字符串生成加密key
     *
     * @return
     */
    public static SecretKey generalKey(String stringKey) {
        byte[] encodedKey;
        try {
            encodedKey = Base64.decodeBase64(stringKey);
            //对值长度有限制
            //encodedKey = DatatypeConverter.parseBase64Binary(stringKey);
        } catch (Throwable e) {
            encodedKey = java.util.Base64.getDecoder().decode(stringKey.getBytes(StandardCharsets.UTF_8));
        }
        return new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
    }

    public static String createJWT(String id, String subject, long ttlMillis) throws Exception {
        return createJWT(id, subject, ttlMillis, DEFAULT_KEY);
    }

    /**
     * 创建jwt
     *
     * @param id
     * @param subject
     * @param ttlMillis  毫秒
     * @param generalKey key
     * @return 加密内容
     */
    public static String createJWT(String id, String subject, long ttlMillis, String generalKey) throws Exception {

        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        SecretKey key = generalKey(generalKey);
        JwtBuilder builder = Jwts.builder()
                .setId(id)
                .setIssuedAt(now)
                .setSubject(subject)
                .signWith(signatureAlgorithm, key);
        if (ttlMillis >= 0) {
            long expMillis = nowMillis + ttlMillis;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }
        return builder.compact();
    }

    public static Claims parseJWT(String jwt) throws Exception {
        return parseJWT(jwt, DEFAULT_KEY);
    }

    /**
     * 解密jwt
     *
     * @param jwt
     * @return
     * @throws Exception
     */
    public static Claims parseJWT(String jwt, String generalKey) throws Exception {
        SecretKey key = generalKey(generalKey);
        return Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(jwt).getBody();
    }

    /*public static void main(String[] args) throws Exception {
        String key = DEFAULT_KEY;
        String authToken = "{\"userId\":10,\"userName\":\"管理员\",\"appToken\":\"CJzdWIiOiJ7XCJ1c2VySDkwMTUeyJhbGciOiJI\",\"superFlag\":false}";
        //7天有效期
        String token = JwtUtil.createJWT("10", authToken, 24 * 60 * 60 * 1000 * 7, key);
        System.out.println("===========token:" + token);

        Claims claims = JwtUtil.parseJWT(token, key);
        Date expirationDate = claims.getExpiration();
        System.out.println("claims.getSubject():" + claims.getSubject());
    }*/

}
