package com.ecommerce.enums;

public enum ExceptionEnum {
    ACCESS_DENIED("Access denied"),
    USER_ROLE_NOT_FOUND("User Role Not Found"),
    EXPIRE_JWT_TOKEN("Expire Jwt Token"),
    SOMETHING_WENT_WRONG("Something went wrong"),
    USER_EXISTS("User with this email already Exists", "USER_EXISTS"),
    ROLE_NOT_FOUND("Role Not Found","ROLE_NOT_FOUND"),
    USER_NOT_FOUND("User not found","USER_NOT_FOUND"),
    PASSWORD_NOT_CORRECT("Password not correct","PASSWORD_NOT_CORRECT"),
    USER_EMAIL_NOT_FOUND("User email not found","USER_EMAIL_NOT_FOUND"),
    USER_DETAILS_NOT_FOUND("User Not Found","USER_DETAILS_NOT_FOUND"),
    USER_DELETED_WITH_ID("User with id '%s' is deleted", "USER_DELETED_WITH_ID"),
    ROLE_DELETED_WITH_ID("Role with id '%s' is deleted", "ROLE_DELETED_WITH_ID"),
    GIVEN_STATUS_AND_DATABASE_STATUS_IS_SAME("Given status and database status is same",
            "GIVEN_STATUS_AND_DATABASE_STATUS_IS_SAME"),
    FORGOT_PASSWORD_DETAILS_NOT_FOUND("Forgot Password Details Not Found");
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
