package com.ecommerce.repository;

import com.ecommerce.entity.BatteryCapacityEntity;
import com.ecommerce.entity.ProcessorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProcessorRepository extends JpaRepository<ProcessorEntity,Long> {
    Optional<ProcessorEntity> findByProcessorName(String processor);
}
