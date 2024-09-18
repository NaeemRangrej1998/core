package com.ecommerce.dto.request;

import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@ToString
@NoArgsConstructor
public class GetTokenClaimsDTO {

    private Long userId;
    private String userName;
    private String roleName;

}
