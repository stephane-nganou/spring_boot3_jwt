package com.masteranything.security.controller;

import com.masteranything.security.dto.AuthenticationRequest;
import com.masteranything.security.dto.AuthenticationResponse;
import com.masteranything.security.dto.RegisterRequest;
import com.masteranything.security.service.security.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

  private final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);
  private final AuthenticationService authenticationService;

  @PostMapping("/")
  public ResponseEntity<AuthenticationResponse> authenticate(
      @RequestBody @Valid AuthenticationRequest authenticationRequest) {

    return ResponseEntity.ok(authenticationService.authenticate(authenticationRequest));
  }


  @PostMapping("/register")
  public ResponseEntity<AuthenticationResponse> register(
      @RequestBody @Valid RegisterRequest registerRequest) {

    logger.info("incoming request to register user: {}", registerRequest);
    return ResponseEntity.ok(authenticationService.register(registerRequest));
  }

}
