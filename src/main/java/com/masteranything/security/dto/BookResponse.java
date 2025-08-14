package com.masteranything.security.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;

@Builder
public record BookResponse(
    Long id,
    String title,
    @JsonProperty("author_name")
    String authourName,
    String isbn,
    String synopsis,
    String owner,
    byte[] cover,
    double rate,
    boolean archived,
    boolean shareable
) {

}
