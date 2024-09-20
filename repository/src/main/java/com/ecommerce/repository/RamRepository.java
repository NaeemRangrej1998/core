package com.ecommerce.repository;

import com.ecommerce.entity.CategoryEntity;
import com.ecommerce.entity.RamEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RamRepository  extends JpaRepository<RamEntity,Long>  {
    Optional<RamEntity> findByRam(String ram);
}
