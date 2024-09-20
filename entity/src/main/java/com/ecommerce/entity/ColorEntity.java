package com.ecommerce.entity;

import jakarta.persistence.*;
import lombok.*;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ColorEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "color_name")
    private String colorName;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "category_id")
    private CategoryEntity categoryEntity;
}

