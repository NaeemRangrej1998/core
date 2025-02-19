package com.ecommerce.dto.response;

import com.ecommerce.entity.RoleEntity;
import lombok.*;

import javax.management.relation.Role;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@RequiredArgsConstructor
public class UserInfoDTO {
    private Long id;

    private String firstName;

    private String lastName;

    private String email;

//    private String password;

    private String userRole;

//    private List<String> rolesNameList;
}
