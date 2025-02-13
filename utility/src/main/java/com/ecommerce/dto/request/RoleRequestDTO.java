package com.ecommerce.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@RequiredArgsConstructor
public class RoleRequestDTO {

    @NotBlank(message = "RoleName cannot be blank")
    private String roleName;

    private List<String> permissions;

}

