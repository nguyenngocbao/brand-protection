package com.project.brandprotection.repositories;

import com.project.brandprotection.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
