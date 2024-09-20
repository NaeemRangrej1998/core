package com.ecommerce.repository;

import com.ecommerce.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity ,Long> {
    Optional<CategoryEntity> findByCategoryName(String categoryName);
}
