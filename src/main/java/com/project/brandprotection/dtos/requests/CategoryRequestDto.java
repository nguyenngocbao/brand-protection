package com.project.brandprotection.dtos.requests;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryRequestDto {
    @NotEmpty(message = "Category name cannot be empty")
    private String name;
}
