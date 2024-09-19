package com.ecommerce.controller;

import com.ecommerce.dto.request.GetTokenClaimsDTO;
import com.ecommerce.dto.request.LoginRequestDto;
import com.ecommerce.dto.request.RegistrationDTO;
import com.ecommerce.dto.response.*;
import com.ecommerce.exception.CustomException;
import com.ecommerce.service.LoginService;
import com.ecommerce.service.UserService;
import com.ecommerce.service.jwt.JwtTokenProvider;
import com.ecommerce.utils.GetClaimsUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class LoginController {
    private final LoginService loginService;
    private final GetClaimsUtils claimsUtils;
    private final JwtTokenProvider jwtTokenProvider;

    public LoginController(LoginService loginService, GetClaimsUtils claimsUtils, JwtTokenProvider jwtTokenProvider) {
        this.loginService = loginService;
        this.claimsUtils = claimsUtils;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/singin")
    public ResponseEntity<ApiResponse> singInUser(@RequestBody @Valid LoginRequestDto loginRequestDto) {
        try {
            JwtResponseDto jwtResponseDto = loginService.singIn(loginRequestDto);
            return ResponseEntity.ok(new ApiResponse(HttpStatus.OK, "Sign in Success", jwtResponseDto));
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException("Something Went Wrong", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/refreshToken")
    public ResponseEntity<ApiResponse> refreshTokenGenerate(HttpServletRequest request) {
        try {
            RefreshTokenResponseDTO refreshTokenResponseDTO = loginService.generateRefreshTokenFromOldToken(request);
            return ResponseEntity.ok(new ApiResponse(HttpStatus.OK, "Get RefToken Success", refreshTokenResponseDTO));
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException("Something Went Wrong", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
