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
public class GetAllUserInfor {
    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String password;

    private String roleName;

//    private List<String> rolesNameList;
}
