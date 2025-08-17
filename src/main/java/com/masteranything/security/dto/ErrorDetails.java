package com.masteranything.security.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;

@Builder
public record ErrorDetails(
    LocalDateTime timestamp,
    String errorMessage,
    List<String> validationErrors,
    String details) {

}
