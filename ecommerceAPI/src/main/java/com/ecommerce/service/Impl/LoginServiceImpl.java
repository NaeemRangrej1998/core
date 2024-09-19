package com.ecommerce.service.Impl;

import com.ecommerce.dto.request.LoginRequestDto;
import com.ecommerce.dto.response.JwtResponseDto;
import com.ecommerce.dto.response.RefreshTokenResponseDTO;
import com.ecommerce.entity.UserEntity;
import com.ecommerce.enums.ExceptionEnum;
import com.ecommerce.exception.CustomException;
import com.ecommerce.repository.RoleRepository;
import com.ecommerce.repository.UserRepository;
import com.ecommerce.service.LoginService;
import com.ecommerce.service.jwt.JwtTokenProvider;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

    private final UserRepository userRepository;

    private  final RoleRepository roleRepository;
    private final JwtTokenProvider jwtTokenProvider;


    private final PasswordEncoder passwordEncoder;

    @Override
    public JwtResponseDto singIn(LoginRequestDto loginRequestDto) {
        UserEntity user = userRepository.getUserByEmail(loginRequestDto.getEmail()).orElseThrow(() -> new CustomException(ExceptionEnum.USER_NOT_FOUND.getValue(), HttpStatus.NOT_FOUND));
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
            }
        }
        return jwtTokenProvider.creatTokenFromRefreshToken(refToken);
    }

    private JwtResponseDto getTokenResponse(UserEntity user) {
        return jwtTokenProvider.createAccessToken(user.getEmail(),user.getRole().getName(),user.getId());
    }

}
