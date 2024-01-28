package com.project.brandprotection.services;

import com.project.brandprotection.controllers.ProductController;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

public interface FileService {

    String storeFile(MultipartFile file);

    static String getImageUrl(String imageName) {
        if (!StringUtils.hasText(imageName)) {
            return null;
        }
        return MvcUriComponentsBuilder.fromMethodName(ProductController.class, "getImage", imageName).build().toUriString();

    }
}
