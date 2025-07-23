package com.masteranything.security.service.security;

import com.masteranything.security.dto.AuthenticationRequest;
import com.masteranything.security.dto.AuthenticationResponse;
import com.masteranything.security.dto.RegisterRequest;
import com.masteranything.security.repository.UserRepository;
import com.masteranything.security.user.Role;
import com.masteranything.security.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @author Masteranythin
 *
 * This Service handles the authentication process for the resgistration and login of users.
 */
@Service
@RequiredArgsConstructor
public class AuthenticationService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;

  public AuthenticationResponse register(RegisterRequest registerRequest) {
    var user = User.builder()
        .firstName(registerRequest.firstName())
        .lastName(registerRequest.lastName())
        .email(registerRequest.email())
        .password(passwordEncoder.encode(registerRequest.password()))
        .role(Role.USER)
        .build();

    userRepository.save(user);

    var jwtToken = jwtService.generateToken(user);

    return new AuthenticationResponse(jwtToken);
  }

  public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
    try {
      authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(
              authenticationRequest.email(),
              authenticationRequest.password()
          )
      );
      var user = userRepository.findUserByEmail(authenticationRequest.email())
          .orElseThrow(() -> new UsernameNotFoundException("User not found"));
      var jwtToken = jwtService.generateToken(user);

      return new AuthenticationResponse(jwtToken);
    } catch (BadCredentialsException e) {
      throw new BadCredentialsException("Invalid username or password");
    }
  }
}
