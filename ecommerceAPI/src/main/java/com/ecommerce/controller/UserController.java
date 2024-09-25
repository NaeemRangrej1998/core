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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final GetClaimsUtils claimsUtils;
    private final JwtTokenProvider jwtTokenProvider;

    public UserController(UserService userService, GetClaimsUtils claimsUtils, JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.claimsUtils = claimsUtils;
        this.jwtTokenProvider = jwtTokenProvider;
    }


    @PostMapping("/addUser")
    public ResponseEntity<ApiResponse> addUser(@RequestBody @Valid RegistrationDTO userRegisterRequest, HttpServletRequest request) {
//        String token = request.getHeader("Authorization");
//        String token = jwtTokenProvider.resolveToken(request);
//        System.out.println("id = ");
//        System.out.println("id = " + jwtTokenProvider.getUserId(token));
//        Long currentUserId= jwtTokenProvider.getUserId(token);
        GetTokenClaimsDTO claimsDTO = claimsUtils.getClaims(request);
        System.out.println("getTokenClaimsDTO = " + claimsDTO);
        AddUserResponseDTO addUserResponseDTO = userService.registerUser(userRegisterRequest, claimsDTO);
        return ResponseEntity.ok(new ApiResponse(HttpStatus.OK, "User Registered Successfully", addUserResponseDTO));
    }


    @GetMapping("/getUser")
    public ResponseEntity<ApiResponse> getAllUsers() {
        List<UserInfoDTO> infoDTO = userService.getAllUsers();
        return ResponseEntity.ok(new ApiResponse(HttpStatus.OK, "User Found Successfully", infoDTO));
    }

    @GetMapping("/getUser/{userId}")
    public ResponseEntity<ApiResponse> getUserById(@PathVariable Long userId) {
        UserInfoDTO infoDTO = userService.getUserById(userId);
        return ResponseEntity.ok(new ApiResponse(HttpStatus.OK, "User Found Successfully", infoDTO));
    }

    @PutMapping("changeStatus/{userId}/{activeStatus}")
    public ResponseEntity<ApiResponse> updateUserStatusById(@PathVariable Long userId, @PathVariable boolean activeStatus, HttpServletRequest request) {
        GetTokenClaimsDTO claimsDTO = claimsUtils.getClaims(request);
        AddUserResponseDTO infoDTO = userService.updateUserStatusById(userId, activeStatus, claimsDTO);
        return ResponseEntity.ok(new ApiResponse(HttpStatus.OK, "Status Changed", infoDTO));
    }
    @PutMapping("/updateUser/{id}")
    public ResponseEntity<ApiResponse> updateUser(@PathVariable("id") Long id, @RequestBody @Valid RegistrationDTO userRegisterRequest, HttpServletRequest request) {
        GetTokenClaimsDTO claimsDTO = claimsUtils.getClaims(request);
        AddUserResponseDTO infoDTO= userService.updateUser(id, userRegisterRequest,claimsDTO);
        return ResponseEntity.ok(new ApiResponse(HttpStatus.OK, "User Updated Successfully", infoDTO));
    }
    @GetMapping("/Name")
    public String getName() {
        return "Naim";
    }
}
