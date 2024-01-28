package com.project.brandprotection.services;

import com.project.brandprotection.dtos.requests.ProductRequestDto;
import com.project.brandprotection.dtos.responses.ProductImageDto;
import com.project.brandprotection.dtos.responses.ProductResponseDto;
import com.project.brandprotection.dtos.responses.PagingResponseDto;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.util.List;

public interface ProductService {
    PagingResponseDto<ProductResponseDto> getProducts(Long categoryId, String searchKey, int pageNo, int pageSize, String sortBy, String sortDir);

    ProductResponseDto getProductById(Long productId);

    ProductResponseDto createProduct(ProductRequestDto productRequestDto);

    ProductResponseDto updateProduct(Long productId, ProductRequestDto productRequestDto);

    void deleteProductById(Long productId);

    boolean existsByName(String productName);

    List<ProductImageDto> uploadProductImages(Long productId, List<MultipartFile> files);

    Resource getImage(String imageName) throws MalformedURLException;

    List<ProductResponseDto> findProductsByIds(List<Long> productIds);
}
