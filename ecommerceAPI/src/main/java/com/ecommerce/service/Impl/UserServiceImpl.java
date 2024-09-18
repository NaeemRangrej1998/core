package com.ecommerce.service.Impl;

import com.ecommerce.dto.request.GetTokenClaimsDTO;
import com.ecommerce.dto.request.LoginRequestDto;
import com.ecommerce.dto.request.RegistrationDTO;
import com.ecommerce.dto.response.AddUserResponseDTO;
import com.ecommerce.dto.response.JwtResponseDto;
import com.ecommerce.dto.response.RefreshTokenResponseDTO;
import com.ecommerce.dto.response.UserInfoDTO;

import com.ecommerce.entity.RoleEntity;
import com.ecommerce.entity.UserEntity;
import com.ecommerce.enums.ExceptionEnum;
import com.ecommerce.exception.CustomException;
import com.ecommerce.repository.RoleRepository;
import com.ecommerce.repository.UserRepository;
import com.ecommerce.service.UserService;
import com.ecommerce.service.jwt.JwtTokenProvider;
import com.ecommerce.utils.CommonUtils;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private  final RoleRepository roleRepository;
    private final JwtTokenProvider jwtTokenProvider;


    private final PasswordEncoder passwordEncoder;

    @Override
    public JwtResponseDto  singIn(LoginRequestDto loginRequestDto) {
        UserEntity user = userRepository.getUserByEmail(loginRequestDto.getEmail()).orElseThrow(() -> new CustomException(ExceptionEnum.USER_NOT_FOUND.getValue(),HttpStatus.NOT_FOUND));
//        String role =user.getRole().getName();
        if (!passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword())) {
            throw new CustomException(ExceptionEnum.PASSWORD_NOT_CORRECT.getValue(), HttpStatus.NOT_FOUND);
        }
        return getTokenResponse(user);
    }

    @Override
    public RefreshTokenResponseDTO generateRefreshTokenFromOldToken(HttpServletRequest request) {
        return generateRefreshToken(request);
    }

    private RefreshTokenResponseDTO generateRefreshToken(HttpServletRequest request) {
        String refToken=jwtTokenProvider.resolveToken(request);
        if ((refToken != null && !refToken.isEmpty())) {
            try {
                jwtTokenProvider.isTokenValid(refToken);
            } catch (JwtException | IllegalArgumentException e) {
                System.out.println("jwt exception catch block inside ::::::: ");
            }
        }
        return jwtTokenProvider.creatTokenFromRefreshToken(refToken);
    }

    private JwtResponseDto getTokenResponse(UserEntity user) {
            return jwtTokenProvider.createAccessToken(user.getEmail(),user.getRole().getName(),user.getId());
    }

    @Override
    public List<UserInfoDTO> getAllUsers() {
        List<UserEntity> userEntity=userRepository.findAll();
        return userEntity.stream().map(this::mapToUserInfoDTO).collect(Collectors.toList());
    }

    @Override
    public UserInfoDTO getUserById(Long userId) {
        UserEntity userEntity= getUserEntity(userId);
        return mapToUserInfoDTO(userEntity);
    }

    @Override
    public AddUserResponseDTO updateUserStatusById(Long userId, boolean activeStatus,GetTokenClaimsDTO claimsDTO) {
        UserEntity userEntity= getUserEntity(userId);
        userEntity.setUpdatedDate(CommonUtils.getDateTime());
        userEntity.setUpdatedBy(new UserEntity(claimsDTO.getUserId()));
        userEntity.setStatus(activeStatus);
        userEntity.setDeactivate(false);
        UserEntity savedUser = userRepository.save(userEntity);

        AddUserResponseDTO  responseDTO = new AddUserResponseDTO();
        responseDTO.setId(savedUser.getId());
        responseDTO.setFirstName(savedUser.getFirstName());
        responseDTO.setLastName(savedUser.getLastName());
        responseDTO.setEmail(savedUser.getEmail());
        responseDTO.setRoleName(savedUser.getRole().getName());
        return responseDTO;
    }

    private UserEntity getUserEntity(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new CustomException(ExceptionEnum.USER_NOT_FOUND.getValue(), HttpStatus.NOT_FOUND));
    }


    @Override
    public AddUserResponseDTO registerUser(RegistrationDTO userRegisterRequest , GetTokenClaimsDTO claimsDTO) {
//        System.out.println("userId = " + currentUserId);
        Optional<UserEntity> userByUsername = userRepository.getUserByEmail(userRegisterRequest.getEmail());

        RoleEntity roleEntity = roleRepository.findByName(userRegisterRequest.getRoleName()).orElseThrow(() -> new CustomException("Role is not available for this id",HttpStatus.NOT_FOUND));

        if (userByUsername.isPresent()) {
            throw new CustomException(ExceptionEnum.USER_EXISTS.getValue(), HttpStatus.BAD_REQUEST);
        }
        UserEntity user = new UserEntity();
        user.setEmail(userRegisterRequest.getEmail());
        user.setFirstName(userRegisterRequest.getFirstName());
        user.setLastName(userRegisterRequest.getLastName());
        user.setPassword(passwordEncoder.encode(userRegisterRequest.getPassword()));
        user.setRole(roleEntity);
        user.setCreatedDate(CommonUtils.getDateTime());
        user.setUpdatedDate(CommonUtils.getDateTime());
        user.setCreatedBy(new UserEntity(claimsDTO.getUserId()));
        user.setUpdatedBy(new UserEntity(claimsDTO.getUserId()));
        user.setStatus(true);
        user.setDeactivate(false);
        UserEntity savedUser = userRepository.save(user);

        AddUserResponseDTO  responseDTO = new AddUserResponseDTO();
        responseDTO.setId(savedUser.getId());
        responseDTO.setFirstName(savedUser.getFirstName());
        responseDTO.setLastName(savedUser.getLastName());
        responseDTO.setEmail(savedUser.getEmail());
        responseDTO.setRoleName(savedUser.getRole().getName());
        return responseDTO;
    }

    public UserInfoDTO mapToUserInfoDTO(UserEntity userEntity){
        UserInfoDTO infoDTO=new UserInfoDTO();
        infoDTO.setId(userEntity.getId());
        infoDTO.setFirstName(userEntity.getFirstName());
        infoDTO.setLastName(userEntity.getLastName());
        infoDTO.setEmail(userEntity.getEmail());
        return infoDTO;
    }
}
