package com.ecommerce.repository;

import com.ecommerce.entity.BatteryCapacityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BatteryCapacityRepository extends JpaRepository<BatteryCapacityEntity,Long> {


    Optional<BatteryCapacityEntity> findByBatteryCapacity(String batteryCapacity);
}
