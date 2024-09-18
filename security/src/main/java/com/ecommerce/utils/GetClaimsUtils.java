package com.ecommerce.utils;

import com.ecommerce.dto.request.GetTokenClaimsDTO;
import com.ecommerce.service.jwt.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

@Component
public class GetClaimsUtils {

    private final JwtTokenProvider jwtTokenProvider;

    public GetClaimsUtils(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }


    public GetTokenClaimsDTO getClaims(HttpServletRequest request){
        System.out.println("token getClaims = " + request.getHeader("Authorization"));
        String token = jwtTokenProvider.resolveToken(request);
        System.out.println("token getClaims = " + token);
        return new GetTokenClaimsDTO(jwtTokenProvider.getUserId(token), jwtTokenProvider.getUsername(token), jwtTokenProvider.getRole(token));
    }
}
