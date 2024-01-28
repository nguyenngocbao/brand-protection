package com.project.brandprotection.services.impl;

import com.project.brandprotection.exceptions.ResourceNotFoundException;
import com.project.brandprotection.models.Product;
import com.project.brandprotection.repositories.ProductRepository;
import com.project.brandprotection.utils.ObjectMapperUtils;
import com.project.brandprotection.dtos.requests.CategoryRequestDto;
import com.project.brandprotection.dtos.responses.CategoryResponseDto;
import com.project.brandprotection.models.Category;
import com.project.brandprotection.repositories.CategoryRepository;
import com.project.brandprotection.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final ObjectMapperUtils objectMapperUtils;

    @Override
    public CategoryResponseDto createCategory(CategoryRequestDto categoryRequestDto) {
        Category category = objectMapperUtils.mapToEntityOrDto(categoryRequestDto, Category.class);
        return objectMapperUtils.mapToEntityOrDto(categoryRepository.save(category), CategoryResponseDto.class);
    }

    @Override
    public CategoryResponseDto getCategoryById(Long categoryId) {
        Category category = getCategory(categoryId);
        return objectMapperUtils.mapToEntityOrDto(category, CategoryResponseDto.class);
    }

    @Override
    public CategoryResponseDto updateCategory(Long categoryId, CategoryRequestDto categoryRequestDto) {
        Category category = getCategory(categoryId);
        category.setName(categoryRequestDto.getName());
        return objectMapperUtils.mapToEntityOrDto(categoryRepository.save(category), CategoryResponseDto.class);
    }

    @Override
    public void deleteCategoryById(Long categoryId) {
        Category category = getCategory(categoryId);
        List<Product> products = productRepository.findByCategory(category);
        if (!products.isEmpty()) {
            throw new IllegalStateException("Cannot delete category with associated products");
        }
        categoryRepository.delete(category);

    }

    @Override
    public List<CategoryResponseDto> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return objectMapperUtils.mapAll(categories, CategoryResponseDto.class);
    }

    private Category getCategory(Long categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category", categoryId));
    }

}
