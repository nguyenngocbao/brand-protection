package com.project.brandprotection.repositories;

import com.project.brandprotection.models.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
    int countByProduct_Id(Long productId);
}
