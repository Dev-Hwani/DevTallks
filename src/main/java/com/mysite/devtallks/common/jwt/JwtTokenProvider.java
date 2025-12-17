package com.mysite.devtallks.common.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

import javax.crypto.SecretKey;

@Component
public class JwtTokenProvider {

    private final JwtProperties jwtProperties;
    private final UserDetailsService userDetailsService;
    private final SecretKey secretKey;

    public JwtTokenProvider(JwtProperties jwtProperties, UserDetailsService userDetailsService) {
        this.jwtProperties = jwtProperties;
        this.userDetailsService = userDetailsService;
        this.secretKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(jwtProperties.getSecretKey()));
    }

    public String generateAccessToken(String email) {
        return generateToken(email, jwtProperties.getAccessTokenExpiration());
    }

    public String generateRefreshToken(String email) {
        return generateToken(email, jwtProperties.getRefreshTokenExpiration());
    }

    public Authentication getAuthentication(String token) {
        String username = getEmailFromToken(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    public String getEmailFromToken(String token) {
        Claims claims = parseClaims(token);
        return claims.getSubject();
    }

    public String getTokenId(String token) {
        Claims claims = parseClaims(token);
        return claims.getId();
    }

    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (ExpiredJwtException e) {
            return false;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public long getAccessTokenExpirationMillis() {
        return jwtProperties.getAccessTokenExpiration();
    }

    public long getRefreshTokenExpirationMillis() {
        return jwtProperties.getRefreshTokenExpiration();
    }

    public Date getExpiration(String token) {
        return parseClaims(token).getExpiration();
    }

    public long getRemainingValidityMillis(String token) {
        Date expiry = getExpiration(token);
        long remaining = expiry.getTime() - System.currentTimeMillis();
        return Math.max(remaining, 0);
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private String generateToken(String email, long validityMillis) {
        Instant now = Instant.now();
        Instant expiry = now.plusMillis(validityMillis);

        return Jwts.builder()
                .id(UUID.randomUUID().toString())
                .subject(email)
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiry))
                .signWith(secretKey)
                .compact();
    }
}
