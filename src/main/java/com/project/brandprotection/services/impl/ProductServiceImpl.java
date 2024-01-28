package com.project.brandprotection.services.impl;

import com.project.brandprotection.dtos.requests.ProductRequestDto;
import com.project.brandprotection.dtos.responses.ProductImageDto;
import com.project.brandprotection.dtos.responses.ProductResponseDto;
import com.project.brandprotection.dtos.responses.PagingResponseDto;
import com.project.brandprotection.exceptions.BadRequestException;
import com.project.brandprotection.exceptions.ResourceNotFoundException;
import com.project.brandprotection.mappers.ProductMapper;
import com.project.brandprotection.models.Category;
import com.project.brandprotection.models.Product;
import com.project.brandprotection.models.ProductImage;
import com.project.brandprotection.repositories.CategoryRepository;
import com.project.brandprotection.repositories.ProductImageRepository;
import com.project.brandprotection.repositories.ProductRepository;
import com.project.brandprotection.services.FileService;
import com.project.brandprotection.services.ProductService;
import com.project.brandprotection.utils.PageableUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;
    private final ProductMapper productMapper;
    private final FileService fileService;

    @Override
    public PagingResponseDto<ProductResponseDto> getProducts(Long categoryId, String searchKey, int pageNo, int pageSize, String sortBy, String sortDir) {
        Pageable pageable = PageableUtils.getPageable(pageNo, pageSize, sortBy, sortDir);
        Page<Product> products = productRepository.searchProducts(categoryId, searchKey, pageable);
        List<Product> listOfItems = products.getContent();
        List<ProductResponseDto> content = productMapper.mapToDtoList(listOfItems);
        return new PagingResponseDto<>(products, content);
    }

    @Override
    public ProductResponseDto getProductById(Long productId) {
        Product product = productRepository.findProductWithImagesById(productId).orElseThrow(() -> new ResourceNotFoundException("Product", productId));
        return productMapper.mapToDto(product);
    }

    @Override
    public ProductResponseDto createProduct(ProductRequestDto productRequestDto) {
        Category category = findCategoryById(productRequestDto.getCategoryId());
        Product product = productMapper.mapToEntity(productRequestDto, category);
        Product savedProduct = productRepository.save(product);
        return productMapper.mapToDto(savedProduct);
    }

    @Override
    public ProductResponseDto updateProduct(Long productId, ProductRequestDto productRequestDto) {
        Product product = findProductById(productId);
        Category category = findCategoryById(productRequestDto.getCategoryId());
        productMapper.mapToEntity(product, productRequestDto, category);
        Product savedProduct = productRepository.save(product);
        return productMapper.mapToDto(savedProduct);
    }

    @Override
    public void deleteProductById(Long productId) {
        Product product = findProductById(productId);
        product.setActive(false);
        productRepository.save(product);
    }

    @Override
    public boolean existsByName(String productName) {
        return productRepository.existsByName(productName);
    }

    @Override
    @Transactional
    public List<ProductImageDto> uploadProductImages(Long productId, List<MultipartFile> files) {
        Product existingProduct = findProductById(productId);
        files = files == null ? new ArrayList<>() : files;
        if (files.size() > ProductImage.MAXIMUM_IMAGES_PER_PRODUCT) {
            throw new BadRequestException("Maximum product images is " + ProductImage.MAXIMUM_IMAGES_PER_PRODUCT);
        }
        List<ProductImageDto> productImages = new ArrayList<>();
        for (MultipartFile file : files) {
            if (file.getSize() == 0) {
                continue;
            }
            if (file.getSize() > 10 * 1024 * 1024) { // size > 10MB
                throw new BadRequestException("Size is too large!");

            }
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                throw new BadRequestException("Invalid content type!");
            }
            String filename = fileService.storeFile(file);
            ProductImageDto productImage = createProductImage(existingProduct.getId(), filename);
            productImages.add(productImage);
        }
        return productImages;
    }

    @Override
    public Resource getImage(String imageName) throws MalformedURLException {
        java.nio.file.Path imagePath = Paths.get("uploads/" + imageName);
        return new UrlResource(imagePath.toUri());
    }

    @Override
    public List<ProductResponseDto> findProductsByIds(List<Long> productIds) {
        return productIds.stream().map(this::getProductById).toList();
    }

    private ProductImageDto createProductImage(Long productId, String imageName) {
        Product existingProduct = productRepository
                .findById(productId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Cannot find product with id: " + productId));
        ProductImage newProductImage = ProductImage.builder()
                .product(existingProduct)
                .imageName(imageName)
                .build();
        int size = productImageRepository.countByProduct_Id(productId);
        if (size >= ProductImage.MAXIMUM_IMAGES_PER_PRODUCT) {
            throw new IllegalArgumentException(
                    "Number of images must be <= " + ProductImage.MAXIMUM_IMAGES_PER_PRODUCT);
        }
        if (!StringUtils.hasText(existingProduct.getThumbnail())) {
            existingProduct.setThumbnail(imageName);
        }
        productRepository.save(existingProduct);
        var savedProductImage = productImageRepository.save(newProductImage);
        return ProductImageDto.builder()
                .imageUrl(FileService.getImageUrl(imageName))
                .productImageId(savedProductImage.getId())
                .build();
    }

    private Product findProductById(Long productId) {
        return productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product", productId));
    }

    private Category findCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category", categoryId));
    }


}
