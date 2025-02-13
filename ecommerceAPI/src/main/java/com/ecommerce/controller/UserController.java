package com.ecommerce.controller;

import com.ecommerce.dto.request.GetTokenClaimsDTO;
import com.ecommerce.dto.request.LoginRequestDto;
import com.ecommerce.dto.request.RegistrationDTO;
import com.ecommerce.dto.request.UpdateUserDTO;
import com.ecommerce.dto.response.*;
import com.ecommerce.exception.CustomException;
import com.ecommerce.service.UserService;
import com.ecommerce.service.jwt.JwtTokenProvider;
import com.ecommerce.utils.GetClaimsUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
//@CrossOrigin("http://localhost:3000")
public class UserController {

    private final UserService userService;
    private final GetClaimsUtils claimsUtils;
    private final JwtTokenProvider jwtTokenProvider;

    public UserController(UserService userService, GetClaimsUtils claimsUtils, JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.claimsUtils = claimsUtils;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PreAuthorize("hasAuthority('ADMIN') and hasAuthority('CREATE')")
    @PostMapping("/addUser")
    public ResponseEntity<ApiResponse> addUser(@Valid @RequestBody RegistrationDTO userRegisterRequest, HttpServletRequest request) {
        GetTokenClaimsDTO claimsDTO = claimsUtils.getClaims(request);
        AddUserResponseDTO addUserResponseDTO = userService.registerUser(userRegisterRequest, claimsDTO);
        return ResponseEntity.ok(new ApiResponse(HttpStatus.OK, "User Registered Successfully", addUserResponseDTO));
    }

    @PreAuthorize("(hasAuthority('ADMIN') or hasAuthority('USER')) and hasAuthority('READ')")
    @GetMapping("/getUser")
    public ResponseEntity<ApiResponse> getAllUsers(@RequestParam(defaultValue = "0")Integer pageNo,
                                                   @RequestParam(defaultValue = "10")Integer pageSize,
                                                       @RequestParam(value = "searchValue", required = false, defaultValue = "") String searchValue,
                                                   @RequestParam(defaultValue = "id")String sortBy,
                                                   HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Authenticated User: " + auth.getName());
        System.out.println("User Authorities: " + auth.getAuthorities());

        GetTokenClaimsDTO claimsDTO = claimsUtils.getClaims(request);
        System.out.println("getTokenClaimsDTO = " + claimsDTO);
        Pageable pageable= PageRequest.of(pageNo,pageSize, Sort.by(sortBy));
        Page<UserInfoDTO> infoDTO = userService.getAllUsers(pageable,searchValue);
        return ResponseEntity.ok(new ApiResponse(HttpStatus.OK, "User Found Successfully", infoDTO));
    }

    @PreAuthorize("(hasAuthority('ADMIN') or hasAuthority('USER')) and hasAuthority('READ')")
    @GetMapping("/getUser/{userId}")
    public ResponseEntity<ApiResponse> getUserById(@PathVariable Long userId) {
        UserInfoDTO infoDTO = userService.getUserById(userId);
        return ResponseEntity.ok(new ApiResponse(HttpStatus.OK, "User Found Successfully", infoDTO));
    }

    @PreAuthorize("hasAuthority('ADMIN') and hasAuthority('UPDATE')")
    @PutMapping("changeStatus/{userId}/{activeStatus}")
    public ResponseEntity<ApiResponse> updateUserStatusById(@PathVariable Long userId, @PathVariable boolean activeStatus, HttpServletRequest request) {
        GetTokenClaimsDTO claimsDTO = claimsUtils.getClaims(request);
        AddUserResponseDTO infoDTO = userService.updateUserStatusById(userId, activeStatus, claimsDTO);
        return ResponseEntity.ok(new ApiResponse(HttpStatus.OK, "Status Changed", infoDTO));
    }

    @PreAuthorize("hasAuthority('ADMIN') and hasAuthority('UPDATE')")
    @PutMapping("/updateUser/{id}")
    public ResponseEntity<ApiResponse> updateUser(@PathVariable("id") Long id, @Valid @RequestBody UpdateUserDTO updateUserDTO, HttpServletRequest request) {
        GetTokenClaimsDTO claimsDTO = claimsUtils.getClaims(request);
        AddUserResponseDTO infoDTO= userService.updateUser(id, updateUserDTO,claimsDTO);
        return ResponseEntity.ok(new ApiResponse(HttpStatus.OK, "User Updated Successfully", infoDTO));
    }

    @PreAuthorize("hasAuthority('ADMIN') and hasAuthority('DELETE')")
    @DeleteMapping("/deleteUser/{id}")
    public ResponseEntity<ApiResponse> deleteUserById(@PathVariable("id") Long id, HttpServletRequest request) {
        GetTokenClaimsDTO claimsDTO = claimsUtils.getClaims(request);
        userService.deleteUserById(id,claimsDTO);
        return ResponseEntity.ok(new ApiResponse(HttpStatus.OK, "User Deleted Successfully"));
    }
    @GetMapping("/Name")
    public String getName() {
        return "Naim";
    }
}
