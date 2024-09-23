package com.ecommerce.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "ram")
public class RamEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ram", unique = true)
    private String ram;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private CategoryEntity categoryEntity;

    public RamEntity(String ram, CategoryEntity categoryEntity) {
        this.ram = ram;
        this.categoryEntity = categoryEntity;
    }
}
