package com.masteranything.security.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class GeneralException extends RuntimeException {

    private final HttpStatus status;
    public GeneralException(String message, HttpStatus status){
        super(message);
        this.status = status;
    }
}
