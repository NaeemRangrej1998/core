package com.ecommerce.repository;

import com.ecommerce.entity.RoleEntity;
import com.ecommerce.entity.RolePermissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RolePermissionRepository extends JpaRepository<RolePermissionEntity, Long> {
    List<RolePermissionEntity> findByRole(RoleEntity role);

    List<RolePermissionEntity> findByRoleAndStatusTrueAndDeactivateFalse(RoleEntity role);

}
