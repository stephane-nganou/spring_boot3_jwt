package com.masteranything.security.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.masteranything.security.dao.Book;
import com.masteranything.security.dao.BookSpecification;
import com.masteranything.security.dao.BookTransactionHistory;
import com.masteranything.security.dao.User;
import com.masteranything.security.dto.BookRequest;
import com.masteranything.security.dto.BookResponse;
import com.masteranything.security.dto.BorrowedBookResponse;
import com.masteranything.security.dto.PageResponse;
import com.masteranything.security.exception.GeneralException;
import com.masteranything.security.repository.BookRepository;
import com.masteranything.security.repository.BookTransactionHistoryRepository;
import com.masteranything.security.util.FactoryUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final BookTransactionHistoryRepository transactionHistory;

    public Long save(BookRequest request, Authentication connectedUser){
        
        var user = (User)connectedUser.getPrincipal();
        var book = FactoryUtils.convertToBook(request);
        book.setOwner(user);

        return bookRepository.save(book).getId();
    }

    public BookResponse findById(Long bookId){

        return bookRepository.findById(bookId)
                .map(FactoryUtils::convertToBookResponse)
                .orElseThrow(() -> new GeneralException("No Book found. Id: " + bookId, HttpStatus.NOT_FOUND));
        
    }

    public PageResponse<BookResponse> findAllBooks(int page, int size, Authentication connectedUser){
        
        var user = (User) connectedUser.getPrincipal();
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<Book> books = bookRepository.findAllDisplayableBooks(pageable, user.getId());
        List<BookResponse> bookResponse = books.stream()
                                                .map(FactoryUtils::convertToBookResponse)
                                                .toList();

        return new PageResponse<>(
            bookResponse,
            books.getNumber(),
            books.getSize(),
            books.getTotalElements(),
            books.getTotalPages(),
            books.isFirst(),
            books.isLast()
        );
    }

    public PageResponse<BookResponse> findAllBooksByOwner(int page, int size, Authentication connectedUser){
        
        var user = (User) connectedUser.getPrincipal();
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());

        //Page<Book> books = bookRepository.findAllByOwner(pageable, user.getId());
        Page<Book> books = bookRepository.findAll(BookSpecification.withOwnerId(user.getId()), pageable);

        List<BookResponse> bookResponse = books.stream()
                                                .map(FactoryUtils::convertToBookResponse)
                                                .toList();
        
        
        return new PageResponse<>(
            bookResponse,
            books.getNumber(),
            books.getSize(),
            books.getTotalElements(),
            books.getTotalPages(),
            books.isFirst(),
            books.isLast()
        );
    }

    public PageResponse<BorrowedBookResponse> findAllBorrowedBooks(int page, int size, Authentication connectedUser){
        
        var user = (User) connectedUser.getPrincipal();
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());

        //Page<Book> books = bookRepository.findAllByOwner(pageable, user.getId());
        Page<BookTransactionHistory> allBorrowedBooks = transactionHistory.findAllBorrowedBooks(pageable, user.getId());

        List<BorrowedBookResponse> borrowedBookResponse = allBorrowedBooks.stream()
                                                .map(FactoryUtils::convertToBorrowedBookResponse)
                                                .toList();

        return new PageResponse<>(
            borrowedBookResponse,
            allBorrowedBooks.getNumber(),
            allBorrowedBooks.getSize(),
            allBorrowedBooks.getTotalElements(),
            allBorrowedBooks.getTotalPages(),
            allBorrowedBooks.isFirst(),
            allBorrowedBooks.isLast()
        );
    }

    
}
