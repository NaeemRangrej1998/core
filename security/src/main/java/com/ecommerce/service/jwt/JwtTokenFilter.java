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
