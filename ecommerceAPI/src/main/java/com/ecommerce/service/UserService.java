package com.ecommerce.service;

import com.ecommerce.dto.request.GetTokenClaimsDTO;
import com.ecommerce.dto.request.RegistrationDTO;
import com.ecommerce.dto.response.AddUserResponseDTO;
import com.ecommerce.dto.response.UserInfoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {

    AddUserResponseDTO registerUser(RegistrationDTO userRegisterRequest, GetTokenClaimsDTO claimsDTO);

    Page<UserInfoDTO> getAllUsers(Pageable pageable);

    UserInfoDTO getUserById(Long userId);
    AddUserResponseDTO updateUserStatusById(Long userId ,boolean activeStatus,GetTokenClaimsDTO claimsDTO);

    AddUserResponseDTO updateUser(Long id, RegistrationDTO userRegisterRequest, GetTokenClaimsDTO claimsDTO);

    void deleteUserById(Long id, GetTokenClaimsDTO claimsDTO);
}
