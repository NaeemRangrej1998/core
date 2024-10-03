package com.ecommerce.service;

import com.ecommerce.dto.request.GetTokenClaimsDTO;
import com.ecommerce.dto.request.RoleRequestDTO;
import com.ecommerce.dto.response.RoleResponseDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RoleService {

    RoleResponseDTO addRole(RoleRequestDTO requestDTO, GetTokenClaimsDTO claimsDTO);

    List<RoleResponseDTO> getAllRoles();

    RoleResponseDTO getRoleById(Long id);

    RoleResponseDTO updateRoleById(Long id, RoleRequestDTO roleRequestDTO, GetTokenClaimsDTO claimsDTO);

    RoleResponseDTO deleteRoleById(Long id, RoleRequestDTO roleRequestDTO, GetTokenClaimsDTO claimsDTO);
}
