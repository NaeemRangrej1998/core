package com.ecommerce.repository;

import com.ecommerce.entity.ResetTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<ResetTokenEntity,Long> {
    public Optional<ResetTokenEntity> findByToken(String token);

}
