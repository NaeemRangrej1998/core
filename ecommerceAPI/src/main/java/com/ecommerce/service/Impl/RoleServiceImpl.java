package com.ecommerce.service.Impl;

import com.ecommerce.dto.request.GetTokenClaimsDTO;
import com.ecommerce.dto.request.RoleRequestDTO;
import com.ecommerce.dto.response.RoleResponseDTO;
import com.ecommerce.entity.PermissionEntity;
import com.ecommerce.entity.RoleEntity;
import com.ecommerce.entity.RolePermissionEntity;
import com.ecommerce.entity.UserEntity;
import com.ecommerce.enums.ExceptionEnum;
import com.ecommerce.exception.CustomException;
import com.ecommerce.repository.PermissionRepository;
import com.ecommerce.repository.RolePermissionRepository;
import com.ecommerce.repository.RoleRepository;
import com.ecommerce.service.RoleService;
import com.ecommerce.utils.CommonUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final RolePermissionRepository rolePermissionRepository;

    public RoleServiceImpl(RoleRepository roleRepository, PermissionRepository permissionRepository, RolePermissionRepository rolePermissionRepository) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
        this.rolePermissionRepository = rolePermissionRepository;
    }

    @Override
    public RoleResponseDTO addRole(RoleRequestDTO requestDTO, GetTokenClaimsDTO claimsDTO) {
        roleRepository.findByName(requestDTO.getRoleName())
                .ifPresent(role -> {
                    throw new CustomException("Role already exists", HttpStatus.BAD_REQUEST);
                });
        //        // Validate if all permissions exist before creating role
        //        List<PermissionEntity> permissions = new ArrayList<>();
        //        for (String permissionName : requestDTO.getPermissions()) {
        //            PermissionEntity permission = permissionRepository.findByName(permissionName)
        //                    .orElseThrow(() -> new CustomException("Permission not found: " + permissionName, HttpStatus.BAD_REQUEST));
        //            permissions.add(permission);
        //        }


        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setName(requestDTO.getRoleName());
        roleEntity.setCreatedDate(CommonUtils.getDateTime());
        roleEntity.setUpdatedDate(CommonUtils.getDateTime());
        roleEntity.setCreatedBy(new UserEntity(claimsDTO.getUserId()));
        roleEntity.setUpdatedBy(new UserEntity(claimsDTO.getUserId()));
        roleEntity.setStatus(true);
        roleEntity.setDeactivate(false);
        roleRepository.save(roleEntity);

        List<PermissionEntity> permissions = permissionRepository.findByIdIn(requestDTO.getPermissions());
        // Assign Permissions to Role
        for (PermissionEntity permission : permissions) {
            RolePermissionEntity rolePermission = new RolePermissionEntity();
            rolePermission.setRole(roleEntity);
            rolePermission.setPermission(permission);
            rolePermission.setCreatedDate(CommonUtils.getDateTime());
            rolePermission.setUpdatedDate(CommonUtils.getDateTime());
            rolePermission.setCreatedBy(new UserEntity(claimsDTO.getUserId()));
            rolePermission.setUpdatedBy(new UserEntity(claimsDTO.getUserId()));
            rolePermission.setStatus(true);
            rolePermission.setDeactivate(false);
            rolePermissionRepository.save(rolePermission);
        }
        return addMapToRoleResponseDTO(roleEntity, permissions);
    }

    //    @Override
    //    public List<RoleResponseDTO> getAllRoles() {
    //        List<RoleEntity> roleEntities= roleRepository.findByStatusAndDeactivate(true,false);
    //        return roleEntities.stream().map(this::mapToRoleResponseDTO).collect(Collectors.toList());
    //    }


    @Override
    public List<RoleResponseDTO> getAllRoles() {
        // Fetch only active and non-deactivated roles
        List<RoleEntity> roles = roleRepository.findByStatusAndDeactivate(true, false);

        return roles.stream().map(role -> {
            List<String> permissions = rolePermissionRepository.findByRoleAndStatusTrueAndDeactivateFalse(role)
                    .stream()
                    .map(rolePermission -> rolePermission.getPermission().getName())
                    .collect(Collectors.toList());

            return new RoleResponseDTO(role.getId(), role.getName(), permissions);
        }).collect(Collectors.toList());
    }


    @Override
    public RoleResponseDTO getRoleById(Long id) {
        RoleEntity roleEntity = roleRepository.findById(id)
                .orElseThrow(() -> new CustomException("Role not found", HttpStatus.NOT_FOUND));
        List<String> permissions = rolePermissionRepository.findByRoleAndStatusTrueAndDeactivateFalse(roleEntity)
                .stream()
                .map(rolePermission -> rolePermission.getPermission().getName())
                .collect(Collectors.toList());

        return new RoleResponseDTO(roleEntity.getId(), roleEntity.getName(), permissions);
        //        return mapToRoleResponseDTO(roleEntity);
    }

//    @Override
//    public RoleResponseDTO updateRoleById(Long id, RoleRequestDTO requestDTO, GetTokenClaimsDTO claimsDTO) {
//        RoleEntity roleEntity = roleRepository.findById(id)
//                .orElseThrow(() -> new CustomException("Role not found", HttpStatus.NOT_FOUND));
//
//
//        //        List<String> permissions = rolePermissionRepository.findByRole(roleEntity)
//        //                .stream()
//        //                .map(rolePermission -> rolePermission.getPermission().getName())
//        //                .collect(Collectors.toList());
//        roleEntity.setName(requestDTO.getRoleName());
//        roleEntity.setUpdatedDate(CommonUtils.getDateTime());
//        roleEntity.setUpdatedBy(new UserEntity(claimsDTO.getUserId()));
//        roleEntity.setStatus(true);
//        roleEntity.setDeactivate(false);
//        roleRepository.save(roleEntity);
//
//
//        return mapToRoleResponseDTO(roleEntity);
//    }

    @Override
    public RoleResponseDTO updateRoleById(Long id, RoleRequestDTO requestDTO, GetTokenClaimsDTO claimsDTO) {
        RoleEntity roleEntity = roleRepository.findById(id)
                .orElseThrow(() -> new CustomException("Role not found", HttpStatus.NOT_FOUND));

        // Step 1: Fetch all existing role permissions
        List<RolePermissionEntity> rolePermissions = rolePermissionRepository.findByRole(roleEntity);

        // Step 2: Deactivate all existing permissions for the role
        for (RolePermissionEntity rolePermission : rolePermissions) {
            rolePermission.setStatus(false);
            rolePermission.setDeactivate(true);
        }

        // Step 3: Get requested permissions from requestDTO
        List<Long> requestedPermissions = requestDTO.getPermissions();  // Assuming requestDTO has a list of permission names

        // Step 4: Fetch permission entities based on requested names
        List<PermissionEntity> permissions = permissionRepository.findByIdIn(requestedPermissions);

        // Step 5: Update RolePermissionEntities - Activate requested permissions
        for (PermissionEntity permission : permissions) {
            RolePermissionEntity rolePermission = rolePermissions.stream()
                    .filter(rp -> rp.getPermission().equals(permission))
                    .findFirst()
                    .orElseGet(() -> {
                        RolePermissionEntity newRolePermission = new RolePermissionEntity(roleEntity, permission);
                        newRolePermission.setCreatedBy(new UserEntity(claimsDTO.getUserId())); // ✅ Set createdBy
                        newRolePermission.setCreatedDate(CommonUtils.getDateTime()); // ✅ Set createdDate
                        return newRolePermission;
                    });

            rolePermission.setStatus(true);
            rolePermission.setDeactivate(false);
            rolePermission.setUpdatedBy(new UserEntity(claimsDTO.getUserId())); // ✅ Set updatedBy
            rolePermission.setUpdatedDate(CommonUtils.getDateTime()); // ✅ Set updatedDate

            rolePermissionRepository.save(rolePermission);
        }

        // Step 6: Update RoleEntity details
        roleEntity.setName(requestDTO.getRoleName());
        roleEntity.setUpdatedDate(CommonUtils.getDateTime());
        roleEntity.setUpdatedBy(new UserEntity(claimsDTO.getUserId()));
        roleEntity.setStatus(true);
        roleEntity.setDeactivate(false);

        // Step 7: Save role and updated permissions
        roleRepository.save(roleEntity);

        return addMapToRoleResponseDTO(roleEntity,permissions);
    }

    @Override
    public void updateRoleStatusById(Long id, Boolean activeStatus, GetTokenClaimsDTO claimsDTO) {
        RoleEntity roleEntity = roleRepository.findById(id)
                .orElseThrow(() -> new CustomException("Role not found", HttpStatus.NOT_FOUND));

        // deleted condition for role
        if (Boolean.FALSE.equals(roleEntity.getStatus()) && Boolean.TRUE.equals(roleEntity.getDeactivate())) {
            throw new CustomException(ExceptionEnum.ROLE_DELETED_WITH_ID.getValue(), HttpStatus.BAD_REQUEST);
        }

        if (activeStatus.equals(roleEntity.getStatus())) {
            throw new CustomException(ExceptionEnum.GIVEN_STATUS_AND_DATABASE_STATUS_IS_SAME.getValue(),
                    HttpStatus.BAD_REQUEST);
        }
        roleEntity.setStatus(activeStatus);
        roleEntity.setUpdatedDate(CommonUtils.getDateTime());
        roleEntity.setUpdatedBy(new UserEntity(claimsDTO.getUserId()));
        roleRepository.save(roleEntity);
    }


    @Override
    public void deleteRoleById(Long id, GetTokenClaimsDTO claimsDTO) {
        RoleEntity roleEntity = roleRepository.findById(id)
                .orElseThrow(() -> new CustomException("Role not found", HttpStatus.NOT_FOUND));
        if (Boolean.FALSE.equals(roleEntity.getStatus()) && Boolean.TRUE.equals(roleEntity.getDeactivate())) {
            throw new CustomException(ExceptionEnum.ROLE_DELETED_WITH_ID.getValue(), HttpStatus.BAD_REQUEST);
        }
        roleEntity.setUpdatedDate(CommonUtils.getDateTime());
        roleEntity.setUpdatedBy(new UserEntity(claimsDTO.getUserId()));

        roleEntity.setStatus(false);
        roleEntity.setDeactivate(true);
        roleRepository.save(roleEntity);
    }


    private RoleResponseDTO mapToRoleResponseDTO(RoleEntity roleEntity) {
        RoleResponseDTO responseDTO = new RoleResponseDTO();
        responseDTO.setId(roleEntity.getId());
        responseDTO.setRoleName(roleEntity.getName());
        return responseDTO;
    }

    private RoleResponseDTO addMapToRoleResponseDTO(RoleEntity roleEntity, List<PermissionEntity> permissions) {
        RoleResponseDTO responseDTO = new RoleResponseDTO();
        responseDTO.setId(roleEntity.getId());
        responseDTO.setRoleName(roleEntity.getName());

        // Convert PermissionEntity list to a list of permission names
        List<String> permissionNames = permissions.stream()
                .map(PermissionEntity::getName)
                .collect(Collectors.toList());

        responseDTO.setPermissions(permissionNames);

        return responseDTO;
    }

}

