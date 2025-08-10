package com.masteranything.security.util;

import java.util.Objects;

import com.masteranything.security.dao.Book;
import com.masteranything.security.dao.Feedback;
import com.masteranything.security.dto.FeedbackRequest;
import com.masteranything.security.dto.FeedbackResponse;

public class FeedbackUtils {

    public static Feedback convertToFeedback(FeedbackRequest request){

        var book = Book.builder()
                        .id(request.bookId())
                        .archived(false)
                        .shareable(false)
                        .build();

        return Feedback.builder()
                .note(request.note())
                .comment(request.comment())
                .book(book)
                .build();
    }

    public static FeedbackResponse convertToFeedbackResponse(Feedback feedback, Long userId){
        return FeedbackResponse.builder()
                .note(feedback.getNote())
                .comment(feedback.getComment())
                .ownFeedback(Objects.equals(feedback.getCreatedBy(), userId))
                .build();
    }

}
