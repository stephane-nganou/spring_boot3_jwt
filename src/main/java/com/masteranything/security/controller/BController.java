package com.masteranything.security.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/secure")
public class BController {

  @GetMapping
  public ResponseEntity<String> getB() {
    return ResponseEntity.ok("B Secure");
  }
}
