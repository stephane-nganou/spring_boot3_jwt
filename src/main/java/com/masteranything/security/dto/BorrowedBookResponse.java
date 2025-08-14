package com.masteranything.security.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;

@Builder
public record BorrowedBookResponse(
    Long id,
    String title,
    @JsonProperty("author_name")
    String authorName,
    String isbn,
    double rate,
    boolean returned,
    @JsonProperty("return_approval")
    boolean returnApproval
) {

}
