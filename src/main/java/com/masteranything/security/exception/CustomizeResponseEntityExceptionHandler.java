package com.masteranything.security.exception;

import com.masteranything.security.dto.ErrorDetails;
import java.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomizeResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

  private final Logger logger = LoggerFactory.getLogger(
      CustomizeResponseEntityExceptionHandler.class);

  @ExceptionHandler(Exception.class)
  public final ResponseEntity<ErrorDetails> handleAllException(Exception ex, WebRequest request) {
    logger.info(ex.getMessage());

    ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), "Server error occurred",
        request.getDescription(false));

    return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(AuthenticationException.class)
  public final ResponseEntity<ErrorDetails> handleAuthenticationException(Exception ex,
      WebRequest request) {
    logger.info(ex.getMessage());

    ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), ex.getMessage(),
        request.getDescription(false));

    return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(InternalAuthenticationServiceException.class)
  public final ResponseEntity<ErrorDetails> handleInternalAuthenticationServiceException(Exception ex,
      WebRequest request) {
    logger.info(ex.getMessage());

    ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), "wrong User or Password",
        request.getDescription(false));

    return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
  }
}