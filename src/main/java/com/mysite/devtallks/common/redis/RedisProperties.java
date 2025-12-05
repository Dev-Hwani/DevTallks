package com.mysite.devtallks.common.redis;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * application.properties 또는 application.yml에서 redis 관련 설정을 바인딩한다.
 *
 * 예 (application.properties):
 * spring.data.redis.host=localhost
 * spring.data.redis.port=6379
 *
 * 추가적으로 필요한 설정을 이곳에 두면 유연하게 사용 가능.
 */
@Component
@ConfigurationProperties(prefix = "spring.data.redis")
public class RedisProperties {

    private String host = "localhost";
    private int port = 6379;
    private String password;
    private int database = 0;

    // Cache default TTL (초)
    private long defaultTtlSeconds = 3600;

    // --- getters / setters ---
    public String getHost() { return host; }
    public void setHost(String host) { this.host = host; }

    public int getPort() { return port; }
    public void setPort(int port) { this.port = port; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public int getDatabase() { return database; }
    public void setDatabase(int database) { this.database = database; }

    public long getDefaultTtlSeconds() { return defaultTtlSeconds; }
    public void setDefaultTtlSeconds(long defaultTtlSeconds) { this.defaultTtlSeconds = defaultTtlSeconds; }
}
