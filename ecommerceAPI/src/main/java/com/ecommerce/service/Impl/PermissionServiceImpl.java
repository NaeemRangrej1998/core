package com.ecommerce.service.Impl;

import com.ecommerce.dto.request.GetTokenClaimsDTO;
import com.ecommerce.dto.request.PermissionRequestDTO;
import com.ecommerce.dto.response.PermissionResponseDTO;
import com.ecommerce.dto.response.RoleResponseDTO;
import com.ecommerce.entity.PermissionEntity;
import com.ecommerce.entity.RoleEntity;
import com.ecommerce.repository.PermissionRepository;
import com.ecommerce.service.PermissionService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PermissionServiceImpl implements PermissionService {

    private final PermissionRepository permissionRepository;

    public PermissionServiceImpl(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    @Override
    public PermissionResponseDTO addPermission(PermissionRequestDTO permissionRequestDTO, GetTokenClaimsDTO claimsDTO) {
        return null;
    }

    @Override
    public List<PermissionResponseDTO> getAllPermission() {
        return permissionRepository.findByStatusAndDeactivate(true, false)
                .stream()
                .map(permission -> new PermissionResponseDTO(permission.getId(), permission.getName()))
                .collect(Collectors.toList());
    }


    @Override
    public PermissionResponseDTO getPermissionById(Long id) {
        return null;
    }

    @Override
    public PermissionResponseDTO updatePermissionById(Long id, PermissionRequestDTO permissionRequestDTO, GetTokenClaimsDTO claimsDTO) {
        return null;
    }

    @Override
    public void deletePermissionById(Long id, GetTokenClaimsDTO claimsDTO) {

    }

    @Override
    public void updatePermissionStatusById(Long id, Boolean activeStatus, GetTokenClaimsDTO claimsDTO) {

    }
}
