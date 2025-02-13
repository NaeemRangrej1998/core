package com.ecommerce.repository;

import com.ecommerce.entity.PermissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PermissionRepository extends JpaRepository<PermissionEntity, Long> {
//    Optional<PermissionEntity> findByName(String name);

    List<PermissionEntity> findByNameIn(List<String> names);

}
