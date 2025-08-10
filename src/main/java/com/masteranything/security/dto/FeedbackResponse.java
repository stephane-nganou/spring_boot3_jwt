package com.masteranything.security.dto;

import lombok.Builder;

@Builder
public record FeedbackResponse(
    Double note,
    String comment,
    boolean ownFeedback
) {

}
