package top.dearbo.util.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Date;

/**
 * @version 1.0
 * @author: Bo
 * @fileName: JwtUtils
 * @createDate: 2020-01-16 10:32.
 * @description: jwt工具类
 */
public class JwtUtil {

	private static final String DEFAULT_KEY = "JwtDearBo+d06ed21a41cd6ff5+85609984d06ed21a41cd6ff5bb1edf94";

	public static String createToken(String id, String subject, long ttlMillis) throws Exception {
		return createToken(id, subject, ttlMillis, DEFAULT_KEY);
	}

	public static String createToken(String id, String subject, long ttlMillis, String generalKey) throws Exception {
		return createToken(id, subject, ttlMillis, generalKey(generalKey));
	}

	public static String createToken(String id, String subject, long ttlMillis, SecretKey generalKey) throws Exception {
		long nowMillis = System.currentTimeMillis();
		JwtBuilder jwtBuilder = Jwts.builder()
				// 设置头部信息header
				.header()
				.add("typ", "JWT")
				.add("alg", "HS256")
				.and()
				// 设置自定义负载信息payload
				//.claim("username", username)
				// 令牌ID
				.id(id)
				// 签发时间
				.issuedAt(new Date())
				// 主题
				.subject(subject)
				// 签发者
				.issuer("SnailClimb")
				// 签名
				.signWith(generalKey, Jwts.SIG.HS256);
		if (ttlMillis >= 0) {
			// 过期日期
			jwtBuilder.expiration(new Date(nowMillis + ttlMillis));
		}
		return jwtBuilder.compact();
	}

	public static Claims parseToken(String token) {
		return parseToken(token, DEFAULT_KEY);
	}

	public static Claims parseToken(String token, String generalKey) {
		return parseToken(token, generalKey(generalKey));
	}

	public static Claims parseToken(String token, SecretKey generalKey) {
		return parseClaim(token, generalKey).getPayload();
	}

	public static Jws<Claims> parseClaim(String token) {
		return parseClaim(token, generalKey(DEFAULT_KEY));
	}

	public static Jws<Claims> parseClaim(String token, SecretKey generalKey) {
		return Jwts.parser()
				.verifyWith(generalKey)
				.build()
				.parseSignedClaims(token);
	}

	/**
	 * 由字符串生成加密key
	 *
	 * @return SecretKey
	 */
	public static SecretKey generalKey(String stringKey) {
		byte[] encodedKey = Decoders.BASE64.decode(stringKey);
		return Keys.hmacShaKeyFor(encodedKey);
	}

    /*public static void main(String[] args) throws Exception {
        String key = DEFAULT_KEY;
        String authToken = "{\"userId\":10,\"userName\":\"管理员\",\"appToken\":\"CJzdWIiOiJ7XCJ1c2VySDkwMTUeyJhbGciOiJI\",\"superFlag\":false}";
        //7天有效期
        String token = JwtUtil.createToken("10", authToken, 24 * 60 * 60 * 1000 * 7, key);
        System.out.println("===========token:" + token);

        Claims claims = JwtUtil.parseToken(token, key);
        Date expirationDate = claims.getExpiration();
        System.out.println("claims.getSubject():" + claims.getSubject());
    }*/

}
