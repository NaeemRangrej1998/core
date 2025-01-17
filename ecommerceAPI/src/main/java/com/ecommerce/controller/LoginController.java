package com.ecommerce.controller;

import com.ecommerce.dto.request.ForgotPasswordDto;
import com.ecommerce.dto.request.LoginRequestDto;
import com.ecommerce.dto.request.ResetPasswordTokenDto;
import com.ecommerce.dto.response.*;
import com.ecommerce.enums.ExceptionEnum;
import com.ecommerce.exception.CustomException;
import com.ecommerce.service.LoginService;
import com.ecommerce.service.jwt.JwtTokenProvider;
import com.ecommerce.utils.GetClaimsUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
//@CrossOrigin("http://localhost:3000")
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
    public ResponseEntity<ApiResponse> singInUser(@Valid @RequestBody LoginRequestDto loginRequestDto) {
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

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse>getUserEmail(@Valid @RequestBody ForgotPasswordDto forgotPasswordDto, HttpServletRequest request){
        try{
            String message = loginService.getUserByEmail(forgotPasswordDto);
            System.out.println("message = " + message);
            return ResponseEntity.ok(new ApiResponse(HttpStatus.OK, message));
        }catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException(ExceptionEnum.SOMETHING_WENT_WRONG.getValue(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse>resetUserPassword(@Valid @RequestBody ResetPasswordTokenDto resetPasswordTokenDto){
        try {
            String message=loginService.resetpassword(resetPasswordTokenDto);
            return ResponseEntity.ok(new ApiResponse(HttpStatus.OK,message));
        }catch (CustomException e){
            throw e;
        }catch (Exception e) {
            throw new CustomException(ExceptionEnum.SOMETHING_WENT_WRONG.getValue(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
