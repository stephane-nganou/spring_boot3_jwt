package com.masteranything.security.service;

import org.springframework.security.core.Authentication;

import com.masteranything.security.dto.BookRequest;
import com.masteranything.security.dto.BookResponse;
import com.masteranything.security.dto.BorrowedBookResponse;
import com.masteranything.security.dto.PageResponse;

public interface BookService {

    Long save(BookRequest request, Authentication connectedUser);

    BookResponse findById(Long bookId);

    PageResponse<BookResponse> findAllBooks(int page, int size, Authentication connectedUser);

    PageResponse<BookResponse> findAllBooksByOwner(int page, int size, Authentication connectedUser);

    PageResponse<BorrowedBookResponse> findAllBorrowedBooks(int page, int size, Authentication connectedUser);

    PageResponse<BorrowedBookResponse> findAllReturnedBooks(int page, int size, Authentication connectedUser);

    Long updateShareableStatus(Long bookId, Authentication connectedUser);

    Long updateArchivedStatus(Long bookId, Authentication connectedUser);

    Long borrowBook(Long bookId, Authentication connectedUser);

    Long returnBorrowBook(Long bookId, Authentication connectedUser);

    Long approveReturnBook(Long bookId, Authentication connectedUser);
}
