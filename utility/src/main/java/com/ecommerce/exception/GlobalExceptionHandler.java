package com.ecommerce.exception;

import com.ecommerce.dto.request.ErrorDetailsDTO;
import com.ecommerce.enums.ExceptionEnum;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Date;

@RestControllerAdvice
public class GlobalExceptionHandler {
//    @ExceptionHandler(UserNotFoundException.class)
//    public ResponseEntity<?> handleUserNotFoundException(UserNotFoundException exception, HttpRequest httpRequest) {
//        ErrorDetailsDTO detailsDTO = new ErrorDetailsDTO(new Date(), exception.getMessage(), ExceptionEnum.USER_NOT_FOUND.getValue());
//        return ResponseEntity
//                .status(HttpStatus.INTERNAL_SERVER_ERROR)
//                .body(detailsDTO);
//    }
//
//    @ExceptionHandler(UserAlreadyExistsException.class)
//    public ResponseEntity<Object> handleUserAlreadyExistsException(UserAlreadyExistsException exception) {
//        HttpStatus httpStatus = HttpStatus.
//        ErrorDetailsDTO detailsDTO = new ErrorDetailsDTO(new Date(), exception.getMessage(), ExceptionEnum.USER_EXISTS.getValue());
//        return ResponseEntity
//                .status(HttpStatus.INTERNAL_SERVER_ERROR)
//                .body(detailsDTO);
//    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(HttpServletRequest req, Exception e) {
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        ErrorDetailsDTO errorDTO = new ErrorDetailsDTO(httpStatus,new Date().getTime(), ExceptionEnum.SOMETHING_WENT_WRONG.getValue(), req.getServletPath());
        return ResponseEntity.status(httpStatus).body(errorDTO);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<?> handleExpiredJwtException(HttpServletRequest req, ExpiredJwtException e) {
        System.out.println("e.getMessage() = " + e.getMessage());
        HttpStatus httpStatus = HttpStatus.FORBIDDEN;
        ErrorDetailsDTO errorDTO = new ErrorDetailsDTO(httpStatus,new Date().getTime(), ExceptionEnum.EXPIRE_JWT_TOKEN.getValue(), req.getServletPath());
        errorDTO.setError(e.getMessage());
        return ResponseEntity.status(httpStatus).body(errorDTO);
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<?> handleJwtException(JwtException e ,HttpServletRequest req) {
        HttpStatus httpStatus = HttpStatus.FORBIDDEN;
        ErrorDetailsDTO errorDTO = new ErrorDetailsDTO(httpStatus,new Date().getTime(), e.getMessage(), req.getServletPath());
        errorDTO.setError(e.getMessage());
        return ResponseEntity.status(httpStatus).body(errorDTO);
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<?> handleCustomException(HttpServletRequest req, CustomException e) {
        HttpStatus httpStatus = e.getHttpStatus();
        ErrorDetailsDTO errorDTO = new ErrorDetailsDTO(httpStatus, new Date().getTime(), e.getMessage(), req.getServletPath());
        errorDTO.setError(e.getMessage());
        errorDTO.setMessage(httpStatus.name());
        return ResponseEntity.status(httpStatus).body(errorDTO);
    }
}
