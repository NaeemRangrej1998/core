package com.ecommerce.controller;

import com.ecommerce.dto.request.GetTokenClaimsDTO;
import com.ecommerce.dto.request.PermissionRequestDTO;
import com.ecommerce.dto.request.RoleRequestDTO;
import com.ecommerce.dto.response.ApiResponse;
import com.ecommerce.dto.response.PermissionResponseDTO;
import com.ecommerce.dto.response.RoleResponseDTO;
import com.ecommerce.service.PermissionService;
import com.ecommerce.service.RoleService;
import com.ecommerce.utils.GetClaimsUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/permission")
@CrossOrigin("http://localhost:3000")
public class PermissionController {

    private final PermissionService permissionService;
    private final GetClaimsUtils claimsUtils;

    public PermissionController(PermissionService permissionService, GetClaimsUtils claimsUtils) {
        this.permissionService = permissionService;
        this.claimsUtils = claimsUtils;
    }

    @PreAuthorize("hasRole('ADMIN') and hasPermission(null, 'WRITE')")
    @PostMapping("/addPermission")
    public ResponseEntity<ApiResponse> addPermission(@Valid @RequestBody PermissionRequestDTO permissionRequestDTO, HttpServletRequest request) {
        GetTokenClaimsDTO claimsDTO = claimsUtils.getClaims(request);
        PermissionResponseDTO permissionResponseDTO = permissionService.addPermission(permissionRequestDTO,claimsDTO);
        return ResponseEntity.ok(new ApiResponse(HttpStatus.OK, "Permission Saved  Successfully", permissionResponseDTO));
    }

//    @PreAuthorize("hasAnyRole('ADMIN', 'USER') and hasPermission(null, 'READ')")
    @GetMapping("/getAllPermission")
    public ResponseEntity<ApiResponse> getAllPermission() {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        System.out.println("auth.getName() = " + auth.getName());
//        System.out.println("auth.getAuthorities() = " + auth.getAuthorities());

//        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortAs));
        List<PermissionResponseDTO> response = permissionService.getAllPermission();
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "Get Permission Successfully", response), HttpStatus.OK);
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'USER') and hasPermission(null, 'READ')")
    @GetMapping("/getPermissionById/{id}")
    public ResponseEntity<ApiResponse> getPermissionById(@PathVariable(value = "id") Long id, HttpServletRequest request) {
        PermissionResponseDTO response  = this.permissionService.getPermissionById(id);
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "Get Permission Successfully", response), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN') and hasPermission(null, 'UPDATE')")
    @PutMapping("/updatePermission/{id}")
    public ResponseEntity<ApiResponse> updatePermission(@PathVariable(value = "id") Long id,@Valid @RequestBody PermissionRequestDTO permissionRequestDTO, HttpServletRequest request) {
        GetTokenClaimsDTO claimsDTO = claimsUtils.getClaims(request);
        PermissionResponseDTO response  = this.permissionService.updatePermissionById(id, permissionRequestDTO,claimsDTO);
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, " Permission Updated Successfully", response), HttpStatus.OK);
    }
    @PreAuthorize("hasRole('ADMIN') and hasPermission(null, 'DELETE')")
    @DeleteMapping("/deletePermission/{id}")
    public ResponseEntity<ApiResponse> deletePermissionById(@PathVariable(value = "id") Long id, HttpServletRequest request) {
        GetTokenClaimsDTO claimsDTO = claimsUtils.getClaims(request);
        this.permissionService.deletePermissionById(id,claimsDTO);
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "Permission Deleted Successfully"), HttpStatus.OK);
    }
    @PreAuthorize("hasRole('ADMIN') and hasPermission(null, 'UPDATE')")
    @PutMapping("/updateStatus/{activeStatus}/{id}")
    public ResponseEntity<ApiResponse> updatePermissionStatusById(@PathVariable(value = "id") Long id,@PathVariable  Boolean activeStatus, HttpServletRequest request) {
        GetTokenClaimsDTO claimsDTO = claimsUtils.getClaims(request);
        this.permissionService.updatePermissionStatusById(id, activeStatus,claimsDTO);
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "Permission Status Updated Successfully"), HttpStatus.OK);
    }


}