package com.ecommerce.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "product")
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private CategoryEntity categoryEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "model_id")
    private ModelEntity modelEntity ;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "color_id")
    private  ColorEntity colorEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ram_id")
    private  RamEntity ramEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "internal_storage_id")
    private InternalStorageEntity internalStorageEntity;


    public ProductEntity(CategoryEntity categoryEntity, ModelEntity modelEntity, ColorEntity colorEntity, RamEntity ramEntity, InternalStorageEntity internalStorageEntity) {
        this.categoryEntity = categoryEntity;
        this.modelEntity = modelEntity;
        this.colorEntity = colorEntity;
        this.ramEntity = ramEntity;
        this.internalStorageEntity = internalStorageEntity;
    }


}
