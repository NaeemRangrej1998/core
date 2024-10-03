package com.ecommerce.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "model",indexes = @Index(name = "index_model", columnList = "modalName",unique = true))
public class ModelEntity extends BaseAuditEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "model_name")
    private String modalName;


    @ManyToOne(fetch = FetchType.LAZY ,cascade = CascadeType.ALL)
    @JoinColumn(name = "category_id")
    private CategoryEntity categoryEntity;

    public ModelEntity(String modalName, CategoryEntity categoryEntity) {
        this.modalName = modalName;
        this.categoryEntity = categoryEntity;
    }
}
