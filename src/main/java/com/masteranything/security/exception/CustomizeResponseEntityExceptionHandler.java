package com.masteranything.security.exception;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.masteranything.security.dto.ErrorDetails;

import jakarta.mail.MessagingException;

@ControllerAdvice
public class CustomizeResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

  private final Logger logger = LoggerFactory.getLogger(
      CustomizeResponseEntityExceptionHandler.class);

  @ExceptionHandler(Exception.class)
  public final ResponseEntity<ErrorDetails> handleAllException(Exception ex, WebRequest request) {
    logger.info(ex.getMessage());

    if (ex instanceof MethodArgumentNotValidException castEx) {
      return handleMethodArgumentNotValidException(castEx, request);
    } else {
      ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), "Server error occurred",
        request.getDescription(false));

    return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @ExceptionHandler(AuthenticationException.class)
  public final ResponseEntity<ErrorDetails> handleAuthenticationException(AuthenticationException ex,
      WebRequest request) {
    logger.info(ex.getMessage());

    ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), ex.getMessage(),
        request.getDescription(false));

    return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(InternalAuthenticationServiceException.class)
  public final ResponseEntity<ErrorDetails> handleInternalAuthenticationServiceException(InternalAuthenticationServiceException ex,
      WebRequest request) {
    logger.info(ex.getMessage());

    ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), "wrong User or Password",
        request.getDescription(false));

    return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(MessagingException.class)
  public final ResponseEntity<ErrorDetails> handleMessagingException(MessagingException ex,
      WebRequest request) {
    logger.info(ex.getMessage());

    ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), "Failed sending mail",
        request.getDescription(false));

    return new ResponseEntity<>(errorDetails, HttpStatus.SERVICE_UNAVAILABLE);
  }

  @ExceptionHandler(TokenException.class)
  public final ResponseEntity<ErrorDetails> handleTokenException(Exception ex,
      WebRequest request) {
    logger.info(ex.getMessage());

    ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), ex.getMessage(),
        request.getDescription(false));

    return new ResponseEntity<>(errorDetails, HttpStatus.FORBIDDEN);
  }

  @ExceptionHandler(GeneralException.class)
  public final ResponseEntity<ErrorDetails> handleGeneralException(GeneralException ex,
      WebRequest request) {
    logger.info(ex.getMessage());

    ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), ex.getMessage(),
        request.getDescription(false));

    return new ResponseEntity<>(errorDetails, ex.getStatus());
  }


  private ResponseEntity<ErrorDetails> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, 
    WebRequest request) {

        Set<String> errors = new HashSet<>();
        ex.getBindingResult().getAllErrors()
          .forEach(error -> {
            errors.add(error.getDefaultMessage());
          });

    logger.info(errors.toString());

    ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), errors.toString(),
        request.getDescription(false));

    return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
  }
}