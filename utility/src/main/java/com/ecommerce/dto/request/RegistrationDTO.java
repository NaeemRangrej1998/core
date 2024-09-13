package com.ecommerce.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationDTO {
    private Long id;

    @NotNull(message = "MISSING_FIRST_NAME")
    @NotEmpty(message = "INVALID_FIRST_NAME")
    private String firstName;

    @NotNull(message = "MISSING_LAST_NAME")
    @NotEmpty(message = "INVALID_LAST_NAME")
    private String lastName;

    @NotNull(message = "MISSING_EMAIL")
    @NotEmpty(message = "INVALID_EMAIL")
    @Email(message = "Please Enter Valid Email")
    private String email;

    private String password;
}
