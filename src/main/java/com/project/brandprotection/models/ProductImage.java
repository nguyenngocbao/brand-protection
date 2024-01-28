package com.project.brandprotection.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "product_images")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductImage extends BaseEntity {
    public static final int MAXIMUM_IMAGES_PER_PRODUCT = 6;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "image_name", length = 300)
    private String imageName;
}
