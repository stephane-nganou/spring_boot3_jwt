package com.masteranything.security.service.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

/**
 * @author MasterAnything
 * <p>
 * This service is responsible for generating and validating JWT tokens.
 */
@Service
public class JwtService {

  @Value("${spring.security.auth.secret-key}")
  private String SECRET_KEY;

  @Value("${spring.security.auth.expiration}")
  private long EXPIRATION;

  public String retrieveUsername(String token) {
    return getClaims(token, Claims::getSubject);
  }

  public <T> T getClaims(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = getAllClaims(token);

    return claimsResolver.apply(claims);
  }

  public String generateToken(UserDetails userDetails) {
    return generateToken(new HashMap<>(), userDetails);
  }

  public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {

    var authorities = userDetails.getAuthorities()
        .stream()
        .map(GrantedAuthority::getAuthority)
        .toList();
    return Jwts
        .builder()
        .setClaims(extraClaims)
        .setSubject(userDetails.getUsername())
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
        .claim("authorities", authorities)
        .signWith(getSignInKey(), SignatureAlgorithm.HS256)
        .compact();
  }

  public boolean isTokenValid(String token, UserDetails userDetails) {
    final String username = retrieveUsername(token);
    return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
  }

  private boolean isTokenExpired(String token) {
    return getExpirationDate(token).before(new Date());
  }

  private Date getExpirationDate(String token) {
    //return getAllClaims(token).getExpiration();
    return getClaims(token, Claims::getExpiration);
  }

  private Claims getAllClaims(String token) {
    return Jwts
        .parserBuilder()
        .setSigningKey(getSignInKey())
        .build()
        .parseClaimsJws(token)
        .getBody();

  }

  private Key getSignInKey() {
    byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);

    return Keys.hmacShaKeyFor(keyBytes);
  }
}
