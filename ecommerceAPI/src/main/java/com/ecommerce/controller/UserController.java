package com.ecommerce.controller;

import com.ecommerce.dto.request.LoginRequestDto;
import com.ecommerce.dto.request.RegistrationDTO;
import com.ecommerce.dto.response.AddUserResponseDTO;
import com.ecommerce.dto.response.ApiResponse;
import com.ecommerce.dto.response.JwtResponseDto;
import com.ecommerce.dto.response.UserInfoDTO;
import com.ecommerce.exception.CustomException;
import com.ecommerce.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
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

    @PostMapping("/addUser")
    public ResponseEntity<ApiResponse> registerUser(@RequestBody @Valid RegistrationDTO userRegisterRequest) {
        try {
            AddUserResponseDTO addUserResponseDTO = userService.registerUser(userRegisterRequest);
            return ResponseEntity.ok(new ApiResponse(HttpStatus.OK, "SignUp in Success", addUserResponseDTO));
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException("Something Went Wrong", HttpStatus.INTERNAL_SERVER_ERROR);
        }
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

    @GetMapping("/Name")
    public String getName() {
        return "Naim";
    }
}
