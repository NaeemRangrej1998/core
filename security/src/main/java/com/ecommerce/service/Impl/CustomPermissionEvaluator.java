package com.ecommerce.service.Impl;

import com.ecommerce.entity.UserEntity;
import com.ecommerce.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.io.Serializable;
@Component
@AllArgsConstructor
public class CustomPermissionEvaluator implements PermissionEvaluator {
    private final UserRepository userRepository;

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        if (authentication == null || permission == null) {
            return false;
        }

        String username = authentication.getName();
        UserEntity user = userRepository.getUserByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Check both role-based and permission-based access
        boolean hasRole = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_" + user.getRole().getName().toUpperCase()));

        boolean hasPermission = user.getRole().getRolePermissions().stream()
                .anyMatch(rolePermission ->
                        rolePermission.getPermission().getName().equalsIgnoreCase(permission.toString()));

        return hasRole && hasPermission;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId,
                                 String targetType, Object permission) {
        return hasPermission(authentication, targetId, permission);
    }
}
//@Component
//@AllArgsConstructor
//public class CustomPermissionEvaluator implements PermissionEvaluator {
//
//    private final UserRepository userRepository;
//
//    @Override
//    public boolean hasPermission(Authentication authentication, Object targetId, Object permission) {
//        String username = authentication.getName();
//        UserEntity user = userRepository.getUserByEmail(username)
//                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
//
//        // Check if user has permission on the given resource ID
//        // Now, check if the user has the required permission (without considering targetId)
//        return user.getRole().getRolePermissions().stream()
//                .anyMatch(rolePermission -> rolePermission.getPermission().getName().equalsIgnoreCase(permission.toString()));
//    }
//
//    @Override
//    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
//        return hasPermission(authentication, targetId, permission);
//    }
//}
