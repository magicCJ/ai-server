/*
 * Gongdao.com Inc.
 * Copyright (c) 2020-2034 All Rights Reserved.
 */
package com.zhibai.ai.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @Author xunbai
 * @Date 2023-05-13 17:48
 * @description
 **/
@Component
@Slf4j
public class JwtTokenProvider {

    @Value("${security.jwtSecret}")
    private String jwtSecret;

    @Value("${security.jwtExpirationInSecond}")
    private int jwtExpirationInSecond;

    public String generateToken(Long userId) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInSecond * 1000 );

        return Jwts.builder()
            .setId(String.valueOf(userId))
            .setIssuedAt(new Date())
            .setExpiration(expiryDate)
            .signWith(SignatureAlgorithm.HS512, jwtSecret)
            .compact();
    }

    public Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parser()
            .setSigningKey(jwtSecret)
            .parseClaimsJws(token)
            .getBody();

        return Long.valueOf(claims.getId());
    }

    public boolean validateToken(String authToken) {
        try {
            Jws<Claims> jws = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);

            //过期
            if (isTokenExpired(jws)){
                return false;
            }
            return true;
        } catch (Exception ex) {
            // handle invalid JWT
            log.error("validToken error", ex);
        }
        return false;
    }

    private boolean isTokenExpired(Jws<Claims> jws){
        Date expirationDate = jws.getBody().getExpiration();
        return expirationDate.before(new Date());
    }
}

