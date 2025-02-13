package com.ecommerce.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@ToString
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "permissions_entity")
public class PermissionEntity extends BaseAuditEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name; // Example: "READ", "WRITE", "UPDATE", "DELETE"

    @OneToMany(mappedBy = "permission", cascade = CascadeType.ALL)
    private List<RolePermissionEntity> rolePermissions = new ArrayList<>();

}
