package com.ecommerce.dto.request;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@ToString
@NoArgsConstructor
public class GetTokenClaimsDTO {

    private Long userId;
    private String userName;
    private String roleName;
    private List<String> permissions;
}
