package com.masteranything.security.service;

import org.springframework.security.core.Authentication;

import com.masteranything.security.dto.FeedbackRequest;
import com.masteranything.security.dto.FeedbackResponse;
import com.masteranything.security.dto.PageResponse;

public interface FeedbackService {

    Long saveFeedback (FeedbackRequest request, Authentication connectedUser);

    PageResponse<FeedbackResponse> findAllFeedbacksByBook(Long bookId, int page, int size, Authentication connectedUser);
}
