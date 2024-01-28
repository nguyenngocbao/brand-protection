package com.project.brandprotection.repositories;

import com.project.brandprotection.models.Category;
import com.project.brandprotection.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p FROM Product p " +
            "WHERE (p.name LIKE CONCAT('%', :searchKey, '%') OR p.description LIKE CONCAT('%', :searchKey, '%')) " +
            "AND p.active = true AND p.category.id = :categoryId ")
    Page<Product> searchProducts(Long categoryId, String searchKey, Pageable pageable);

    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.productImages WHERE p.id = :id AND p.active = true")
    Optional<Product> findProductWithImagesById(Long id);

    List<Product> findByCategory(Category category);

    boolean existsByName(String productName);

    @Query("SELECT p FROM Product p JOIN FETCH p.category WHERE p.id IN :ids")
    List<Product> findByIds(List<Long> ids);

}
