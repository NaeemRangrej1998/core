package com.ecommerce.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationDTO {
    private Long id;

    @NotNull(message = "Username must be not null")
    private String firstName;

    @NotNull(message = "Lastname must be not null")
    private String lastName;

    @NotNull(message = "Email must be not null")
    @Email(message = "Please Enter Valid Email")
    private String email;

    private String password;
}
