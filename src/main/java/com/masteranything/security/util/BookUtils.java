package com.masteranything.security.util;

import java.util.Objects;

import org.springframework.http.HttpStatus;

import com.masteranything.security.dao.Book;
import com.masteranything.security.dao.BookTransactionHistory;
import com.masteranything.security.dao.User;
import com.masteranything.security.dto.BookRequest;
import com.masteranything.security.dto.BookResponse;
import com.masteranything.security.dto.BorrowedBookResponse;
import com.masteranything.security.exception.GeneralException;

public class BookUtils {

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
            .isbn(request.isbn())
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

    public static void checkIfBookArchivedOrShareable(Book book){
        if(book.isArchived() || !book.isShareable())
            throw new GeneralException("Book unavailable", HttpStatus.NOT_ACCEPTABLE);
    }


    public static void checkIfBookArchivedOrNotShareable(Book book){
        if(book.isArchived() || book.isShareable())
            throw new GeneralException("Book unavailable", HttpStatus.NOT_ACCEPTABLE);
    }

    public static void checkIfBookArchived(Book book){
        if(book.isArchived())
            throw new GeneralException("Book unavailable", HttpStatus.NOT_ACCEPTABLE);
    }

    public static void checkIfNotOwnerBook(Book book, User user){
        if(Objects.equals(book.getOwner().getId(), user.getId()))
            throw new GeneralException("Can not update own book", HttpStatus.NOT_ACCEPTABLE);
    }

    public static void checkIfOwnerBook(Book book, User user){
        if(!Objects.equals(book.getOwner().getId(), user.getId()))
            throw new GeneralException("Can not update this book", HttpStatus.FORBIDDEN);
    }

    
    public static void checkIfAlreadyBorrowed(Book book, User user){
        if(Objects.equals(book.getOwner().getId(), user.getId()))
            throw new GeneralException("Can not borrowed book", HttpStatus.NOT_ACCEPTABLE);
    }
}
