package com.ecommerce.repository;

import com.ecommerce.entity.PermissionEntity;
import com.ecommerce.entity.RoleEntity;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PermissionRepository extends JpaRepository<PermissionEntity, Long> {
//    Optional<PermissionEntity> findByName(String name);

//    List<PermissionEntity> findByNameIn(List<String> names);
    List<PermissionEntity> findByIdIn(List<Long> permissionId);

    List<PermissionEntity> findByStatusAndDeactivate(Boolean status, Boolean deactivate);

    Optional<PermissionEntity> findByName(String name);
}
