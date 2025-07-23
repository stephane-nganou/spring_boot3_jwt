package com.masteranything.security.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Masteranything
 *
 * This class is responsible for handling requests to the /api/v1/secure endpoint.
 */
@RestController
@RequestMapping("/api/v1/secure")
public class BController {

  /**
   * Dummy endpoint to test the security configuration.
   */
  @GetMapping
  public ResponseEntity<String> getB() {
    return ResponseEntity.ok("B Secure");
  }
}
