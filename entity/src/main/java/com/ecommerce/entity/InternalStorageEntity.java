package com.ecommerce.entity;

import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "internal_storage")
public class InternalStorageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "internal_storage", unique = true)
    private String  internalStorage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private CategoryEntity categoryEntity;

    public InternalStorageEntity(String internalStorage, CategoryEntity categoryEntity) {
        this.internalStorage = internalStorage;
        this.categoryEntity = categoryEntity;
    }
}
