package com.masteranything.security.dto;

import lombok.Builder;

@Builder
public record BookResponse(
    Long id,
    String title,
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
