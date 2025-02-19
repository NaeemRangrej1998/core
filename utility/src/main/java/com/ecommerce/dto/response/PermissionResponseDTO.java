package com.ecommerce.dto.response;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@RequiredArgsConstructor
public class PermissionResponseDTO {
    private Long id;
    private String permissionName;
}
