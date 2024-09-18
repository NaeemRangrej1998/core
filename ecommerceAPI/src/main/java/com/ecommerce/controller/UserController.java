package com.ecommerce.controller;

import com.ecommerce.dto.request.GetTokenClaimsDTO;
import com.ecommerce.dto.request.LoginRequestDto;
import com.ecommerce.dto.request.RegistrationDTO;
import com.ecommerce.dto.response.*;
import com.ecommerce.exception.CustomException;
import com.ecommerce.service.UserService;
import com.ecommerce.service.jwt.JwtTokenProvider;
import com.ecommerce.utils.GetClaimsUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class UserController {

    private final UserService userService;
    private final GetClaimsUtils claimsUtils;
    private final JwtTokenProvider jwtTokenProvider;
    public UserController(UserService userService, GetClaimsUtils claimsUtils, JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.claimsUtils = claimsUtils;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/singin")
    public ResponseEntity<ApiResponse> singInUser(@RequestBody @Valid LoginRequestDto loginRequestDto) {
        try {
            JwtResponseDto jwtResponseDto = userService.singIn(loginRequestDto);
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
            RefreshTokenResponseDTO refreshTokenResponseDTO = userService.generateRefreshTokenFromOldToken(request);
            return ResponseEntity.ok(new ApiResponse(HttpStatus.OK, "Get RefToken Success", refreshTokenResponseDTO));
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException("Something Went Wrong", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/addUser")
    public ResponseEntity<ApiResponse> addUser(@RequestBody @Valid RegistrationDTO userRegisterRequest, HttpServletRequest request) {
//        try {
//        String token = request.getHeader("Authorization");
//        String token = jwtTokenProvider.resolveToken(request);
//        System.out.println("id = ");
//        System.out.println("id = " + jwtTokenProvider.getUserId(token));
//        Long currentUserId= jwtTokenProvider.getUserId(token);
        GetTokenClaimsDTO claimsDTO = claimsUtils.getClaims(request);
        System.out.println("getTokenClaimsDTO = " + claimsDTO);
        AddUserResponseDTO addUserResponseDTO = userService.registerUser(userRegisterRequest, claimsDTO);
            return ResponseEntity.ok(new ApiResponse(HttpStatus.OK, "SignUp in Success", addUserResponseDTO));
//        } catch (CustomException e) {
//            throw e;
//        } catch (Exception e) {
//            throw new CustomException("Something Went Wrong", HttpStatus.INTERNAL_SERVER_ERROR);
//        }
    }


    @GetMapping("/getUser")
    public ResponseEntity<ApiResponse> getAllUsers() {
        try {
            List<UserInfoDTO> infoDTO = userService.getAllUsers();
            return ResponseEntity.ok(new ApiResponse(HttpStatus.OK, "Get All Users", infoDTO));
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException("Something Went Wrong", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getUser/{userId}")
    public ResponseEntity<ApiResponse> getUserById(@PathVariable Long userId) {
        try {
            UserInfoDTO infoDTO = userService.getUserById(userId);
            return ResponseEntity.ok(new ApiResponse(HttpStatus.OK, "Get Users By UserId", infoDTO));
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException("Something Went Wrong", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{userId}/{activeStatus}")
    public ResponseEntity<ApiResponse> updateUserStatusById(@PathVariable Long userId ,@PathVariable boolean activeStatus,HttpServletRequest request) {
        try {
            GetTokenClaimsDTO claimsDTO=claimsUtils.getClaims(request);
            AddUserResponseDTO infoDTO = userService.updateUserStatusById(userId,activeStatus,claimsDTO);
            return ResponseEntity.ok(new ApiResponse(HttpStatus.OK, "Get Users By UserId", infoDTO));
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException("Something Went Wrong", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/Name")
    public String getName() {
        return "Naim";
    }
}
