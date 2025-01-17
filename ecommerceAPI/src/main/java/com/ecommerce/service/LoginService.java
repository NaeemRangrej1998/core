package com.ecommerce.service;

import com.ecommerce.dto.request.ForgotPasswordDto;
import com.ecommerce.dto.request.LoginRequestDto;
import com.ecommerce.dto.request.ResetPasswordTokenDto;
import com.ecommerce.dto.response.JwtResponseDto;
import com.ecommerce.dto.response.RefreshTokenResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

@Service
public interface LoginService {
    JwtResponseDto singIn(LoginRequestDto loginRequestDto);

    RefreshTokenResponseDTO generateRefreshTokenFromOldToken(HttpServletRequest request);

    String getUserByEmail(ForgotPasswordDto forgotPasswordDto);

    String resetpassword(@Valid ResetPasswordTokenDto resetPasswordTokenDto);
}
