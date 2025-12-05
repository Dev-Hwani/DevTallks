package com.mysite.devtallks.common.file;

import com.mysite.devtallks.common.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileUploadService {

    private final FileProperties fileProperties;
    private final FileValidator fileValidator;

    public FileUploadService(FileProperties fileProperties, FileValidator fileValidator) {
        this.fileProperties = fileProperties;
        this.fileValidator = fileValidator;
    }

    /**
     * 파일 업로드
     */
    public String uploadFile(MultipartFile file) {
        fileValidator.validate(file);

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        Path uploadPath = Paths.get(fileProperties.getUploadDir());
        if (!Files.exists(uploadPath)) {
            try {
                Files.createDirectories(uploadPath);
            } catch (IOException e) {
                throw new BusinessException("파일 업로드 디렉토리 생성 실패", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        Path filePath = uploadPath.resolve(fileName);
        try {
            file.transferTo(filePath);
        } catch (IOException e) {
            throw new BusinessException("파일 업로드 실패: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return filePath.toString();
    }

    /**
     * 파일 삭제
     */
    public void deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            if (!file.delete()) {
                throw new BusinessException("파일 삭제 실패", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }
}
