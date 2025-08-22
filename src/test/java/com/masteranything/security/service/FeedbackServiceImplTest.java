package com.masteranything.security.service;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.masteranything.security.dao.Book;
import com.masteranything.security.dao.Feedback;
import com.masteranything.security.dao.User;
import com.masteranything.security.dto.FeedbackRequest;
import com.masteranything.security.dto.FeedbackResponse;
import com.masteranything.security.dto.PageResponse;
import com.masteranything.security.repository.BookRepository;
import com.masteranything.security.repository.FeedbackRepository;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

/**
 *
 * @author StephaneWafo
 * 
 * Class aiming to test the FeedbackServiceImpl class
 */
@ExtendWith(MockitoExtension.class)
class FeedbackServiceImplTest {
    @Mock
    private BookRepository bookRepository;
    @Mock
    private FeedbackRepository feedbackRepository;
    @Mock
    private Authentication connectedUser;

    @InjectMocks
    private FeedbackServiceImpl feedbackService;

    private User user;
    private Book book;
    private FeedbackRequest feedbackRequest;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .build();
        book = Book.builder()
                .id(1L)
                .owner(user)
                .shareable(true)
                .archived(false)
                .build();
        feedbackRequest = FeedbackRequest.builder()
                            .note(4D)
                            .comment("comment test")
                            .bookId(book.getId())
                            .build();
    }

    @Test
    void whenSaveFeedback_WithValidFeedbackRequest_ThenReturnFeedbackId(){

        // prepare
        User user_2 = User.builder()
                        .id(2L)
                        .build();
        Feedback feedbackResponse = Feedback.builder()
                                        .id(1L)
                                        .book(book)
                                        .build();
        when(bookRepository.findById(any(Long.class))).thenReturn(Optional.of(book));
        when(connectedUser.getPrincipal()).thenReturn(user_2);
        when(feedbackRepository.save(any(Feedback.class))).thenReturn(feedbackResponse);

        // test
        Long savedFeedbackId = feedbackService.saveFeedback(feedbackRequest, connectedUser);

        // verify
        assertEquals(savedFeedbackId, feedbackResponse.getId());
        verify(feedbackRepository, times(1)).save(any(Feedback.class));
    }

    @Test
    void whenfindAllFeedbacksByBook_ThenReturnLstOfFeedbackResponseInPageResponse(){

        // prepare
        Feedback feedback = Feedback.builder()
                                    .id(1L)
                                    .book(book)
                                    .build();
        Feedback feedback_2 = Feedback.builder()
                                        .id(2L)
                                        .book(book)
                                        .build();
        Feedback feedback_3 = Feedback.builder()
                                        .id(3L)
                                        .book(book)
                                        .build();
        List<Feedback> feedbackList = List.of(feedback, feedback_2, feedback_3);
        Page<Feedback> page = new PageImpl<>(feedbackList);
        when(connectedUser.getPrincipal()).thenReturn(user);
        when(feedbackRepository.findAllByBookId(
            any(Pageable.class),
            any(Long.class)
            )
        ).thenReturn(page);

        // test
        PageResponse<FeedbackResponse> feedbackListResponse = feedbackService.findAllFeedbacksByBook(
                                                        book.getId(),
                                                        0,
                                                        10,
                                                        connectedUser
                                                    );

        // verify
        verify(feedbackRepository, times(1)).findAllByBookId(
            any(Pageable.class), any(Long.class));
        assertEquals(feedbackList.size(), feedbackListResponse.size());
        assertEquals(feedbackList.size(), feedbackListResponse.totalElements());
        assertEquals(0, feedbackListResponse.number());
        assertEquals(true, feedbackListResponse.first());
        assertEquals(true, feedbackListResponse.last());
    }

}
