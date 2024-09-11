package com.ecommerce.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class AddUserResponseDTO {
    private Long id;

    private String firstName;

    private String lastName;

    private String email;
}
