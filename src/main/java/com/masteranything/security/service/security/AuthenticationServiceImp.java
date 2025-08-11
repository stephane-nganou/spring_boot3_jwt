package com.masteranything.security.service.security;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.masteranything.security.dao.Role;
import com.masteranything.security.dao.Token;
import com.masteranything.security.dao.User;
import com.masteranything.security.dto.AuthenticationRequest;
import com.masteranything.security.dto.AuthenticationResponse;
import com.masteranything.security.dto.EmailTemplateName;
import com.masteranything.security.dto.ROLE;
import com.masteranything.security.dto.RegisterRequest;
import com.masteranything.security.exception.AuthenticationException;
import com.masteranything.security.exception.GeneralException;
import com.masteranything.security.exception.TokenException;
import com.masteranything.security.repository.RoleRepository;
import com.masteranything.security.repository.TokenRepository;
import com.masteranything.security.repository.UserRepository;
import com.masteranything.security.service.EmailService;
import com.masteranything.security.service.UserService;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;

/**
 * @author Masteranythin
 * <p>
 * This Service handles the authentication process for the resgistration and login of users.
 */
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImp implements AuthenticationService {

  private final UserService userService;
  private final RoleRepository roleRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;
  private final TokenRepository tokenRepository;
  private final EmailService emailService;

  @Value("${spring.security.auth.characters}")
  private String CHARACTERS;
  @Value("${spring.security.auth.act-token-length}")
  private int ACTIVE_TOKEN_LENGTH;
  @Value("${spring.security.auth.act-token-exp-time}")
  private int ACTIVE_TOKEN_EXP_TIME;
  @Value("${spring.mailing.frontend.activation-url}")
  private String ACTIVATION_URL;
  @Value("${spring.mailing.subject.activation}")
  private String ACTIVATION_SUBJECT;

  /**
  public AuthenticationResponse register(RegisterRequest registerRequest) {
  
    Optional<Role> userRole = roleRepository.findByName(Role.USER.name())
    .orElseThrow(() -> new IllegalStateException("Role not found"));

    var user = User.builder()
        .firstName(registerRequest.firstName())
        .lastName(registerRequest.lastName())
        .email(registerRequest.email())
        .password(passwordEncoder.encode(registerRequest.password()))
        .accountLocked(false)
        .enabled(false)
        .roles(List.of(userRole))
        .build();

    userService.saveUser(user);
    var jwtToken = jwtService.generateToken(user);
    return new AuthenticationResponse(jwtToken);

  }
   * @throws MessagingException */
  
  @Override
  public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
    try {
      var auth = authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(
              authenticationRequest.email(),
              authenticationRequest.password()
          )
      );
      var claims = new HashMap<String, Object>();
      var user = ((User)auth.getPrincipal());
      if(!user.isEnabled()){
        throw new GeneralException("""
          Account not enable jetzt. Please activate using the E-Mail you received the day 
          of the registration.
        """,
        HttpStatus.FORBIDDEN);
      }

      claims.put("fullName", user.getFullName());
      
      return new AuthenticationResponse(jwtService.generateToken(claims, user));
      //var user = userService.findUserByEmail(authenticationRequest.email());
      //var jwtToken = jwtService.generateToken(user);

      //return new AuthenticationResponse(jwtToken);
    } catch (BadCredentialsException e) {
      throw new AuthenticationException("Invalid username or password");
    }
  }

  @Override
  public void register(RegisterRequest registerRequest) throws MessagingException {
  
    // to do: better throws and handling of IllegalStateException
    var userRole = roleRepository.findByName(ROLE.USER.name());
    if (userRole.isEmpty()){
      userRole = saveDefaultRole();
    }
    //.orElseThrow(() -> new IllegalStateException("Role not found"));

    var user = User.builder()
        .firstName(registerRequest.firstName())
        .lastName(registerRequest.lastName())
        .email(registerRequest.email())
        .password(passwordEncoder.encode(registerRequest.password()))
        .accountLocked(false)
        .enabled(false)
        .roles(List.of(userRole.get()))
        .build();

    userService.saveUser(user);
    
    sendValidationEmail(user);
  }

  @Override
  //@Transactional
  public void activateAccount(String token) throws MessagingException {
    Token savedToken = tokenRepository.findByToken(token)
      .orElseThrow(() -> new TokenException("Invalid Token"));

    var user = savedToken.getUser();

    if (LocalDateTime.now().isAfter(savedToken.getExpiresAt())){
      sendValidationEmail(user);

      throw new TokenException("Activation token expired. A new token has been sent");
    }

    user.setEnabled(true);
    userService.saveUser(user);
    savedToken.setValidatedAt(LocalDateTime.now());
    tokenRepository.save(savedToken);
  }
    

  private void sendValidationEmail(User user) throws MessagingException {
    var newToken = generateAndSaveActivationToken(user);
    
    emailService.sendEmail(
      user.getEmail(), user.getFullName(),
      EmailTemplateName.ACTIVATION_ACCOUNT, ACTIVATION_URL, newToken, ACTIVATION_SUBJECT);
  }

  private Optional<Role> saveDefaultRole(){
    Role defaultRole = roleRepository.save(Role.builder()
                          .name(ROLE.USER.name())
                          .build());

    return Optional.of(defaultRole);
  }

  private String generateAndSaveActivationToken(User user) {
    String generatedToken = generateActivationCode(ACTIVE_TOKEN_LENGTH);
    var token = Token.builder()
        .token(generatedToken)
        .createdAt(LocalDateTime.now())
        .expiresAt(LocalDateTime.now().plusMinutes(ACTIVE_TOKEN_EXP_TIME))
        .user(user)
        .build();

    tokenRepository.save(token);
    return generatedToken;
  }

  private String generateActivationCode(int length) {
    StringBuilder codeBuilder = new StringBuilder();
    SecureRandom secureRandom = new SecureRandom();
    for (int i = 0; i < length; i++) {
      int randomIndex = secureRandom.nextInt(CHARACTERS.length());
      codeBuilder.append(CHARACTERS.charAt(randomIndex));
    }

    return codeBuilder.toString();
  }

}
