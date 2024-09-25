package com.ecommerce.repository;

import com.ecommerce.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {


//    Optional<ProductEntity> findByCategoryEntityAndModelEntityAndColorEntityAndRamEntityAndInternalStorageEntity(CategoryEntity categoryEntity, ModelEntity modelEntity,
//                                           ColorEntity colorEntity, RamEntity ramEntity,
//                                           InternalStorageEntity internalStorageEntity);


    @Query("SELECT product FROM ProductEntity product WHERE product.categoryEntity = :categoryEntity " +
            "AND product.brandEntity = :brandEntity " +
            "AND product.modelEntity = :modelEntity " +
            "AND product.colorEntity = :colorEntity " +
            "AND product.ramEntity = :ramEntity " +
            "AND product.internalStorageEntity = :internalStorageEntity " +
            "AND product.networkEntity = :networkEntity " +
            "AND product.simSlotEntity = :simSlotEntity " +
            "AND product.screenSizeEntity = :screenSizeEntity " +
            "AND product.batteryCapacityEntity = :batteryCapacityEntity " +
            "AND product.processorEntity = :processorEntity ")
    Optional<ProductEntity> findByEntities(CategoryEntity categoryEntity, BrandEntity brandEntity, ModelEntity modelEntity,
                                           ColorEntity colorEntity, RamEntity ramEntity,
                                           InternalStorageEntity internalStorageEntity,
                                           NetworkEntity networkEntity, SimSlotEntity simSlotEntity,
                                           ScreenSizeEntity screenSizeEntity, BatteryCapacityEntity
                                           batteryCapacityEntity, ProcessorEntity processorEntity);

}

