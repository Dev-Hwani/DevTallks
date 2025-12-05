package com.mysite.devtallks.common.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtTokenProvider {

	@Value("${jwt.secret-key}")
	private String secret;

	@Value("${jwt.access-token-expire}")
	private long accessExpiration;

	@Value("${jwt.refresh-token-expire}")
	private long refreshExpiration;

    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Access Token 생성
     */
    public String createAccessToken(String username) {
        return createToken(username, accessExpiration);
    }

    /**
     * Refresh Token 생성
     */
    public String createRefreshToken(String username) {
        return createToken(username, refreshExpiration);
    }

    /**
     * JWT 생성 공통 로직
     */
    private String createToken(String username, long validityInMs) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + validityInMs);

        return Jwts.builder()
                .subject(username)
                .issuedAt(now)
                .expiration(expiration)
                .signWith(secretKey, Jwts.SIG.HS256)     // ⭐ 0.12.x 방식
                .compact();
    }

    /**
     * JWT 검증
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(secretKey)              // ⭐ 0.12.x 방식
                    .build()
                    .parseSignedClaims(token);
            return true;

        } catch (ExpiredJwtException e) {
            return false;

        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Claims 조회
     */
    public Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)                  // ⭐
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String getUsername(String token) {
        return getClaims(token).getSubject();
    }
    
    public String getSubject(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

}
