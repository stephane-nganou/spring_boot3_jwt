package com.masteranything.security.config.security;

import com.masteranything.security.service.security.JwtService;
import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

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

    final String authorizationHeader = request.getHeader(authHeader);

    if (null == authorizationHeader || !authorizationHeader.startsWith(authBearer)) {
      filterChain.doFilter(request, response);
    } else {
      handleNewAuthentication(request, response, filterChain);
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
}
