package com.ecommerce.service.Impl;

import com.ecommerce.entity.UserEntity;
import com.ecommerce.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CustomeUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> userOptional = userRepository.getUserByEmail(username);

        if (userOptional.isPresent()) {
            UserEntity user = userOptional.get();

            List<GrantedAuthority> authorities = getAuthorities(user);

            return User.builder()
                    .username(user.getEmail())
                    .password(user.getPassword())
                    .authorities(authorities) // Assign both roles and permissions
                    .build();
        } else {
            throw new UsernameNotFoundException("User not found: " + username);
        }
    }

    private List<GrantedAuthority> getAuthorities(UserEntity user) {
        List<GrantedAuthority> authorities = user.getRole().getRolePermissions().stream()
                .map(rp -> new SimpleGrantedAuthority(rp.getPermission().getName().toUpperCase()))
                .collect(Collectors.toList());

        // Add Role as "ROLE_ADMIN" or "ROLE_USER"
        authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole().getName().toUpperCase()));

        return authorities;
    }
}
