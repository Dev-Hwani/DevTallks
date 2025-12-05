package com.mysite.devtallks.common.file;

import com.mysite.devtallks.common.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;

@Component
public class FileValidator {

    private final FileProperties fileProperties;

    public FileValidator(FileProperties fileProperties) {
        this.fileProperties = fileProperties;
    }

    public void validate(MultipartFile file) {
        if (file.isEmpty()) {
            throw new BusinessException("파일이 비어있습니다.", HttpStatus.BAD_REQUEST);
        }

        if (file.getSize() > fileProperties.getMaxFileSize()) {
            throw new BusinessException("파일 용량이 너무 큽니다. 최대 허용 용량: " 
                                        + fileProperties.getMaxFileSize() + "바이트", HttpStatus.BAD_REQUEST);
        }

        String extension = getExtension(file.getOriginalFilename());
        if (!Arrays.asList(fileProperties.getAllowedExtensions()).contains(extension.toLowerCase())) {
            throw new BusinessException("허용되지 않은 파일 확장자입니다. (" + extension + ")", HttpStatus.BAD_REQUEST);
        }
    }

    private String getExtension(String fileName) {
        int idx = fileName.lastIndexOf(".");
        if (idx == -1) return "";
        return fileName.substring(idx + 1);
    }
}
