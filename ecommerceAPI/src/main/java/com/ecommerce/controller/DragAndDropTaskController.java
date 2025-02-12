package com.ecommerce.controller;

import com.ecommerce.dto.request.DragAndDropTaskRequestDTO;
import com.ecommerce.dto.request.GetTokenClaimsDTO;
import com.ecommerce.dto.response.ApiResponse;
import com.ecommerce.dto.response.TaskResponseDTO;
import com.ecommerce.service.TaskService;
import com.ecommerce.service.UserService;
import com.ecommerce.service.jwt.JwtTokenProvider;
import com.ecommerce.utils.GetClaimsUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/task")
public class DragAndDropTaskController {
    private final TaskService taskService;
    private final GetClaimsUtils claimsUtils;
    private final JwtTokenProvider jwtTokenProvider;

    public DragAndDropTaskController(TaskService taskService, GetClaimsUtils claimsUtils, JwtTokenProvider jwtTokenProvider) {
        this.taskService = taskService;
        this.claimsUtils = claimsUtils;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/addTask")
    public ResponseEntity<ApiResponse> AddTask(@RequestBody DragAndDropTaskRequestDTO  taskRequestDTO, HttpServletRequest request){
        GetTokenClaimsDTO claimsDTO = claimsUtils.getClaims(request);
        TaskResponseDTO taskResponseDTO=taskService.addTask(taskRequestDTO,claimsDTO);
        return ResponseEntity.ok(new ApiResponse(HttpStatus.OK, "Task Added Successfully", taskResponseDTO));
    }

    @GetMapping("/getAllTasks")
    public ResponseEntity<ApiResponse>getAllTask(){
        List<TaskResponseDTO> taskResponseDTO=taskService.getAllTask();
        return ResponseEntity.ok(new ApiResponse(HttpStatus.OK, "Task Retrieve Successfully", taskResponseDTO));
    }

    @PutMapping("/updateTask/{id}")
    public ResponseEntity<ApiResponse> updateTask(@PathVariable("id") Long id, @RequestBody DragAndDropTaskRequestDTO  taskRequestDTO, HttpServletRequest request){
        GetTokenClaimsDTO claimsDTO = claimsUtils.getClaims(request);
        TaskResponseDTO taskResponseDTO=taskService.updateTask(id,taskRequestDTO,claimsDTO);
        return ResponseEntity.ok(new ApiResponse(HttpStatus.OK, "Task Updated Successfully", taskResponseDTO));
    }

    @DeleteMapping("/deleteTask/{id}")
    public ResponseEntity<ApiResponse> deleteTask(@PathVariable("id") Long id, HttpServletRequest request){
        GetTokenClaimsDTO claimsDTO = claimsUtils.getClaims(request);
        taskService.deleteTask(id,claimsDTO);
        return ResponseEntity.ok(new ApiResponse(HttpStatus.OK, "Task Deleted Successfully"));
    }
    @PutMapping("/updateTaskStatus")
    public ResponseEntity<ApiResponse> updateStatusTask(@RequestBody DragAndDropTaskRequestDTO  taskRequestDTO, HttpServletRequest request){
        GetTokenClaimsDTO claimsDTO = claimsUtils.getClaims(request);
        TaskResponseDTO taskResponseDTO=taskService.updateStatusTask(taskRequestDTO,claimsDTO);
        return ResponseEntity.ok(new ApiResponse(HttpStatus.OK, "Task Status Updated Successfully", taskResponseDTO));
    }

}
