package com.ecommerce.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "ram",indexes = @Index(name = "index_ram", columnList = "ram",unique = true))
public class RamEntity extends BaseAuditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ram")
    private String ram;


    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "category_id")
    private CategoryEntity categoryEntity;

    public RamEntity(String ram, CategoryEntity categoryEntity) {
        this.ram = ram;
        this.categoryEntity = categoryEntity;
    }
}
