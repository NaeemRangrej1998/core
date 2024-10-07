package com.ecommerce.service.Impl;

import com.ecommerce.dto.request.GetTokenClaimsDTO;
import com.ecommerce.dto.request.RegistrationDTO;
import com.ecommerce.dto.response.AddUserResponseDTO;

import com.ecommerce.dto.response.UserInfoDTO;

import com.ecommerce.entity.RoleEntity;
import com.ecommerce.entity.RoleMappingEntity;
import com.ecommerce.entity.UserEntity;
import com.ecommerce.enums.ExceptionEnum;
import com.ecommerce.exception.CustomException;
import com.ecommerce.repository.RoleMappingRepository;
import com.ecommerce.repository.RoleRepository;
import com.ecommerce.repository.UserRepository;
import com.ecommerce.service.UserService;
import com.ecommerce.service.jwt.JwtTokenProvider;
import com.ecommerce.utils.CommonUtils;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
    private final RoleMappingRepository roleMappingRepository;
    private final RoleRepository roleRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Page<UserInfoDTO> getAllUsers(Pageable pageable) {
        Page<UserEntity> userEntity = userRepository.getUserEntityByStatusAndDeactivate(true, false,pageable);
        List<UserInfoDTO>List=userEntity.getContent().stream().map(this::mapToUserInfoDTO).toList();
        return new PageImpl<>(List,pageable,userEntity.getTotalElements());
    }

    @Override
    public UserInfoDTO getUserById(Long userId) {
        try {
            UserEntity userEntity = getUserEntity(userId);
            return mapToUserInfoDTO(userEntity);
        }catch (CustomException e){
            throw new CustomException(e.getMessage(),e.getHttpStatus());
        }

    }

    @Override
    public AddUserResponseDTO updateUserStatusById(Long userId, boolean activeStatus, GetTokenClaimsDTO claimsDTO) {
        try {
            UserEntity userEntity = getUserEntity(userId);
            userEntity.setUpdatedDate(CommonUtils.getDateTime());
            userEntity.setUpdatedBy(new UserEntity(claimsDTO.getUserId()));
            userEntity.setStatus(activeStatus);
            userEntity.setDeactivate(false);
            UserEntity savedUser = userRepository.save(userEntity);
            return mapToAddUserResponseDTO(savedUser);
        }
        catch (CustomException e){
            throw new CustomException(e.getMessage(),e.getHttpStatus());
        }

    }

    @Override
    public AddUserResponseDTO registerUser(RegistrationDTO userRegisterRequest, GetTokenClaimsDTO claimsDTO) {
        try {
            Optional<UserEntity> userByUsername = userRepository.getUserByEmail(userRegisterRequest.getEmail());

//        RoleEntity roleEntity = roleRepository.findByName(userRegisterRequest.getRoleName()).orElseThrow(() -> new CustomException(ExceptionEnum.ROLE_NOT_FOUND.getMessage(), HttpStatus.NOT_FOUND));

            RoleEntity roleEntity = roleRepository.findById(userRegisterRequest.getRoleId()).orElseThrow(() -> new CustomException(ExceptionEnum.ROLE_NOT_FOUND.getMessage(), HttpStatus.NOT_FOUND));

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

            RoleMappingEntity roleMappingEntity = new RoleMappingEntity();
            roleMappingEntity.setUserEntity(user);
            roleMappingEntity.setRoleEntity(roleEntity);
            roleMappingEntity.setCreatedDate(CommonUtils.getDateTime());
            roleMappingEntity.setUpdatedDate(CommonUtils.getDateTime());
            roleMappingEntity.setCreatedBy(new UserEntity(claimsDTO.getUserId()));
            roleMappingEntity.setUpdatedBy(new UserEntity(claimsDTO.getUserId()));
            roleMappingEntity.setStatus(true);
            roleMappingEntity.setDeactivate(false);
            roleMappingRepository.save(roleMappingEntity);

            return mapToAddUserResponseDTO(savedUser);
        }catch (CustomException e){
            throw new CustomException(e.getMessage(),e.getHttpStatus());
        }
    }

    @Override
    public AddUserResponseDTO updateUser(Long id, RegistrationDTO userRegisterRequest, GetTokenClaimsDTO claimsDTO) {
        try {
            UserEntity userEntity = getUserEntity(id);
            RoleEntity roleEntity = getRoleEntity(userRegisterRequest.getRoleId());
            userEntity.setEmail(userRegisterRequest.getEmail());
            userEntity.setFirstName(userRegisterRequest.getFirstName());
            userEntity.setLastName(userRegisterRequest.getLastName());
//            userEntity.setPassword(passwordEncoder.encode(userRegisterRequest.getPassword()));
            userEntity.setRole(roleEntity);
            userEntity.setUpdatedDate(CommonUtils.getDateTime());
            userEntity.setUpdatedBy(new UserEntity(claimsDTO.getUserId()));
            userEntity.setStatus(true);
            userEntity.setDeactivate(false);
            userRepository.save(userEntity);
            Optional<RoleMappingEntity> optionalUser = roleMappingRepository.findByUserEntity(userEntity);

            RoleMappingEntity roleMappingEntity;
            if (optionalUser.isPresent()) {
                roleMappingEntity = optionalUser.get();
                roleMappingEntity.setRoleEntity(roleEntity);
                roleMappingEntity.setUpdatedDate(CommonUtils.getDateTime());
                roleMappingEntity.setUpdatedBy(new UserEntity(claimsDTO.getUserId()));
                roleMappingEntity.setStatus(true);
                roleMappingEntity.setDeactivate(false);
                userEntity.setUpdatedDate(CommonUtils.getDateTime());
                roleMappingRepository.save(roleMappingEntity);
            }
            return mapToAddUserResponseDTO(userEntity);
        } catch (CustomException e) {
            throw new CustomException(e.getMessage(), e.getHttpStatus());
        }
    }

    @Override
    public void deleteUserById(Long id, GetTokenClaimsDTO claimsDTO) {
        try {
            UserEntity userEntity = getUserEntity(id);
            userEntity.setStatus(false);
            userEntity.setDeactivate(true);
            userEntity.setUpdatedBy(new UserEntity(claimsDTO.getUserId()));
            userEntity.setUpdatedDate(CommonUtils.getDateTime());
            userRepository.save(userEntity);
        } catch (CustomException e) {
            throw new CustomException(e.getMessage(), e.getHttpStatus());
        }

    }

    private UserEntity getUserEntity(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new CustomException(ExceptionEnum.USER_NOT_FOUND.getValue(), HttpStatus.NOT_FOUND));
    }

    private RoleEntity getRoleEntity(Long roleId) {
        return roleRepository.findById(roleId).orElseThrow(() -> new CustomException("Role Not Found", HttpStatus.NOT_FOUND));
    }

    public UserInfoDTO mapToUserInfoDTO(UserEntity userEntity) {
//        Optional<List<RoleMappingEntity>> roleMappingsOpt = Optional.ofNullable(roleMappingRepository.findByUserEntity(userEntity));

        // Extract role names from role mappings
        UserInfoDTO infoDTO = new UserInfoDTO();
        infoDTO.setId(userEntity.getId());
        infoDTO.setFirstName(userEntity.getFirstName());
        infoDTO.setLastName(userEntity.getLastName());
        infoDTO.setEmail(userEntity.getEmail());
        infoDTO.setPassword(userEntity.getPassword());
        infoDTO.setRoleId(userEntity.getRole().getId());

//        if (roleMappingsOpt.isPresent()){
//            List<RoleMappingEntity> roleMappings = roleMappingsOpt.get();
//            List<String> rolesNameList = roleMappings.stream()
//                    .map(mapping -> mapping.getRoleEntity().getName())  // Get the role name
//                    .toList();
//            infoDTO.setRolesNameList(rolesNameList);
//        }
        return infoDTO;
    }

    public AddUserResponseDTO mapToAddUserResponseDTO(UserEntity entity) {
        AddUserResponseDTO responseDTO = new AddUserResponseDTO();
        responseDTO.setId(entity.getId());
        responseDTO.setFirstName(entity.getFirstName());
        responseDTO.setLastName(entity.getLastName());
        responseDTO.setEmail(entity.getEmail());
        responseDTO.setRoleId(entity.getRole().getId());
        return responseDTO;
    }
}
