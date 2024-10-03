package com.ecommerce.service.Impl;

import com.ecommerce.dto.request.GetTokenClaimsDTO;
import com.ecommerce.dto.request.RoleRequestDTO;
import com.ecommerce.dto.response.RoleResponseDTO;
import com.ecommerce.entity.RoleEntity;
import com.ecommerce.entity.UserEntity;
import com.ecommerce.exception.CustomException;
import com.ecommerce.repository.RoleRepository;
import com.ecommerce.service.RoleService;
import com.ecommerce.utils.CommonUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public RoleResponseDTO addRole(RoleRequestDTO requestDTO, GetTokenClaimsDTO claimsDTO) {
        roleRepository.findByName(requestDTO.getRollName())
                .ifPresent(role -> {
                    throw new CustomException("Role already exists", HttpStatus.BAD_REQUEST);
                });

        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setName(requestDTO.getRollName());
        roleEntity.setCreatedDate(CommonUtils.getDateTime());
        roleEntity.setUpdatedDate(CommonUtils.getDateTime());
        roleEntity.setCreatedBy(new UserEntity(claimsDTO.getUserId()));
        roleEntity.setUpdatedBy(new UserEntity(claimsDTO.getUserId()));
        roleEntity.setStatus(true);
        roleEntity.setDeactivate(false);
        roleRepository.save(roleEntity);
        return mapToRoleResponseDTO(roleEntity);
    }

    @Override
    public List<RoleResponseDTO> getAllRoles() {
        return roleRepository.findAll().stream()
                .map(this::mapToRoleResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public RoleResponseDTO getRoleById(Long id) {
        RoleEntity roleEntity = roleRepository.findById(id)
                .orElseThrow(() -> new CustomException("Role not found", HttpStatus.NOT_FOUND));

        return mapToRoleResponseDTO(roleEntity);
    }

    @Override
    public RoleResponseDTO updateRoleById(Long id, RoleRequestDTO requestDTO, GetTokenClaimsDTO claimsDTO) {
        RoleEntity roleEntity = roleRepository.findById(id)
                .orElseThrow(() -> new CustomException("Role not found", HttpStatus.NOT_FOUND));

        roleEntity.setName(requestDTO.getRollName());
        roleEntity.setUpdatedDate(CommonUtils.getDateTime());
        roleEntity.setUpdatedBy(new UserEntity(claimsDTO.getUserId()));
        roleEntity.setStatus(true);
        roleEntity.setDeactivate(false);
        roleRepository.save(roleEntity);
        return mapToRoleResponseDTO(roleEntity);
    }

    @Override
    public RoleResponseDTO deleteRoleById(Long id, RoleRequestDTO roleRequestDTO, GetTokenClaimsDTO claimsDTO) {
        return null;
    }

    private RoleResponseDTO mapToRoleResponseDTO(RoleEntity roleEntity) {
        RoleResponseDTO responseDTO = new RoleResponseDTO();
        responseDTO.setId(roleEntity.getId());
        responseDTO.setRollName(roleEntity.getName());
        return responseDTO;
    }
}

