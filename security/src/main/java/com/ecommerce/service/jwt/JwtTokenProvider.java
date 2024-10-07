package com.ecommerce.service.jwt;

import com.ecommerce.dto.response.JwtResponseDto;
import com.ecommerce.dto.response.RefreshTokenResponseDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Component
public class JwtTokenProvider {
    private final UserDetailsService userDetailsService;

    private String secretKey ="3cfa76ef14937c1c0ea519f8fc057a80fcd04a7420f8e8bcd0a7567c272e007b";
//
//    @Value("${security.jwt.expiration-time}")
//    private long jwtExpiration;

    private static final String APPLICATION_ROLE = "role";

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        if (bearerToken != null) {
            return bearerToken;
        }
        return bearerToken;
    }

    public boolean isTokenValid(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSignInKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            // Token is invalid
            return false;
        }
    }

    public String getUsername(String token) {
        String subject = Jwts.parserBuilder()  // Use parserBuilder() instead of parser()
                .setSigningKey(getSignInKey())  // Use getSignInKey() to get the signing key
                .build()  // Build the parser
                .parseClaimsJws(token)  // Parse the token
                .getBody()  // Extract the body of the JWT
                .getSubject();
        System.out.println("subject = " + subject);
        return subject;  // Get the subject (typically the username)
    }

    public String createAccessToken(String username, String role, Long id) {
        System.out.println("role = " + role);
        Claims claims = Jwts.claims().setSubject(username);
//        claims.put("APPLICATION_ROLE","ROLE_"+role.toUpperCase());
        claims.put("APPLICATION_ROLE", role);
        claims.put("USER_ID", id);
        Date now = new Date();
        Date validity = new Date(now.getTime() + 900000);
        String token = Jwts.builder()//
                .setClaims(claims)//
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(validity)
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
        System.out.println("creare token = " + token);
        return token; // Return the token wrapped in JwtResponseDto

    }

    public Authentication getAuthentication(String token) throws JsonProcessingException {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        String role = getRole(token);
        System.out.println("role getAuthentication= " + role);
        grantedAuthorities.add(new SimpleGrantedAuthority(role));
        String userEmail = getUsername(token);
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
        return new UsernamePasswordAuthenticationToken(userDetails, null, grantedAuthorities);
    }

    public String getRole(String token) {
        return (String) Jwts.parserBuilder()  // Use parserBuilder() instead of parser()
                .setSigningKey(getSignInKey())  // Use getSignInKey() to get the signing key
                .build()  // Build the parser
                .parseClaimsJws(token)  // Parse the token
                .getBody()  // Extract the body of the JWT
                .get("APPLICATION_ROLE");  // Cast the role claim to a String
    }

    public Long getUserId(String token) {
        return Long.parseLong(Jwts.parserBuilder()  // Use parserBuilder() instead of parser()
                .setSigningKey(getSignInKey())  // Use getSignInKey() to get the signing key
                .build()  // Build the parser
                .parseClaimsJws(token)  // Parse the token
                .getBody()  // Extract the body of the JWT
                .get("USER_ID").toString());  // Cast the role claim to a String
    }

    public RefreshTokenResponseDTO creatTokenFromRefreshToken(String token) {
        System.out.println("new token " + token);
        Claims claims = Jwts.claims().setSubject(getUsername(token));
        claims.put("USER_ID", getUserId(token));
        claims.put("APPLICATION_ROLE", getRole(token));
        Date now = new Date();
        Date validity = new Date(now.getTime() + 86400000);
        String newAccessTokenGenerateFromOldToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(validity)
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();

        return new RefreshTokenResponseDTO(newAccessTokenGenerateFromOldToken);
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
