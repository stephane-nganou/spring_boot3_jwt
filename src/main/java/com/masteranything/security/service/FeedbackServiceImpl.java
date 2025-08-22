package com.masteranything.security.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.masteranything.security.dao.Feedback;
import com.masteranything.security.dao.User;
import com.masteranything.security.dto.FeedbackRequest;
import com.masteranything.security.dto.FeedbackResponse;
import com.masteranything.security.dto.PageResponse;
import com.masteranything.security.exception.GeneralException;
import com.masteranything.security.repository.BookRepository;
import com.masteranything.security.repository.FeedbackRepository;
import com.masteranything.security.util.BookUtils;
import com.masteranything.security.util.FeedbackUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {
    
    private final static String BOOK_NOT_FOUND = "No Book found with ID: ";

    private final BookRepository bookRepository;
    private final FeedbackRepository feedbackRepository;

    @Override
    public Long saveFeedback(
        FeedbackRequest request, 
        Authentication connectedUser) {
        
        var book = bookRepository.findById(request.bookId())
            .orElseThrow(() -> new GeneralException(
                BOOK_NOT_FOUND + request.bookId(),
                HttpStatus.NOT_FOUND
            ));

        var user = (User) connectedUser.getPrincipal();
        BookUtils.checkIfNotOwnerBook(book, user);

        var feedback = FeedbackUtils.convertToFeedback(request);
        
        return feedbackRepository.save(feedback).getId();
    }

    @Override
    public PageResponse<FeedbackResponse> findAllFeedbacksByBook(Long bookId, int page, int size, Authentication connectedUser) {
        
        Pageable pageable = PageRequest.of(page, size);

        var user = (User) connectedUser.getPrincipal();

        Page<Feedback> feedbacks = feedbackRepository.findAllByBookId(pageable, bookId);
        
        List<FeedbackResponse> feedbackResponse = feedbacks.stream()
                                                    .map(feedback -> FeedbackUtils.convertToFeedbackResponse(feedback, user.getId()))
                                                    .toList();

        return new PageResponse<>(
            feedbackResponse,
            feedbacks.getNumber(),
            feedbacks.getSize(),
            feedbacks.getTotalElements(),
            feedbacks.getTotalPages(),
            feedbacks.isFirst(),
            feedbacks.isLast()
        );
    }

}
