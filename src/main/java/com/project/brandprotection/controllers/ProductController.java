package com.project.brandprotection.controllers;

import com.project.brandprotection.constants.AppConstants;
import com.project.brandprotection.dtos.requests.ProductRequestDto;
import com.project.brandprotection.dtos.responses.PagingResponseDto;
import com.project.brandprotection.dtos.responses.ProductResponseDto;
import com.project.brandprotection.services.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<?> getProducts(
            @RequestParam(value = "category_id") Long categoryId,
            @RequestParam(value = "search_key", defaultValue = AppConstants.EMPTY, required = false) String searchKey,
            @RequestParam(value = "page_no", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "page_size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sort_by", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sort_dir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ) {
        PagingResponseDto<ProductResponseDto> pagingResponseDto = productService.getProducts(categoryId, searchKey, pageNo, pageSize, sortBy, sortDir);
        return ResponseEntity.ok(pagingResponseDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable("id") Long productId) {
        ProductResponseDto productResponseDto = productService.getProductById(productId);
        return ResponseEntity.ok(productResponseDto);
    }

    @PostMapping
    public ResponseEntity<?> createProduct(@Valid @RequestBody ProductRequestDto productRequestDto) {
        ProductResponseDto productResponseDto = productService.createProduct(productRequestDto);
        return ResponseEntity.ok(productResponseDto);
    }

    @PostMapping(value = "/uploads/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadImages(@PathVariable("id") Long productId, @ModelAttribute("files") List<MultipartFile> files) {
        var productImages = productService.uploadProductImages(productId, files);
        return ResponseEntity.ok(productImages);
    }

    @GetMapping("/images/{imageName:.+}")
    public ResponseEntity<?> getImage(@PathVariable String imageName) throws MalformedURLException {
        Resource resource = productService.getImage(imageName);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(resource);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable("id") Long productId, @Valid @RequestBody ProductRequestDto productRequestDto) {
        ProductResponseDto productResponseDto = productService.updateProduct(productId, productRequestDto);
        return ResponseEntity.ok(productResponseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductById(@PathVariable("id") Long productId) {
        productService.deleteProductById(productId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/by-ids")
    public ResponseEntity<?> getProductsByIds(@RequestParam("ids") String ids) {
        List<Long> productIds = Arrays.stream(ids.split(",")).map(Long::parseLong).toList();
        List<ProductResponseDto> products = productService.findProductsByIds(productIds);
        return ResponseEntity.ok(products);
    }

}
