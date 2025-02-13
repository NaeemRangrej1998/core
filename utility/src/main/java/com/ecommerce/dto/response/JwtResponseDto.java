package com.ecommerce.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponseDto {

    private String token;

    private Long userId;

    private String userRole;

    private String userName;

    private List<String> permission;

    public JwtResponseDto(String token) {
    }



}
