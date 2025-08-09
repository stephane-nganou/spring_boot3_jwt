package com.masteranything.security.service.security;

import com.masteranything.security.dto.AuthenticationRequest;
import com.masteranything.security.dto.AuthenticationResponse;
import com.masteranything.security.dto.RegisterRequest;

import jakarta.mail.MessagingException;

public interface AuthenticationService {

    void register(RegisterRequest registerRequest) throws MessagingException;

    void activateAccount(String token) throws MessagingException;

    AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest);
}
