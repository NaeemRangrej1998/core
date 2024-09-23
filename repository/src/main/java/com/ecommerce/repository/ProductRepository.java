package com.ecommerce.repository;

import com.ecommerce.dto.ExcelDTO;
import com.ecommerce.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    @Query("SELECT product FROM ProductEntity product WHERE product.categoryEntity = :categoryEntity " +
            "AND product.modelEntity = :#{#excelDTO.modelEntity} " +
            "AND product.colorEntity = :#{#excelDTO.colorEntity} " +
            "AND product.ramEntity = :#{#excelDTO.ramEntity} " +
            "AND product.internalStorageEntity = :#{#excelDTO.internalStorageEntity}")
    Optional<ProductEntity> findByAllEntities(ExcelDTO excelDTO);

//    @Query("SELECT product FROM ProductEntity product WHERE product.categoryEntity = :categoryEntity " +
//            "AND product.modelEntity = :modelEntity " +
//            "AND product.colorEntity = :colorEntity " +
//            "AND product.ramEntity = :ramEntity " +
//            "AND product.internalStorageEntity = :internalStorageEntity")
//    Optional<ProductEntity> findByAllEntities(CategoryEntity categoryEntity, ModelEntity modelEntity,
//                                              ColorEntity colorEntity, RamEntity ramEntity,
//                                              InternalStorageEntity internalStorageEntity);
}

