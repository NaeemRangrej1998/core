package com.ecommerce.service.Impl;

import com.ecommerce.dto.request.GetTokenClaimsDTO;
import com.ecommerce.dto.request.PermissionRequestDTO;
import com.ecommerce.dto.response.PermissionResponseDTO;
import com.ecommerce.dto.response.RoleResponseDTO;
import com.ecommerce.entity.PermissionEntity;
import com.ecommerce.entity.RoleEntity;
import com.ecommerce.entity.UserEntity;
import com.ecommerce.enums.ExceptionEnum;
import com.ecommerce.exception.CustomException;
import com.ecommerce.repository.PermissionRepository;
import com.ecommerce.service.PermissionService;
import com.ecommerce.utils.CommonUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PermissionServiceImpl implements PermissionService {

    private final PermissionRepository permissionRepository;

    public PermissionServiceImpl(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    @Override
    public PermissionResponseDTO addPermission(PermissionRequestDTO permissionRequestDTO, GetTokenClaimsDTO claimsDTO) {
       permissionRepository.findByName(permissionRequestDTO.getPermissionName()).ifPresent(role -> {
           throw new CustomException("Permission already exists", HttpStatus.BAD_REQUEST);
       });
        PermissionEntity permissionEntity = getPermissionEntity(permissionRequestDTO, claimsDTO);
        permissionRepository.save(permissionEntity);
        return new PermissionResponseDTO(permissionEntity.getId(),permissionEntity.getName());
    }

    public PermissionEntity getPermissionEntity(PermissionRequestDTO permissionRequestDTO, GetTokenClaimsDTO claimsDTO) {
        PermissionEntity permissionEntity = new PermissionEntity();
        permissionEntity.setName(permissionRequestDTO.getPermissionName());
        permissionEntity.setCreatedDate(CommonUtils.getDateTime());
        permissionEntity.setUpdatedDate(CommonUtils.getDateTime());
        permissionEntity.setCreatedBy(new UserEntity(claimsDTO.getUserId()));
        permissionEntity.setUpdatedBy(new UserEntity(claimsDTO.getUserId()));
        permissionEntity.setStatus(true);
        permissionEntity.setDeactivate(false);
        return permissionEntity;
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
        PermissionEntity permissionEntity = getPermissionByIds(id);
        if (permissionEntity == null) {
            throw new CustomException("Permission does not exists", HttpStatus.NOT_FOUND);
        }
        return new PermissionResponseDTO(permissionEntity.getId(), permissionEntity.getName());
    }

    @Override
    public PermissionResponseDTO updatePermissionById(Long id, PermissionRequestDTO permissionRequestDTO, GetTokenClaimsDTO claimsDTO) {
        PermissionEntity permissionEntity = getPermissionByIds(id);
        if (permissionEntity == null) {
            throw new CustomException("Permission does not exists", HttpStatus.NOT_FOUND);
        }
        permissionEntity.setName(permissionRequestDTO.getPermissionName());
        permissionEntity.setUpdatedDate(CommonUtils.getDateTime());
        permissionEntity.setUpdatedBy(new UserEntity(claimsDTO.getUserId()));
        permissionEntity.setStatus(true);
        permissionEntity.setDeactivate(false);
        permissionRepository.save(permissionEntity);
        return new PermissionResponseDTO(permissionEntity.getId(),permissionEntity.getName());
    }

    @Override
    public void deletePermissionById(Long id, GetTokenClaimsDTO claimsDTO) {
        PermissionEntity permissionEntity = getPermissionByIds(id);
        if (permissionEntity == null) {
            throw new CustomException("Permission does not exists", HttpStatus.NOT_FOUND);
        }
        if (Boolean.FALSE.equals(permissionEntity.getStatus()) && Boolean.TRUE.equals(permissionEntity.getDeactivate())) {
            throw new CustomException(ExceptionEnum.ROLE_DELETED_WITH_ID.getValue(), HttpStatus.BAD_REQUEST);
        }
        permissionEntity.setUpdatedDate(CommonUtils.getDateTime());
        permissionEntity.setUpdatedBy(new UserEntity(claimsDTO.getUserId()));
        permissionEntity.setStatus(false);
        permissionEntity.setDeactivate(true);

        permissionRepository.save(permissionEntity);
    }

    @Override
    public void updatePermissionStatusById(Long id, Boolean activeStatus, GetTokenClaimsDTO claimsDTO) {
        PermissionEntity permissionEntity = getPermissionByIds(id);
        if (permissionEntity == null) {
            throw new CustomException("Permission does not exists", HttpStatus.NOT_FOUND);
        }
        // deleted condition for role
        if (Boolean.FALSE.equals(permissionEntity.getStatus()) && Boolean.TRUE.equals(permissionEntity.getDeactivate())) {
            throw new CustomException(ExceptionEnum.PERMISSION_DELETED_WITH_ID.getValue(), HttpStatus.BAD_REQUEST);
        }

        if (activeStatus.equals(permissionEntity.getStatus())) {
            throw new CustomException(ExceptionEnum.GIVEN_STATUS_AND_DATABASE_STATUS_IS_SAME.getValue(),
                    HttpStatus.BAD_REQUEST);
        }
        permissionEntity.setStatus(activeStatus);
        permissionEntity.setUpdatedDate(CommonUtils.getDateTime());
        permissionEntity.setUpdatedBy(new UserEntity(claimsDTO.getUserId()));
        permissionRepository.save(permissionEntity);
    }

    public PermissionEntity getPermissionByIds(Long id){
        Optional<PermissionEntity> permissionEntity = permissionRepository.findById(id);
        if (permissionEntity.isEmpty()) {
            throw new CustomException("Permission does not exists", HttpStatus.NOT_FOUND);
        }
        return permissionEntity.get();
    }
}
