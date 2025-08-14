package com.masteranything.security.config.shared;

import java.util.Arrays;
import java.util.Collections;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.AuditorAware;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * @author MasterAnything
 * <p>
 * Class responsible for configuring the application beans.
 */
@Configuration
@RequiredArgsConstructor
public class AppConfig {

  private final UserDetailsService userDetailsService;

  @Value("${frontend-host}")
  private String FRONTEND_HOST;


  @Bean
  public AuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider(
        userDetailsService);

    authenticationProvider.setPasswordEncoder(passwordEncoder());

    return authenticationProvider;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
      throws Exception {
    return config.getAuthenticationManager();
  }

  @Bean
  public AuditorAware<Long> auditorAware(){
    return new AppAuditAware();
  }

  /**
   * Bean responsible for handling cors.
   * This will used by default in the Security config througt: ".cors(withDefaults()"
   */
  @Bean
  public CorsFilter corsFilter(){
    
    final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    final CorsConfiguration config = new CorsConfiguration();
    config.setAllowCredentials(true);
    config.setAllowedOrigins(
      Collections.singletonList((FRONTEND_HOST))
    );

    config.setAllowedHeaders(Arrays.asList(
      HttpHeaders.ORIGIN,
      HttpHeaders.CONTENT_TYPE,
      HttpHeaders.ACCEPT,
      HttpHeaders.AUTHORIZATION
    ));

    config.setAllowedMethods(Arrays.asList(
      "GET",
      "POST",
      "DELETE",
      "PUT",
      "PATCH"  
    ));

    source.registerCorsConfiguration("/**", config);
    return  new CorsFilter(source);
  }
}
