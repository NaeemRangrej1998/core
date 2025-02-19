package com.ecommerce.service;

import com.ecommerce.dto.request.GetTokenClaimsDTO;
import com.ecommerce.dto.request.PermissionRequestDTO;
import com.ecommerce.dto.response.PermissionResponseDTO;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PermissionService {
    PermissionResponseDTO addPermission(@Valid PermissionRequestDTO permissionRequestDTO, GetTokenClaimsDTO claimsDTO);

    List<PermissionResponseDTO> getAllPermission();

    PermissionResponseDTO getPermissionById(Long id);

    PermissionResponseDTO updatePermissionById(Long id, @Valid PermissionRequestDTO permissionRequestDTO, GetTokenClaimsDTO claimsDTO);

    void deletePermissionById(Long id, GetTokenClaimsDTO claimsDTO);

    void updatePermissionStatusById(Long id, Boolean activeStatus, GetTokenClaimsDTO claimsDTO);
}
