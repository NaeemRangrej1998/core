package com.ecommerce.controller;

import com.ecommerce.dto.request.GetTokenClaimsDTO;
import com.ecommerce.dto.request.RoleRequestDTO;
import com.ecommerce.dto.response.ApiResponse;
import com.ecommerce.dto.response.RoleResponseDTO;
import com.ecommerce.service.RoleService;
import com.ecommerce.utils.GetClaimsUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/role")
@CrossOrigin("http://localhost:3000")
public class RoleController {

    private final RoleService roleService;
    private final GetClaimsUtils claimsUtils;

    public RoleController(RoleService roleService, GetClaimsUtils claimsUtils) {
        this.roleService = roleService;
        this.claimsUtils = claimsUtils;
    }


    @PostMapping("/addRole")
    public ResponseEntity<ApiResponse> addUser(@RequestBody RoleRequestDTO roleRequestDTO, HttpServletRequest request) {
        GetTokenClaimsDTO claimsDTO = claimsUtils.getClaims(request);
        RoleResponseDTO addUserResponseDTO = roleService.addRole(roleRequestDTO,claimsDTO);
        return ResponseEntity.ok(new ApiResponse(HttpStatus.OK, "Role Saved  Successfully", addUserResponseDTO));
    }

    @GetMapping("/getAllRole")
    public ResponseEntity<ApiResponse> getAllRole() {
    List<RoleResponseDTO> response = roleService.getAllRoles();
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "Get Role Successfully", response), HttpStatus.OK);
    }

    @GetMapping("/getRoleById/{id}")
    public ResponseEntity<ApiResponse> getRoleById(@PathVariable(value = "id") Long id, HttpServletRequest request) {
        RoleResponseDTO response  = this.roleService.getRoleById(id);
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "Get Role Successfully", response), HttpStatus.OK);
    }

    @PutMapping("/updateRole/{id}")
    public ResponseEntity<ApiResponse> updateRole(@PathVariable(value = "id") Long id,@RequestBody RoleRequestDTO roleRequestDTO, HttpServletRequest request) {
        GetTokenClaimsDTO claimsDTO = claimsUtils.getClaims(request);
        RoleResponseDTO response  = this.roleService.updateRoleById(id, roleRequestDTO,claimsDTO);
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, " Role Updated Successfully", response), HttpStatus.OK);
    }

    @DeleteMapping("/deleteRole/{id}")
    public ResponseEntity<ApiResponse> deleteRoleById(@PathVariable(value = "id") Long id, HttpServletRequest request) {
        GetTokenClaimsDTO claimsDTO = claimsUtils.getClaims(request);
         this.roleService.deleteRoleById(id,claimsDTO);
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "Role Deleted Successfully"), HttpStatus.OK);
    }

    @PutMapping("/updateStatus/{activeStatus}/{id}")
    public ResponseEntity<ApiResponse> updateRoleStatusById(@PathVariable(value = "id") Long id,@PathVariable  Boolean activeStatus, HttpServletRequest request) {
        GetTokenClaimsDTO claimsDTO = claimsUtils.getClaims(request);
        this.roleService.updateRoleStatusById(id, activeStatus,claimsDTO);
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "Role Status Updated Successfully"), HttpStatus.OK);
    }


}
