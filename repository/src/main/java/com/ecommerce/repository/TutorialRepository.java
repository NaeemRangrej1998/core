package com.ecommerce.repository;

import com.ecommerce.entity.TutorialEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TutorialRepository extends JpaRepository<TutorialEntity, Long> {
}
