//package com.ecommerce.security;
//
//import com.ecommerce.config.CrossOriginFilter;
//import com.ecommerce.service.Impl.CustomeUserDetailService;
////import com.ecommerce.service.jwt.JwtAuthenticationEntryPoint;
//import com.ecommerce.service.jwt.JwtTokenFilter;
//import lombok.AllArgsConstructor;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationProvider;
//import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
//import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//@EnableWebSecurity
//@AllArgsConstructor
//@EnableMethodSecurity
//@Configuration
//public class SecurityConfig {
//
//    private JwtTokenFilter jwtTokenFilter;
//
//    private final CrossOriginFilter crossOriginFilter;
////    private JwtAuthenticationEntryPoint authenticationEntryPoint;
//
//    //todo try to create custom
//
//    @Autowired
//    private CustomeUserDetailService uds; //todo try to create custom
//
//    private final PasswordEncoder passwordEncoder;
//
//    @Bean
//    public AuthenticationProvider authenticationProvider() {
//        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
//        authenticationProvider.setUserDetailsService(uds);
//        authenticationProvider.setPasswordEncoder(passwordEncoder);
//        return authenticationProvider;
//    }
//
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
//        httpSecurity.csrf(csrf -> csrf.disable())
//                .cors(configure -> configure.configurationSource(crossOriginFilter.corsConfigurationSource()))
//                .authorizeHttpRequests((authorize) -> authorize
//                        .requestMatchers(PUBLICURL).permitAll()
//                        .requestMatchers("/role/*").hasAuthority("READ")
//                        .anyRequest().authenticated()
//                )
////                .exceptionHandling(ex -> ex.authenticationEntryPoint(authenticationEntryPoint))
//                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .authenticationProvider(authenticationProvider())
//                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
////        httpSecurity.cors(cors -> cors.configurationSource(corsConfig.corsFilter()));
//
//        return httpSecurity.build();
//    }
//
//    private static  String[] PUBLICURL= {
//            "/v3/api-docs/**",
//            "/swagger-ui/**",
//            "/auth/*",
//            "/excel/upload",
//    };
//
//}
package com.ecommerce.security;

import com.ecommerce.config.CrossOriginFilter;
import com.ecommerce.dto.response.ApiResponse;
import com.ecommerce.service.Impl.CustomPermissionEvaluator;
import com.ecommerce.service.Impl.CustomeUserDetailService;
import com.ecommerce.service.jwt.JwtTokenFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@AllArgsConstructor
@EnableMethodSecurity(prePostEnabled = true)
@Configuration
public class SecurityConfig {

    private JwtTokenFilter jwtTokenFilter;
    private final CrossOriginFilter crossOriginFilter;
    @Autowired
    private CustomeUserDetailService uds;
    private final PasswordEncoder passwordEncoder;
    private final CustomPermissionEvaluator customPermissionEvaluator;

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(uds);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return authenticationProvider;
    }

    @Bean
    public DefaultMethodSecurityExpressionHandler methodSecurityExpressionHandler() {
        DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
        expressionHandler.setPermissionEvaluator(customPermissionEvaluator);
        return expressionHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(csrf -> csrf.disable())
                .cors(configure -> configure.configurationSource(crossOriginFilter.corsConfigurationSource()))
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers(PUBLICURL).permitAll()
//                        .requestMatchers("/role/*").hasAuthority("READ")
                        .anyRequest().authenticated()
                )
//                .exceptionHandling(exceptions -> exceptions
//                        .authenticationEntryPoint((request, response, authException) -> {
//                            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//                            response.setStatus(HttpStatus.UNAUTHORIZED.value());
//
//                            ApiResponse apiResponse = new ApiResponse(
//                                    HttpStatus.UNAUTHORIZED,
//                                    "Authentication failed: " + authException.getMessage(),
//                                    null
//                            );
//
//                            new ObjectMapper().writeValue(response.getOutputStream(), apiResponse);
//                        })
//                        .accessDeniedHandler((request, response, accessDeniedException) -> {
//                            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//                            response.setStatus(HttpStatus.FORBIDDEN.value());
//
//                            ApiResponse apiResponse = new ApiResponse(
//                                    HttpStatus.FORBIDDEN,
//                                    "Access Denied: You don't have the required permissions",
//                                    null
//                            );
//
//                            new ObjectMapper().writeValue(response.getOutputStream(), apiResponse);
//                        })
//                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

    private static final String[] PUBLICURL = {
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/auth/*",
            "/excel/upload",
    };
}