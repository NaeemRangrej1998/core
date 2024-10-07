package com.ecommerce.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponseDto {

    private String token;

    private Long userId;

    private String userRole;

    private String userName;


    public JwtResponseDto(String token) {
    }



}
