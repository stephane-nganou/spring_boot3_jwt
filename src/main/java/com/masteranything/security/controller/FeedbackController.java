package com.masteranything.security.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.masteranything.security.dto.FeedbackRequest;
import com.masteranything.security.dto.FeedbackResponse;
import com.masteranything.security.dto.PageResponse;
import com.masteranything.security.service.FeedbackService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/feedbacks")
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackService feedbackService;

    @PostMapping("/")
    public ResponseEntity<Long> saveFeedback(
        @Valid @RequestBody FeedbackRequest request, 
        Authentication connectedUser) {
            return ResponseEntity.status(HttpStatus.OK)
            .body(feedbackService.saveFeedback(request, connectedUser));
        }
    
    @GetMapping("/book/{book-id}")
    public ResponseEntity<PageResponse<FeedbackResponse>> findAllFeedbackByBook(
        @PathVariable("book-id") Long bookId,
        @RequestParam(name = "page", defaultValue = "0", required = false) int page,
        @RequestParam(name = "size", defaultValue = "10", required = false) int size,
        Authentication connectedUser
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(feedbackService.findAllFeedbacksByBook(bookId, page, size, connectedUser));
    }
}
