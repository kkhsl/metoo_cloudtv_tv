package com.cloud.tv.core.jwt.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Map;

/**
 * <p>
 * Title: JwtUtil
 * </p>
 *
 * <p>
 * Description: JWT 工具类
 * </p>
 *
 * <author>
 * HKK
 * </author>
 */
@Component
public class JwtUtil {

    private Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    private static final String SING = "!WR$%^YGB#$FS";

    /**
     * 生成token
     *
     * @return
     */
    public static String getToken(Map<String, String> map) {

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 7);

        // 创建JWT Builder
        JWTCreator.Builder builder = JWT.create();

        // Payload
        map.forEach((k, v) -> {
            builder.withClaim(k, v);
        });
        String token = builder.withExpiresAt(calendar.getTime()).sign(Algorithm.HMAC256(SING));

       /* String token = JWT.create().withHeader(map)
                .withClaim("userId", 10)
                .withClaim("userName", "Hkk")
                .withExpiresAt(calendar.getTime())
                .sign(Algorithm.HMAC256(SING));*/
        System.out.println("JWTToken: " + token);
        return token;
    }


    /**
     * 验证token 合法性
     */
    public static DecodedJWT verifyJwt(String token) {

        return JWT.require(Algorithm.HMAC256(SING)).build().verify(token);

        // 创建验证对象

        /*JWTVerifier jwtVerifier =  JWT.require(Algorithm.HMAC256(SING)).build().verify(token);*/

        /*DecodedJWT DeCodeJWT = jwtVerifier.verify("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyTmFtZSI6IkhrayIsImV4cCI6MTYxNzk0NjQxMCwidXNlcklkIjoxMH0.0Dnk4oeHbDHiGbaXMALrfBOMDG7-7JoWK5uic8_2bTU");
        System.out.println("head: " + DeCodeJWT.getHeader());
        System.out.println("userId: " + DeCodeJWT.getClaim("userId").asInt());
        System.out.println("userName: " + DeCodeJWT.getClaim("userName").asString());
        System.out.println("Signature: " + DeCodeJWT.getSignature());
        System.out.println("ExpiresAt: " + DeCodeJWT.getExpiresAt());*/
    }

    /**
     * 获取token信息
     *
     * @param token
     * @return
     */
    public static DecodedJWT getDecodedJWT(String token) {
        /*JWTVerifier jwtVerifier =  JWT.require(Algorithm.HMAC256(SING)).build();
        DecodedJWT DeCodeJWT = jwtVerifier.verify(token);*/
        DecodedJWT DeCodeJWT = JWT.require(Algorithm.HMAC256(SING)).build().verify(token);

        return DeCodeJWT;
    }

}
