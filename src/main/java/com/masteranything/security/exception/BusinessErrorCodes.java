package com.masteranything.security.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum BusinessErrorCodes {

    NO_CODE(0, HttpStatus.NOT_IMPLEMENTED, "No Code"),
    INCORRECT_CURRENT_PASSWORD (300, HttpStatus.BAD_REQUEST, "Incorrect User Or Password"),
    NEW_PASSWORD_DOES_NOT_MATCH (301, HttpStatus.BAD_REQUEST, "Password does not match"),
    ACCOUNT_LOCKED (302, HttpStatus.FORBIDDEN, "Account is locked"),
    ;

    //@Getter
    private final int code;
    //@Getter
    private final String description;
    //@Getter
    private final HttpStatus httpStatus;


    BusinessErrorCodes(int code, HttpStatus httpStatus, String description){
        this.code = code;
        this.httpStatus = httpStatus;
        this.description = description;
    }
}
