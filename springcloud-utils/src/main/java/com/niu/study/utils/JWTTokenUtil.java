package com.niu.study.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.jsonwebtoken.InvalidClaimException;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JWTTokenUtil {

    private static final String SECRET = "niuniuzuikeaila200010021211520##@@*&^**^^%&";

    /**
     * 生成jwtToken
     * @param claims
     * @param maxLife
     * @return
     */
    public static String createToken(Map<String,String> claims, long maxLife) {
        try {
            Map<String, Object> header = new HashMap<String, Object>();
            header.put("alg","HS256");
            header.put("typ","JWT");
            JWTCreator.Builder b=JWT.create().withHeader(header);

            if(claims!=null&&claims.size()>0){
                for(String key:claims.keySet()){
                    b.withClaim(key,claims.get(key));
                }
            }
            b.withExpiresAt(new Date(System.currentTimeMillis()+maxLife));//有效时间 即改token 有效时长

            String token=b.sign(Algorithm.HMAC256(SECRET));//加密
            return token;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("根据token获取对象失败");
        }
    }

    /**
     * 获取对象
     * @param jwt_token
     * @return token
     */
    public static Map<String,String> verifyTokenAndGetClaims(String jwt_token) {
        try {
            Map<String,String> result = new HashMap<String,String>();
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SECRET)).build();
            DecodedJWT jwt = verifier.verify(jwt_token);
            Map<String, Claim> claims=jwt.getClaims();
            for (String key : claims.keySet()) {
                result.put(key, claims.get(key).asString());
            }
            return result;
        } catch (InvalidClaimException e) {
            e.printStackTrace();
            throw new RuntimeException("无效的jwt_token");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("校验jwt_token令牌失败");
        }
    }
}