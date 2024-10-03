package com.ecommerce.repository;

import com.ecommerce.entity.UserEntity;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,Long> {

    List<UserEntity> getUserEntityByStatusAndDeactivate(Boolean status, Boolean deactivate);
    Optional<UserEntity> getUserByEmail(String email);
}
