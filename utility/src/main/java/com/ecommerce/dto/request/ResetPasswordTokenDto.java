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

    @NotNull(message = "MISSING_NEW_PASSWORD")
    @NotEmpty(message = "INVALID_NEW_PASSWORD")
    private String newPassword;

    private String resetToken;

}
