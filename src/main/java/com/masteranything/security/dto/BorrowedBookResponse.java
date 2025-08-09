package com.masteranything.security.dto;

import lombok.Builder;

@Builder
public record BorrowedBookResponse(
    Long id,
    String title,
    String authorName,
    String isbn,
    double rate,
    boolean returned,
    boolean returnApproval
) {

}
