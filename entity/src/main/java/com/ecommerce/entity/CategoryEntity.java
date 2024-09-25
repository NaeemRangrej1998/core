package com.ecommerce.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "category" ,indexes = @Index(name = "index_category", columnList = "categoryName",unique = true))
public class CategoryEntity extends BaseAuditEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "category_name")
    private String categoryName;

    public CategoryEntity(String categoryName) {
        this.categoryName = categoryName;
    }
}
