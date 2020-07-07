package com.ihrm.common.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Date;
import java.util.Map;

@Data
@ConfigurationProperties("jwt.config")
public class JwtUtils {

//    签名私钥
    private String key;
//    签名失效时间
    private Long ttl;

    public String createJwt(String id, String name, Map<String,Object> map){

//        设置失效时间
        long now = System.currentTimeMillis();
        long exp = now + ttl;
        JwtBuilder jwtBuilder = Jwts.builder().setId(id)
                .setSubject(name)
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS256, key);
        jwtBuilder.setClaims(map);
        jwtBuilder.setExpiration(new Date());
        String token = jwtBuilder.compact();
        return token;
    }

    public Claims parseJwt(String token){
        Claims claims = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
        return claims;
    }
}
