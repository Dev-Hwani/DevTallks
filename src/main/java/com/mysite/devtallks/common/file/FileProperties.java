package com.mysite.devtallks.common.file;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Component
@ConfigurationProperties(prefix = "file")
public class FileProperties {

    private String uploadDir; // 로컬 업로드 경로
    private long maxFileSize; // 최대 파일 크기
    private String[] allowedExtensions; // 허용 확장자

    public void setUploadDir(String uploadDir) {
        this.uploadDir = uploadDir;
    }

    public void setMaxFileSize(long maxFileSize) {
        this.maxFileSize = maxFileSize;
    }

    public void setAllowedExtensions(String[] allowedExtensions) {
        this.allowedExtensions = allowedExtensions;
    }
}
