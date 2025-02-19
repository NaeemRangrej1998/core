package com.ecommerce.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@RequiredArgsConstructor
public class PermissionRequestDTO {

    @NotBlank(message = "PermissionName cannot be blank")
    private String permissionName;
}
