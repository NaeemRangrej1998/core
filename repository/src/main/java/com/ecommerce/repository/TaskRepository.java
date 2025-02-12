package com.ecommerce.repository;

import com.ecommerce.entity.DragAndDropEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<DragAndDropEntity,Long> {
    DragAndDropEntity findByTaskName(String taskName);

    List<DragAndDropEntity> getTaskByStatusAndDeactivate(Boolean status, Boolean deactivate);

}
