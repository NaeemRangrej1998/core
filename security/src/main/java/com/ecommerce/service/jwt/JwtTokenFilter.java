//package com.ecommerce.service.jwt;
//
//import com.ecommerce.dto.response.ApiResponse;
//import com.ecommerce.exception.CustomException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import io.jsonwebtoken.ExpiredJwtException;
//import io.jsonwebtoken.JwtException;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//import org.springframework.lang.NonNull;
//import org.springframework.web.servlet.HandlerExceptionResolver;
//
//import java.io.IOException;
//
//@Component
//@RequiredArgsConstructor
//public class JwtTokenFilter extends OncePerRequestFilter {
//
//    private static final String INVALID_JWT_TOKEN = "Invalid JWT token";
//    private final JwtTokenProvider jwtTokenProvider;
//    private final HandlerExceptionResolver handlerExceptionResolver;
//    private final ObjectMapper objectMapper;
//
////    public JwtTokenFilter(JwtTokenProvider jwtTokenProvider) {
////        this.jwtTokenProvider = jwtTokenProvider;
////    }
//
//    @Override
//    protected void doFilterInternal(
//            @NonNull HttpServletRequest req,
//            @NonNull HttpServletResponse res,
//            @NonNull FilterChain filterChain
//    ) throws ServletException, IOException {
//        try {
//            final String authHeader = req.getHeader("Authorization");
//            System.out.println("token = " + authHeader);
//            String token = jwtTokenProvider.resolveToken(req);
////            System.out.println("token = " + token);
////
////            if ((token != null && !token.isEmpty())) {
////                try {
////                    jwtTokenProvider.isTokenValid(token);
////                } catch (JwtException | IllegalArgumentException e) {
////
////            //                    e.printStackTrace();
////            //                    res.sendError(HttpServletResponse.SC_UNAUTHORIZED, INVALID_JWT_TOKEN);
////                    throw new CustomException(INVALID_JWT_TOKEN, HttpStatus.UNAUTHORIZED);
////                }
////                Authentication auth = jwtTokenProvider.getAuthentication(token);
////                System.out.println("auth = " + auth.getAuthorities());
////                SecurityContextHolder.getContext().setAuthentication(auth);
////            }
////            filterChain.doFilter(req, res);
//            if (token != null && !token.isEmpty()) {
//                try {
//                    if (!jwtTokenProvider.isTokenValid(token)) {
//                        handleAuthenticationError(res, "Invalid token", HttpStatus.UNAUTHORIZED);
//                        return;
//                    }
//
//                    Authentication auth = jwtTokenProvider.getAuthentication(token);
//                    if (auth != null && auth.getAuthorities().isEmpty()) {
//                        handleAuthenticationError(res, "No authorities found", HttpStatus.FORBIDDEN);
//                        return;
//                    }
//
//                    SecurityContextHolder.getContext().setAuthentication(auth);
//                } catch (JwtException | IllegalArgumentException e) {
//                    handleAuthenticationError(res, INVALID_JWT_TOKEN, HttpStatus.UNAUTHORIZED);
//                    return;
//                }
//            }
//
//            filterChain.doFilter(req, res);
//        }  catch (ExpiredJwtException e) {
//            handleAuthenticationError(res, "Token has expired", HttpStatus.UNAUTHORIZED);
//        } catch (Exception e) {
//            handleAuthenticationError(res, e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//    private void handleAuthenticationError(HttpServletResponse response, String message, HttpStatus status)
//            throws IOException {
//        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//        response.setStatus(status.value());
//
//        ApiResponse apiResponse = new ApiResponse(
//                status,
//                message,
//                null
//        );
//
//        objectMapper.writeValue(response.getOutputStream(), apiResponse);
//    }
//}
package com.ecommerce.service.jwt;

import com.ecommerce.exception.CustomException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private static final String INVALID_JWT_TOKEN = "Invalid JWT token";
    private final JwtTokenProvider jwtTokenProvider;
    private final HandlerExceptionResolver handlerExceptionResolver;
//    public JwtTokenFilter(JwtTokenProvider jwtTokenProvider) {
//        this.jwtTokenProvider = jwtTokenProvider;
//    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest req,
            @NonNull HttpServletResponse res,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            final String authHeader = req.getHeader("Authorization");
            System.out.println("token = " + authHeader);
            String token = jwtTokenProvider.resolveToken(req);
            System.out.println("token = " + token);

            if ((token != null && !token.isEmpty())) {
                try {
                    jwtTokenProvider.isTokenValid(token);
                } catch (JwtException | IllegalArgumentException e) {

//                    e.printStackTrace();
//                    res.sendError(HttpServletResponse.SC_UNAUTHORIZED, INVALID_JWT_TOKEN);
                    throw new CustomException(INVALID_JWT_TOKEN, HttpStatus.UNAUTHORIZED);
                }
                Authentication auth = jwtTokenProvider.getAuthentication(token);
                System.out.println("auth = " + auth.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
            filterChain.doFilter(req, res);
        }  catch (ExpiredJwtException e) {
            System.out.println("e = " + e);
            this.handlerExceptionResolver.resolveException(req,res,null,e);
        } catch (Exception e) {
            System.out.println("e Exception = " + e.getMessage());
            this.handlerExceptionResolver.resolveException(req,res,null,e);
        }
    }
}
