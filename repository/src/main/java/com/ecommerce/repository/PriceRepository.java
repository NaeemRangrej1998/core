package com.ecommerce.repository;

import com.ecommerce.entity.PriceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PriceRepository extends JpaRepository<PriceEntity,Long> {
    Optional<PriceEntity> findByPrice(Double price);
}
