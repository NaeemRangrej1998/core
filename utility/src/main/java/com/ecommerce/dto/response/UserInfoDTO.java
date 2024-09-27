package com.ecommerce.dto.response;

import com.ecommerce.entity.RoleEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.management.relation.Role;
import java.util.List;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class UserInfoDTO {
    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private List<String> rolesNameList;
}
