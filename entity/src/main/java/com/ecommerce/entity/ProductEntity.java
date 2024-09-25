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
public class ProductEntity extends BaseAuditEntity {
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id")
    private BrandEntity brandEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "network_id")
    private NetworkEntity networkEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sim_slot_id")
    private  SimSlotEntity simSlotEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "screen_size_id")
    private ScreenSizeEntity screenSizeEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "battery_capacity_id")
    private BatteryCapacityEntity batteryCapacityEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "processor_id")
    private ProcessorEntity processorEntity;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "price_id")
//    private PriceEntity priceEntity;


    public ProductEntity(CategoryEntity categoryEntity, ModelEntity modelEntity, ColorEntity colorEntity, RamEntity ramEntity, InternalStorageEntity internalStorageEntity) {
        this.categoryEntity = categoryEntity;
        this.modelEntity = modelEntity;
        this.colorEntity = colorEntity;
        this.ramEntity = ramEntity;
        this.internalStorageEntity = internalStorageEntity;
    }


}
