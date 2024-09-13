package com.ecommerce.enums;

public enum ExceptionEnum {
    ACCESS_DENIED("Access denied"),
    EXPIRE_JWT_TOKEN("Expire Jwt Token"),
    SOMETHING_WENT_WRONG("Something went wrong"),
    USER_EXISTS("User with email '%s' already Exists", "USER_EXISTS"),
    USER_NOT_FOUND("User not found","USER_NOT_FOUND"),
    PASSWORD_NOT_CORRECT("Password not correct","PASSWORD_NOT_CORRECT");
    ExceptionEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public String getMessage() {
        return message;
    }

    ExceptionEnum(String value, String message) {
        this.value = value;
        this.message = message;
    }

    private String value;
    private String message;
}
