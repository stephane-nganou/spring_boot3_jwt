package com.masteranything.security.service;

import java.util.List;
import java.util.Objects;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
import com.masteranything.security.util.BookUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookServiceImp implements BookService {

    private final BookRepository bookRepository;
    private final BookTransactionHistoryRepository transactionHistoryRepository;
    private final FileStorageService fileStorageService; 

    private final static String NOT_PERMITTED = "Operation Not Permitted";
    private final static String BOOK_NOT_FOUND = "No Book found with ID: ";

    @Override
    public Long save(BookRequest request, Authentication connectedUser){
        
        var user = (User)connectedUser.getPrincipal();
        var book = BookUtils.convertToBook(request);
        book.setOwner(user);

        return bookRepository.save(book).getId();
    }

    @Override
    public BookResponse findById(Long bookId){

        return bookRepository.findById(bookId)
                .map(BookUtils::convertToBookResponse)
                .orElseThrow(() -> new GeneralException("No Book found. Id: " + bookId, HttpStatus.NOT_FOUND));
        
    }

    @Override
    public PageResponse<BookResponse> findAllBooks(int page, int size, Authentication connectedUser){
        
        var user = (User) connectedUser.getPrincipal();
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<Book> books = bookRepository.findAllDisplayableBooks(pageable, user.getId());
        //Predicate<Book> isNotArchivePredicate = book -> !book.isArchived();
        List<BookResponse> bookResponse = books.stream()
                                                //.filter(isNotArchivePredicate)
                                                .map(BookUtils::convertToBookResponse)
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

    @Override
    public PageResponse<BookResponse> findAllBooksByOwner(int page, int size, Authentication connectedUser){
        
        var user = (User) connectedUser.getPrincipal();
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());

        //Page<Book> books = bookRepository.findAllByOwner(pageable, user.getId());
        Page<Book> books = bookRepository.findAll(BookSpecification.withOwnerId(user.getId()), pageable);

        List<BookResponse> bookResponse = books.stream()
                                                .map(BookUtils::convertToBookResponse)
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

    @Override
    public PageResponse<BorrowedBookResponse> findAllBorrowedBooks(int page, int size, Authentication connectedUser){
        
        var user = (User) connectedUser.getPrincipal();
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());

        //Page<Book> books = bookRepository.findAllByOwner(pageable, user.getId());
        Page<BookTransactionHistory> allBorrowedBooks = transactionHistoryRepository.findAllBorrowedBooks(pageable, user.getId());

        List<BorrowedBookResponse> borrowedBookResponse = allBorrowedBooks.stream()
                                                .map(BookUtils::convertToBorrowedBookResponse)
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

    @Override
    public PageResponse<BorrowedBookResponse> findAllReturnedBooks(int page, int size, Authentication connectedUser) {
        var user = (User) connectedUser.getPrincipal();
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());

        
        Page<BookTransactionHistory> allBorrowedBooks = transactionHistoryRepository.findAllReturnedBooks(pageable, user.getId());

        //Predicate<BookTransactionHistory> bookReturnedPredicate = history -> (history.isReturnApproved()) && (history.isReturned());
        List<BorrowedBookResponse> borrowedBookResponse = allBorrowedBooks.stream()
                                                //.filter(bookReturnedPredicate)
                                                .map(BookUtils::convertToBorrowedBookResponse)
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

    @Override
    public Long updateShareableStatus(Long bookId, Authentication connectedUser) {

        var book = bookRepository.findById(bookId)
            .orElseThrow(() -> new GeneralException(
                BOOK_NOT_FOUND + bookId,
                HttpStatus.NOT_FOUND
            ));

        var user = (User) connectedUser.getPrincipal();

        if(!Objects.equals(book.getOwner().getId(), user.getId()))
            throw new GeneralException(NOT_PERMITTED, HttpStatus.FORBIDDEN);
        
        book.setShareable(!book.isShareable());
        bookRepository.save(book);

        return book.getId();

    }

    @Override
    public Long updateArchivedStatus(Long bookId, Authentication connectedUser) {

        var book = bookRepository.findById(bookId)
            .orElseThrow(() -> new GeneralException(
                BOOK_NOT_FOUND + bookId,
                HttpStatus.NOT_FOUND
            ));

        var user = (User) connectedUser.getPrincipal();

        if(!Objects.equals(book.getOwner().getId(), user.getId()))
            throw new GeneralException(NOT_PERMITTED, HttpStatus.FORBIDDEN);
        
        book.setArchived(!book.isArchived());
        bookRepository.save(book);

        return book.getId();

    }

    @Override
    public Long borrowBook(Long bookId, Authentication connectedUser) {

        var book = bookRepository.findById(bookId)
            .orElseThrow(() -> new GeneralException(
                BOOK_NOT_FOUND + bookId,
                HttpStatus.NOT_FOUND
            ));

        

        var user = (User) connectedUser.getPrincipal();

        BookUtils.checkIfBookArchivedOrShareable(book);

        BookUtils.checkIfNotOwnerBook(book, user);

        BookUtils.checkIfAlreadyBorrowed(book, user);  
        
        if(transactionHistoryRepository.isAlreadyBorrowedByUser(bookId))
            throw new GeneralException("Book already Borrowed", HttpStatus.NOT_ACCEPTABLE);

        
        var bookTransactionHistory = BookTransactionHistory.builder()
                                        .user(user)
                                        .book(book)
                                        .returned(false)
                                        .returnApproved(false)
                                        .build();

        return transactionHistoryRepository.save(bookTransactionHistory).getId();

    }

    @Override
    public Long returnBorrowBook(Long bookId, Authentication connectedUser) {

        var book = bookRepository.findById(bookId)
            .orElseThrow(() -> new GeneralException(
                BOOK_NOT_FOUND + bookId,
                HttpStatus.NOT_FOUND
            ));


        var user = (User) connectedUser.getPrincipal();

        BookUtils.checkIfBookArchivedOrShareable(book);

        BookUtils.checkIfNotOwnerBook(book, user);

        BookTransactionHistory bookTransactionHistory = transactionHistoryRepository
                                                            .findByBookIdAndUserId(bookId, user.getId())
                                                            .orElseThrow(() -> new GeneralException("No Transaction found", HttpStatus.NOT_FOUND));

        bookTransactionHistory.setReturned(true);

        return transactionHistoryRepository.save(bookTransactionHistory).getId();
    }

    @Override
    public Long approveReturnBook(Long bookId, Authentication connectedUser) {

        var book = bookRepository.findById(bookId)
            .orElseThrow(() -> new GeneralException(
                BOOK_NOT_FOUND + bookId,
                HttpStatus.NOT_FOUND
            ));


        var user = (User) connectedUser.getPrincipal();

        BookUtils.checkIfBookArchivedOrShareable(book);

        BookUtils.checkIfOwnerBook(book, user);

        BookTransactionHistory bookTransactionHistory = transactionHistoryRepository
                                                            .findByBookIdAndOwnerId(bookId, user.getId())
                                                            .orElseThrow(() -> new GeneralException("No Transaction found", HttpStatus.NOT_FOUND));

        bookTransactionHistory.setReturnApproved(false);

        return transactionHistoryRepository.save(bookTransactionHistory).getId();
    }

    @Override
    public void uploadBookCoverPicture(MultipartFile file, Long bookId, Authentication connectedUser) {

        var book = bookRepository.findById(bookId)
            .orElseThrow(() -> new GeneralException(
                BOOK_NOT_FOUND + bookId,
                HttpStatus.NOT_FOUND
            ));


        var user = (User) connectedUser.getPrincipal();

        BookUtils.checkIfOwnerBook(book, user);

        var bookCover = fileStorageService.saveFile(file, bookId);
        book.setBookCover(bookCover);
        bookRepository.save(book);
    }

    
}
