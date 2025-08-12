package com.masteranything.security.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record BookRequest(
    //@Pattern(regexp="[0-9]", message="id must contain only number")
    Long id,

    @Size(min = 1, message = "100 title is required and should be at least 1 characters")
    String title,

    @Size(min = 1, message = "101 authorName is required and should be at least 1 characters")
    @JsonProperty("author_name")
    String authorName,

    @Size(min = 10, message = "102 isbn is required and should be at least 10 characters")
    String isbn,

    @Size(min = 10, message = "103 synopsis is required and should be at least 1 characters")
    String synopsis,

    @NotNull(message="shareable must be set")
    boolean shareable
) {

}
