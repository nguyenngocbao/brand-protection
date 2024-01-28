package com.project.brandprotection.services.impl;

import com.project.brandprotection.services.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
public class FileServiceImpl implements FileService {

    private final String UPLOADS_FOLDER = "uploads";

    @Override
    public String storeFile(MultipartFile file) {
        if (!isImageFile(file) || file.getOriginalFilename() == null) {
            throw new IllegalArgumentException("Invalid image format");
        }
        try {
            String filename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
            String uniqueFilename = UUID.randomUUID() + "_" + filename;
            java.nio.file.Path uploadDir = Paths.get(UPLOADS_FOLDER);
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }
            java.nio.file.Path destination = Paths.get(uploadDir.toString(), uniqueFilename);
            Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
            return uniqueFilename;
        } catch (IOException e) {
            log.error("Fail to upload files", e);
            throw new RuntimeException(e);
        }
    }

    private boolean isImageFile(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && contentType.startsWith("image/");
    }

}
