package com.masteranything.security.dto;

import jakarta.validation.constraints.Size;

public record BookRequest(
    // regex for allowed characters
    Long id,
    @Size(min = 1, message = "100 title is required and should be at least 1 characters")
    String title,
    @Size(min = 1, message = "101 authorName is required and should be at least 1 characters")
    String authorName,
    @Size(min = 10, message = "102 isbn is required and should be at least 10 characters")
    String isbn,
    @Size(min = 10, message = "103 synopsis is required and should be at least 1 characters")
    String synopsis,

    boolean shareable
) {

}
