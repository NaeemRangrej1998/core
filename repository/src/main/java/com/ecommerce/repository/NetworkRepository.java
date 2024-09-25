package com.ecommerce.repository;

import com.ecommerce.entity.BatteryCapacityEntity;
import com.ecommerce.entity.NetworkEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NetworkRepository extends JpaRepository<NetworkEntity,Long> {

    Optional<NetworkEntity> findByNetwork(String network);
}
