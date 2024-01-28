package com.project.brandprotection.mappers;


import com.project.brandprotection.dtos.requests.ProductRequestDto;
import com.project.brandprotection.dtos.responses.ProductImageDto;
import com.project.brandprotection.dtos.responses.ProductResponseDto;
import com.project.brandprotection.models.Category;
import com.project.brandprotection.models.Product;
import com.project.brandprotection.services.FileService;
import com.project.brandprotection.utils.ObjectMapperUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductMapper {

    private final ObjectMapperUtils objectMapperUtils;

    public List<ProductResponseDto> mapToDtoList(List<Product> products) {
        return products.stream().map(this::mapToDto).toList();
    }

    public Product mapToEntity(ProductRequestDto productRequestDto, Category category) {
        Product product = objectMapperUtils.mapToEntityOrDto(productRequestDto, Product.class);
        product.setCategory(category);
        return product;
    }

    public void mapToEntity(Product product, ProductRequestDto productRequestDto, Category category) {
        product.setCategory(category);
        objectMapperUtils.mapToPersistedEntity(product, productRequestDto);
    }

    public ProductResponseDto mapToDto(Product product) {
        ProductResponseDto productResponseDto = objectMapperUtils.mapToEntityOrDto(product, ProductResponseDto.class);
        productResponseDto.setCategoryId(product.getCategory().getId());
        if (StringUtils.hasText(product.getThumbnail())) {
            productResponseDto.setThumbnail(FileService.getImageUrl(product.getThumbnail()));
        }
        List<ProductImageDto> productImages = product.getProductImages().stream().map
                (productImage -> new ProductImageDto(productImage.getId(), FileService.getImageUrl(productImage.getImageName()))).toList();
        productResponseDto.setProductImages(productImages);
        return productResponseDto;
    }

}
