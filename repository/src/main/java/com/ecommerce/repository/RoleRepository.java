package com.ecommerce.repository;

import com.ecommerce.dto.response.RoleResponseDTO;
import com.ecommerce.entity.RoleEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity,Long> {
    Optional<RoleEntity> findByName(String name);

    @Query(value = "SELECT r.id, r.name FROM role r WHERE r.deactivate = FALSE", nativeQuery = true)
    List<RoleResponseDTO> getAllRoleByActiveStatus(@Param("searchValue") String searchValue , Pageable pageable);


    List<RoleEntity> findByStatusAndDeactivate(Boolean status, Boolean deactivate);
}
