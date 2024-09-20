package com.ecommerce.repository;

import com.ecommerce.entity.CategoryEntity;
import com.ecommerce.entity.ColorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ColorRepository extends JpaRepository<ColorEntity,Long> {
    Optional<ColorEntity> findByColorName(String colorName);

}
