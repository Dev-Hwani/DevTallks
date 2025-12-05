package com.mysite.devtallks.common.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * application.properties 에서 jwt.* 값을 바인딩 받음
 *
 * 예:
 * jwt.secret-key=your-secret-key
 * jwt.access-token-expire=1800000
 * jwt.refresh-token-expire=1209600000
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
    private String secretKey;
    private long accessTokenExpire;   // milliseconds
    private long refreshTokenExpire;  // milliseconds
}
