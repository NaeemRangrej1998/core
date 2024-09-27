package com.ecommerce.controller;

import com.ecommerce.dto.request.RoleRequestDTO;
import com.ecommerce.dto.response.ApiResponse;
import com.ecommerce.dto.response.RoleResponseDTO;
import com.ecommerce.service.RoleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/role")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }


    @PostMapping("/addRole")
    public ResponseEntity<ApiResponse> addUser(@RequestBody RoleRequestDTO roleRequestDTO) {
        RoleResponseDTO addUserResponseDTO = roleService.addRole(roleRequestDTO);
        return ResponseEntity.ok(new ApiResponse(HttpStatus.OK, "Role Added", addUserResponseDTO));
    }

    @GetMapping("/getAllRole")
    public ResponseEntity<ApiResponse> getAllRole() {
    List<RoleResponseDTO> response = roleService.getAllRoles();
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "Role Found Successfully", response), HttpStatus.OK);
    }

    @GetMapping("/getRoleById/{id}")
    public ResponseEntity<ApiResponse> getRoleById(@PathVariable(value = "id") Long id) {
        RoleResponseDTO response  = this.roleService.getRoleById(id);
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "Role Found Successfully", response), HttpStatus.OK);
    }

    @PutMapping("/updateRole/{id}")
    public ResponseEntity<ApiResponse> updateRole(@PathVariable(value = "id") Long id,@RequestBody RoleRequestDTO roleRequestDTO) {
        RoleResponseDTO response  = this.roleService.updateRoleById(id, roleRequestDTO);
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "Role Found Successfully", response), HttpStatus.OK);
    }


}
