package com.masteranything.security.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;

@Builder
public record FeedbackResponse(
    Double note,
    String comment,
    @JsonProperty("own_feedback")
    boolean ownFeedback
) {

}
