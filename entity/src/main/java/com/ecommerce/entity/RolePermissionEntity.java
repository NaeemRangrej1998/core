package com.ecommerce.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@ToString
@Entity
@NoArgsConstructor
@AllArgsConstructor
//@RequiredArgsConstructor
@Table(name = "role_permissions")
public class RolePermissionEntity extends BaseAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private RoleEntity role;

    @ManyToOne
    @JoinColumn(name = "permission_id", nullable = false)
    private PermissionEntity permission;

    // Custom constructor
    public RolePermissionEntity(RoleEntity role, PermissionEntity permission) {
        this.role = role;
        this.permission = permission;
    }
}
