package com.ecommerce.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@RequiredArgsConstructor
public class RoleResponseDTO {
    private Long id;
    private String roleName;
    private List<String> permissions;
}
