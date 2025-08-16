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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/feedbacks")
@RequiredArgsConstructor
@Tag(name="Feedback", description="Handles everything related to Feedback component")
public class FeedbackController {

    private final FeedbackService feedbackService;

    @PostMapping("/")
    @Operation(summary="saveFeedback", description="Submit data to save a new Feedback")
    @ApiResponses({
        @ApiResponse(responseCode="200", description="Feedback saved successfully"),
        @ApiResponse(responseCode="404", description="Book not found"),
        @ApiResponse(responseCode="406", description="Can not add feedback to the given book")
    })
    public ResponseEntity<Long> saveFeedback(
        @Valid @RequestBody FeedbackRequest request, 
        Authentication connectedUser) {
            return ResponseEntity.status(HttpStatus.OK)
            .body(feedbackService.saveFeedback(request, connectedUser));
        }
    
    @GetMapping("/book/{book-id}")
    @Operation(summary="findAllFeedbackByBook", description="Get all feedbacks for given Book")
    @ApiResponses({
        @ApiResponse(responseCode="200", description="Operation successfully"),
        @ApiResponse(responseCode="404", description="Book not found")
    })
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
