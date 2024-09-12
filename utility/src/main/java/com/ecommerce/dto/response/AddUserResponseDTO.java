package com.ecommerce.dto.response;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@RequiredArgsConstructor
public class AddUserResponseDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
}
