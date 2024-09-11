package com.ecommerce.service.Impl;

import com.ecommerce.dto.LoginRequestDto;
import com.ecommerce.dto.RegistrationDTO;
import com.ecommerce.dto.response.AddUserResponseDTO;
import com.ecommerce.dto.response.JwtResponseDto;
import com.ecommerce.dto.response.UserInfoDTO;

import com.ecommerce.entity.UserEntity;
import com.ecommerce.exception.CustomException;
import com.ecommerce.exception.UserAlreadyExistsException;
import com.ecommerce.exception.UserNotFoundException;
import com.ecommerce.repository.UserRepository;
import com.ecommerce.service.UserService;
import com.ecommerce.service.jwt.JwtTokenProvider;
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


    private final JwtTokenProvider jwtTokenProvider;


    private final PasswordEncoder passwordEncoder;

    @Override
    public JwtResponseDto  singIn(LoginRequestDto loginRequestDto) {
        UserEntity user = userRepository.getUserByEmail(loginRequestDto.getEmail()).orElseThrow(() -> new UserNotFoundException("User Not Found For This Email"));
        if (!passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword())) {
            throw new CustomException("Password Not Correct", HttpStatus.BAD_REQUEST);
        }
        return getTokenResponse(user);
    }

    private JwtResponseDto getTokenResponse(UserEntity user) {
            return jwtTokenProvider.createAccessToken(user.getEmail());
    }

    @Override
    public List<UserInfoDTO> getAllUsers() {
        List<UserEntity> userEntity=userRepository.findAll();
        return userEntity.stream().map(this::mapToUserInfoDTO).collect(Collectors.toList());
    }


    @Override
    public AddUserResponseDTO registerUser(RegistrationDTO userRegisterRequest) {
        Optional<UserEntity> userByUsername = userRepository.getUserByEmail(userRegisterRequest.getEmail());

        if (userByUsername.isPresent()) {
            throw new UserAlreadyExistsException("User already  exists");
        }
        UserEntity user = new UserEntity();
        user.setEmail(userRegisterRequest.getEmail());
        user.setFirstName(userRegisterRequest.getFirstName());
        user.setLastName(userRegisterRequest.getLastName());
        user.setPassword(passwordEncoder.encode(userRegisterRequest.getPassword()));
        UserEntity savedUser = userRepository.save(user);

        AddUserResponseDTO  responseDTO = new AddUserResponseDTO();
        responseDTO.setId(savedUser.getId());
        responseDTO.setFirstName(savedUser.getFirstName());
        responseDTO.setLastName(savedUser.getLastName());
        responseDTO.setEmail(savedUser.getEmail());
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
