package com.ecommerce.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@ToString(exclude = "rolePermissions")  // Exclude the recursive relationship
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "role")
public class
RoleEntity extends BaseAuditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<RolePermissionEntity> rolePermissions = new ArrayList<>();
}
