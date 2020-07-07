package com.ihrm.demo;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class CreateJwtTest {

    public static void main(String[] args) {
        JwtBuilder jwtBuilder = Jwts.builder().setId("88").setSubject("小白").setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS256, "ihrm");
        String compact = jwtBuilder.compact();
        System.out.println(compact);
    }
}
