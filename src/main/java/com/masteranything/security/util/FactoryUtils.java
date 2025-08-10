package com.masteranything.security.util;

import com.masteranything.security.dao.Book;
import com.masteranything.security.dao.BookTransactionHistory;
import com.masteranything.security.dto.BookRequest;
import com.masteranything.security.dto.BookResponse;
import com.masteranything.security.dto.BorrowedBookResponse;

public class FactoryUtils {

    public static BookResponse convertToBookResponse(Book book){
        return BookResponse.builder()
            .id(book.getId())
            .title(book.getTitle())
            .authourName(book.getAuthorName())
            .isbn(book.getIsbn())
            .synopsis(book.getSynopsis())
            .rate(book.getRate())
            .archived(book.isArchived())
            .shareable(book.isShareable())
            .cover(FileUtils.readFileFromLocation(book.getBookCover()))
            .owner(book.getOwner().getFullName())
            .build();
    }

    public static Book convertToBook(BookRequest request){

        return Book.builder()
            .id(request.id())
            .title(request.title())
            .authorName(request.authorName())
            .synopsis(request.synopsis())
            .archived(false)
            .shareable(request.shareable())
            .build();
    }

    public static BorrowedBookResponse convertToBorrowedBookResponse(BookTransactionHistory history){

        return BorrowedBookResponse.builder()
            .id(history.getBook().getId())
            .title(history.getBook().getTitle())
            .authorName(history.getBook().getAuthorName())
            .isbn(history.getBook().getIsbn())
            .rate(history.getBook().getRate())
            .returned(history.isReturned())
            .returnApproval(history.isReturnApproved())
            .build();
    }
}
