package com.ecommerce.entity;

import jakarta.persistence.*;
import lombok.*;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "color")
public class ColorEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "color_name",unique = true)
    private String colorName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private CategoryEntity categoryEntity;

    public ColorEntity(String colorName, CategoryEntity categoryEntity) {
        this.colorName = colorName;
        this.categoryEntity = categoryEntity;
    }
}

