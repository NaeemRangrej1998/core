package com.ecommerce.service.Impl;

import com.ecommerce.dto.request.DragAndDropTaskRequestDTO;
import com.ecommerce.dto.request.GetTokenClaimsDTO;
import com.ecommerce.dto.response.TaskResponseDTO;
import com.ecommerce.entity.DragAndDropEntity;
import com.ecommerce.entity.UserEntity;
import com.ecommerce.exception.CustomException;
import com.ecommerce.repository.TaskRepository;
import com.ecommerce.service.TaskService;
import com.ecommerce.utils.CommonUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    @Override
    public TaskResponseDTO addTask(DragAndDropTaskRequestDTO taskRequestDTO, GetTokenClaimsDTO claimsDTO) {
       try{
           DragAndDropEntity entity=getTaskByTaskName(taskRequestDTO.getTaskName());
           if (entity != null){
               throw new CustomException("Task Already Exists", HttpStatus.BAD_REQUEST);
           }
           entity=new DragAndDropEntity();
           entity.setTaskName(taskRequestDTO.getTaskName());
           entity.setTaskStatus(taskRequestDTO.getTaskStatus());
           entity.setTaskColumnName(taskRequestDTO.getTaskColumnName());
           entity.setCreatedDate(CommonUtils.getDateTime());
           entity.setUpdatedDate(CommonUtils.getDateTime());
           entity.setCreatedBy(new UserEntity(claimsDTO.getUserId()));
           entity.setUpdatedBy(new UserEntity(claimsDTO.getUserId()));
           entity.setStatus(true);
           entity.setDeactivate(false);
           System.out.println("entity = " + entity);
           taskRepository.save(entity);
           return mapToTaskResponseDTO(entity);
       }
       catch (CustomException e){
           throw new CustomException(e.getMessage(),e.getHttpStatus());
       }



    }

    @Override
    public List<TaskResponseDTO> getAllTask() {
        List<DragAndDropEntity> entities=taskRepository.getTaskByStatusAndDeactivate(true ,false);
        return entities.stream().map(this::mapToTaskResponseDTO).toList();

    }

    @Override
    public TaskResponseDTO updateTask(Long taskId, DragAndDropTaskRequestDTO taskRequestDTO, GetTokenClaimsDTO claimsDTO) {
        try {
            Optional<DragAndDropEntity> optionalEntity = getTaskById(taskId);

            if (optionalEntity.isPresent()) {
                DragAndDropEntity entity = optionalEntity.get(); // Unwrap Optional

                entity.setTaskName(taskRequestDTO.getTaskName());
                entity.setUpdatedDate(CommonUtils.getDateTime());
                entity.setUpdatedBy(new UserEntity(claimsDTO.getUserId()));

                taskRepository.save(entity);
                return mapToTaskResponseDTO(entity);
            } else {
                throw new CustomException("Task not found with ID: " + taskId, HttpStatus.NOT_FOUND);
            }
        }catch (CustomException e){
            throw new CustomException(e.getMessage(),e.getHttpStatus());
        }
    }

    @Override
    public void deleteTask(Long id, GetTokenClaimsDTO claimsDTO) {
        try {
            Optional<DragAndDropEntity> optionalEntity = getTaskById(id);

            if (optionalEntity.isPresent()) {
                DragAndDropEntity entity = optionalEntity.get(); // Unwrap Optional
                entity.setStatus(false);
                entity.setDeactivate(true);
                entity.setUpdatedDate(CommonUtils.getDateTime());
                entity.setUpdatedBy(new UserEntity(claimsDTO.getUserId()));
                taskRepository.save(entity);
            } else {
                throw new CustomException("Task not found with ID: " + id, HttpStatus.NOT_FOUND);
            }
        }catch (CustomException e){
            throw new CustomException(e.getMessage(),e.getHttpStatus());
        }
    }

    @Override
    public TaskResponseDTO updateStatusTask(DragAndDropTaskRequestDTO taskRequestDTO, GetTokenClaimsDTO claimsDTO) {
        try{
            DragAndDropEntity entity=getTaskByTaskName(taskRequestDTO.getTaskName());
            if (entity != null){
                entity.setTaskStatus(taskRequestDTO.getTaskStatus());
                entity.setTaskColumnName(taskRequestDTO.getTaskColumnName());
                entity.setUpdatedDate(CommonUtils.getDateTime());
                entity.setUpdatedBy(new UserEntity(claimsDTO.getUserId()));
                taskRepository.save(entity);
                return mapToTaskResponseDTO(entity);
            }
            else {
                throw new CustomException("Task Not  Found", HttpStatus.NOT_FOUND);

            }
        }
        catch (CustomException e){
            throw new CustomException(e.getMessage(),e.getHttpStatus());
        }
    }


    private TaskResponseDTO mapToTaskResponseDTO(DragAndDropEntity entity) {
        return new TaskResponseDTO(entity.getId(),entity.getTaskName(),entity.getTaskStatus(),entity.getTaskColumnName());
    }

    private DragAndDropEntity getTaskByTaskName(String taskName) {
        DragAndDropEntity dragAndDropEntity;
        dragAndDropEntity = taskRepository.findByTaskName(taskName);
        return dragAndDropEntity;
    }

    private Optional<DragAndDropEntity> getTaskById(Long id) {
        Optional<DragAndDropEntity> dragAndDropEntity;
        dragAndDropEntity = taskRepository.findById(id);
        return dragAndDropEntity;
    }
}
