package com.ecommerce.repository;

import com.ecommerce.entity.RoleMappingEntity;
import com.ecommerce.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleMappingRepository extends JpaRepository<RoleMappingEntity,Long> {


    Optional<RoleMappingEntity> findByUserEntity(UserEntity userEntity);
}
