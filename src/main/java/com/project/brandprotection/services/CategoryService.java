package com.project.brandprotection.services;

import com.project.brandprotection.dtos.requests.CategoryRequestDto;
import com.project.brandprotection.dtos.responses.CategoryResponseDto;

import java.util.List;

public interface CategoryService {
    CategoryResponseDto getCategoryById(Long categoryId);

    CategoryResponseDto updateCategory(Long categoryId, CategoryRequestDto categoryRequestDto);

    void deleteCategoryById(Long categoryId);

    List<CategoryResponseDto> getAllCategories();

    CategoryResponseDto createCategory(CategoryRequestDto categoryRequestDto);
}
