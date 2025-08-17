package com.masteranything.security.config.security;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.masteranything.security.dto.ErrorDetails;
import com.masteranything.security.service.security.JwtService;

import io.jsonwebtoken.MalformedJwtException;
import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

/**
 * @author MasterAnything
 *
 * Class responsible for filtering requests and adding authentication to the context.
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

  private final JwtService jwtService;
  private final UserDetailsService userDetailsService;

  @Value("${spring.security.auth.header}")
  private String authHeader;

  @Value("${spring.security.auth.bearer}")
  private String authBearer;

  @Override
  protected void doFilterInternal(@Nonnull HttpServletRequest request,
      @Nonnull HttpServletResponse response,
      @Nonnull FilterChain filterChain) throws ServletException, IOException {

    try{
      if(request.getServletPath().contains("/api/v1/auth")){
        filterChain.doFilter(request, response);
        return ;
      }

      final String authorizationHeader = request.getHeader(authHeader);

      if (null == authorizationHeader || !authorizationHeader.startsWith(authBearer)) {
        filterChain.doFilter(request, response);
      } else {
        handleNewAuthentication(request, response, filterChain);
      }
    } catch (MalformedJwtException e) {
      logger.error("JWT authentication error: {}", e.getMessage());
      response.setContentType(MediaType.APPLICATION_JSON_VALUE);
      response.setStatus(HttpStatus.UNAUTHORIZED.value());
      
      ErrorDetails errorDetails = ErrorDetails.builder()
                                    .timestamp(LocalDateTime.now())
                                    .errorMessage("Invalid JWT token")
                                    .validationErrors(List.of())
                                    .details("")
                                    .build();
      

            
            // Let Spring's HttpMessageConverter handle serialization
      response.getWriter().write(convertToJson(errorDetails));
      response.getWriter().flush();
    }
  }

  private void handleNewAuthentication(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    final String jwt;
    final String userEmail;
    jwt = request.getHeader(authHeader).substring(authBearer.length());
    userEmail = jwtService.retrieveUsername(jwt);
    if (null != userEmail && null == SecurityContextHolder.getContext().getAuthentication()) {
      UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
      if (jwtService.isTokenValid(jwt, userDetails)) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
            userDetails, null, userDetails.getAuthorities());
        authenticationToken.setDetails(
            new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        filterChain.doFilter(request, response);
      }
    }
  }

  private String convertToJson(ErrorDetails errorDetails) {
        return String.format(
            "{\"timestamp\":\"%s\",\"message\":\"%s\",\"details\":\"%s\"}",
            errorDetails.timestamp(),
            errorDetails.errorMessage(),
            errorDetails.details()
        );
    }
  
}
