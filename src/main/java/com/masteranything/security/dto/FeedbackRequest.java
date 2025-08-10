package com.masteranything.security.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record FeedbackRequest(
    @Positive(message="200")
    @Min(value = 0, message="201")
    @Max(value = 5, message="202")
    Double note,

    @Size(min=1, message="203")
    String comment,

    @NotNull(message="204")
    Long bookId
) {

}
