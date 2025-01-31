package com.ecommerce.service.Impl;

import com.ecommerce.dto.request.ForgotPasswordDto;
import com.ecommerce.dto.request.LoginRequestDto;
import com.ecommerce.dto.request.ResetPasswordTokenDto;
import com.ecommerce.dto.response.JwtResponseDto;
import com.ecommerce.dto.response.RefreshTokenResponseDTO;
import com.ecommerce.entity.ResetTokenEntity;
import com.ecommerce.entity.RoleMappingEntity;
import com.ecommerce.entity.UserEntity;
import com.ecommerce.enums.ExceptionEnum;
import com.ecommerce.exception.CustomException;
import com.ecommerce.repository.RoleMappingRepository;
import com.ecommerce.repository.RoleRepository;
import com.ecommerce.repository.TokenRepository;
import com.ecommerce.repository.UserRepository;
import com.ecommerce.service.LoginService;
import com.ecommerce.service.jwt.JwtTokenProvider;
import com.ecommerce.utils.CommonUtils;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {
    private static final Long VALIDITY_MILLI_SEC = 6 * 60 * 60 * 1000L;

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final RoleMappingRepository roleMappingRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender javaMailSender;
    private final TokenRepository tokenRepository;

    @Override
    public JwtResponseDto singIn(LoginRequestDto loginRequestDto) {
        UserEntity user = userRepository.getUserByEmail(loginRequestDto.getEmail()).orElseThrow(() -> new CustomException(ExceptionEnum.USER_NOT_FOUND.getValue(), HttpStatus.NOT_FOUND));

//        String role =user.getRole().getName();
        if (!passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword())) {
            throw new CustomException(ExceptionEnum.PASSWORD_NOT_CORRECT.getValue(), HttpStatus.BAD_REQUEST);
        }
        return getTokenResponse(user);
    }

    @Override
    public RefreshTokenResponseDTO generateRefreshTokenFromOldToken(HttpServletRequest request) {
        return generateRefreshToken(request);
    }

    @Override
    public String getUserByEmail(ForgotPasswordDto forgotPasswordDto) {

//        UserEntity userEntity = userRepository.getUserByEmail(forgotPasswordDto.getEmail()).orElseThrow(() -> new CustomException(ExceptionEnum.USER_EMAIL_NOT_FOUND.getValue(), HttpStatus.NOT_FOUND));
        Optional<UserEntity> userEntity = userRepository.getUserByEmail(forgotPasswordDto.getEmail());

        // userEntity validation
        if (userEntity.isEmpty()) {
//            LOGGER.error("generateForgotPasswordTokenAndSendEmail :: User with email {} does not exists", email);
            throw new CustomException(ExceptionEnum.USER_DETAILS_NOT_FOUND.getValue(), HttpStatus.NOT_FOUND);
        }
        Instant nowInstant = Instant.now();
        Instant validTillInstant = nowInstant.plusMillis(VALIDITY_MILLI_SEC);
        LocalDateTime validTillLocalDateTime = LocalDateTime.ofInstant(validTillInstant, ZoneOffset.UTC);
        try {
            String resetToken = generateForgotPasswordToken(userEntity.get(), validTillInstant.toEpochMilli());
            ResetTokenEntity tokenEntity = new ResetTokenEntity();
            tokenEntity.setToken(resetToken);
            tokenEntity.setUser(userEntity.get());
            tokenEntity.setTokenValidTill(validTillLocalDateTime);
            tokenEntity.setCreatedDate(LocalDateTime.now());
            tokenRepository.save(tokenEntity);
            // sending email to email id provided
            String emailUrl = forgotPasswordDto.getHostName()+"reset-password" + "?" + "token"+ "=" + resetToken;
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom("naim.softvan@gmail.com");
            mailMessage.setTo(userEntity.get().getEmail());
            mailMessage.setSubject("Welcome");
            mailMessage.setText("Hello\n\nPlease Click On this Link To Reset Your Password: " + emailUrl);
            System.out.println("mailMessage = " + mailMessage);

            javaMailSender.send(mailMessage); // This is likely where the issue occurs
            return String.format("FORGOT_PASSWORD_SET_SUCCESSFULLY", forgotPasswordDto.getEmail());
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            // Log any unexpected exceptions
            e.printStackTrace();
            throw new CustomException("EMAIL_SENDING_FAILED", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Override
    public String resetpassword(ResetPasswordTokenDto resetPasswordTokenDto) {
        String token = resetPasswordTokenDto.getResetToken();
        // token db check
        ResetTokenEntity resetTokenEntity = tokenRepository.findByToken(token).orElseThrow(() -> new CustomException(ExceptionEnum.FORGOT_PASSWORD_DETAILS_NOT_FOUND.getValue(), HttpStatus.NOT_FOUND));

//        UserEntity user = userRepository.getUserByEmail(resetPasswordTokenDto.getResetToken()).orElseThrow(() -> new CustomException(ExceptionEnum.USER_NOT_FOUND.getValue(), HttpStatus.NOT_FOUND));
//        System.out.println("before user = " + user);
//        if (user == null) {
//            throw new CustomException(ExceptionEnum.USER_NOT_FOUND.getValue(), HttpStatus.NOT_FOUND);
//        }
        UserEntity userEntity = resetTokenEntity.getUser();
        userEntity.setPassword(passwordEncoder.encode(resetPasswordTokenDto.getNewPassword()));
        userEntity.setUpdatedDate(CommonUtils.getDateTime());
        userEntity.setUpdatedBy(userEntity);
        System.out.println("before user = " + userEntity);
        userRepository.save(userEntity);
        return String.format("PASSWORD_SET_SUCCESSFULLY");
    }

    private RefreshTokenResponseDTO generateRefreshToken(HttpServletRequest request) {
        String refToken = jwtTokenProvider.resolveToken(request);
        if ((refToken != null && !refToken.isEmpty())) {
            try {
                jwtTokenProvider.isTokenValid(refToken);
            } catch (JwtException | IllegalArgumentException e) {
            }
        }
        return jwtTokenProvider.creatTokenFromRefreshToken(refToken);
    }

    private JwtResponseDto getTokenResponse(UserEntity user) {
        String userRole;
        Optional<RoleMappingEntity> userRoleMappingEntity = roleMappingRepository.findByUserEntity(user);

        if (userRoleMappingEntity.isPresent()) {
            userRole = userRoleMappingEntity.get().getRoleEntity().getName();
        } else {
            throw new CustomException(ExceptionEnum.USER_ROLE_NOT_FOUND.getValue(), HttpStatus.NOT_FOUND);
        }

        return new JwtResponseDto(jwtTokenProvider.createAccessToken(user.getEmail(), userRole, user.getId()), user.getId(), userRole, user.getFirstName());
    }

    public String generateResetToken(UserEntity user) {
        UUID uuid = UUID.randomUUID();
        LocalDateTime currentDateTime = LocalDateTime.now();
        LocalDateTime expiryDateTime = currentDateTime.plusMinutes(10);
        ResetTokenEntity resetToken = new ResetTokenEntity();
        resetToken.setToken(uuid.toString());
        resetToken.setUser(user);
        resetToken.setTokenValidTill(expiryDateTime);
        resetToken.setCreatedDate(LocalDateTime.now());
        ResetTokenEntity token = tokenRepository.save(resetToken);
        if (token != null) {
            String endpointUrl = "http://localhost:3001/reset-password";
            return endpointUrl + "?" + "token" + "=" + resetToken.getToken();
        }
        return "";
    }

    private String generateForgotPasswordToken(UserEntity user, long epochMilli) {
        Date now = new Date();
        Date validTillDate = new Date(epochMilli);
        Date validity = new Date(validTillDate.getTime());
        Map<String, Object> claimsMap = new HashMap<>();
        claimsMap.put("email", user.getEmail());
        return Jwts.builder()//
                .addClaims(claimsMap)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode("3cfa76ef14937c1c0ea519f8fc057a80fcd04a7420f8e8bcd0a7567c272e007b");
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
