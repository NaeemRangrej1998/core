package com.ecommerce.dto.request;

import jakarta.validation.constraints.Email;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ForgotPasswordDto {
    @Email(message = "Please Enter Valid Email")
    private String email;

    private String hostName;
}
