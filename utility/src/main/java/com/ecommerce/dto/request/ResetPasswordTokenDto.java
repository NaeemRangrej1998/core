package com.ecommerce.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResetPasswordTokenDto {

    @Email(message = "Please Enter Valid Email")
    private String email;

    @NotNull(message = "MISSING_NEW_PASSWORD")
    @NotEmpty(message = "INVALID_NEW_PASSWORD")
    private String newPassword;

}
