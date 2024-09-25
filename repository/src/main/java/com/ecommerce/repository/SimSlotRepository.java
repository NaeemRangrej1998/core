package com.ecommerce.repository;

import com.ecommerce.entity.BatteryCapacityEntity;
import com.ecommerce.entity.SimSlotEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SimSlotRepository extends JpaRepository<SimSlotEntity,Long> {
    Optional<SimSlotEntity> findBySimSlot(String simSlot);
}
