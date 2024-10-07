package com.ecommerce.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@RequiredArgsConstructor
public class RoleRequestDTO {

    @NotBlank(message = "RoleName cannot be blank")
    private String rollName;
}

