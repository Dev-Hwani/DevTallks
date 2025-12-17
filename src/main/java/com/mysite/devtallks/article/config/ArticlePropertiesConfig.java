package com.mysite.devtallks.article.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ArticlePropertiesConfig {

    @Bean(name = "articleViewCacheProperties")
    @ConfigurationProperties(prefix = "article.view-cache")
    public ArticleViewCacheProperties articleViewCacheProperties() {
        return new ArticleViewCacheProperties();
    }
}
