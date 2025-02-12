package com.ecommerce.entity;

import jakarta.persistence.*;
import lombok.*;

@ToString
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "drag_and_drop")
public class DragAndDropEntity extends BaseAuditEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "task_name", nullable = true)
    private String taskName;

    @Column(name = "task_status", nullable = true)
    private String taskStatus;

    @Column(name = "task_column_name", nullable = true)
    private String taskColumnName;
}
