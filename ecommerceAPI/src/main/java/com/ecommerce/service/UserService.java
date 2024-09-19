package com.ecommerce.service;

import com.ecommerce.dto.request.GetTokenClaimsDTO;
import com.ecommerce.dto.request.LoginRequestDto;
import com.ecommerce.dto.request.RegistrationDTO;
import com.ecommerce.dto.response.AddUserResponseDTO;
import com.ecommerce.dto.response.JwtResponseDto;
import com.ecommerce.dto.response.RefreshTokenResponseDTO;
import com.ecommerce.dto.response.UserInfoDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {

    AddUserResponseDTO registerUser(RegistrationDTO userRegisterRequest, GetTokenClaimsDTO claimsDTO);

    List<UserInfoDTO> getAllUsers();

    UserInfoDTO getUserById(Long userId);
    AddUserResponseDTO updateUserStatusById(Long userId ,boolean activeStatus,GetTokenClaimsDTO claimsDTO);

    AddUserResponseDTO updateUser(Long id, RegistrationDTO userRegisterRequest, GetTokenClaimsDTO claimsDTO);
}
