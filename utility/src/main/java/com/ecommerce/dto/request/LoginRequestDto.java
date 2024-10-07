package com.ecommerce.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequestDto {

    @NotNull(message = "Email must be not null")
    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Please Enter Valid Email")
    private String email;

    @NotBlank(message = "Password cannot be blank")
    private String password;
}
