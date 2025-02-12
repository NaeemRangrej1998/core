package com.ecommerce.service;

import com.ecommerce.dto.request.DragAndDropTaskRequestDTO;
import com.ecommerce.dto.request.GetTokenClaimsDTO;
import com.ecommerce.dto.response.TaskResponseDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TaskService {
    TaskResponseDTO addTask(DragAndDropTaskRequestDTO taskRequestDTO, GetTokenClaimsDTO claimsDTO);

    List<TaskResponseDTO> getAllTask();

    TaskResponseDTO updateTask(Long taskId,DragAndDropTaskRequestDTO taskRequestDTO, GetTokenClaimsDTO claimsDTO);

    void deleteTask(Long id, GetTokenClaimsDTO claimsDTO);

    TaskResponseDTO updateStatusTask(DragAndDropTaskRequestDTO taskRequestDTO, GetTokenClaimsDTO claimsDTO);
}
