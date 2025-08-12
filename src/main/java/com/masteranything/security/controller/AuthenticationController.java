package com.masteranything.security.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.masteranything.security.dto.AuthenticationRequest;
import com.masteranything.security.dto.AuthenticationResponse;
import com.masteranything.security.dto.RegisterRequest;
import com.masteranything.security.service.security.AuthenticationService;

import jakarta.annotation.Nonnull;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * @author MasterAnything
 *
 * This class is responsible for handling authentication requests.
 */

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
//@Tag(name = "Authentication Controller", description = "This controller is responsible for handling authentication requests.")
public class AuthenticationController {

  private final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);
  private final AuthenticationService authenticationService;

  /**
   * This endpoint is used to authenticate a user and return a JWT token as AuthenticationResponse
   * @param authenticationRequest as AuthenticationRequest
   * @return AuthenticationResponse
   */
  @PostMapping()
  // Endpoint tested
  public ResponseEntity<AuthenticationResponse> authenticate(
      @RequestBody @Valid AuthenticationRequest authenticationRequest) {

    return ResponseEntity.ok(authenticationService.authenticate(authenticationRequest));
  }


  /**
   * This endpoint is used to register a new user and return a JWT token as AuthenticationResponse
   * @param registerRequest as RegisterRequest
   * @return AuthenticationResponse
   * @throws MessagingException 
   */
  @PostMapping("/register")
  // Endpoint tested
  public ResponseEntity<?> register(
      @RequestBody @Valid RegisterRequest registerRequest) throws MessagingException {

    logger.info("incoming request to register user: {}", registerRequest);
    //return new ResponseEntity<>(authenticationService.register(registerRequest), HttpStatus.CREATED);
    authenticationService.register(registerRequest);
    return ResponseEntity.accepted().build();
  }

  @GetMapping("/activate-account")
  // Endpoint tested
  public ResponseEntity<?> confirm(@RequestParam @Nonnull String token) throws MessagingException{
    authenticationService.activateAccount(token);

    return ResponseEntity.ok().build();
  }

}
