package com.ecommerce.repository;

import com.ecommerce.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,Long> {

    Page<UserEntity> getUserEntityByStatusAndDeactivate(Boolean status, Boolean deactivate, Pageable pageable);
    Optional<UserEntity> getUserByEmail(String email);
}
