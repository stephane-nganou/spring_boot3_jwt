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
import com.masteranything.security.dao.User;
import com.masteranything.security.dto.BookRequest;
import com.masteranything.security.dto.BookResponse;
import com.masteranything.security.dto.PageResponse;
import com.masteranything.security.exception.GeneralException;
import com.masteranything.security.repository.BookRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    public Long save(BookRequest request, Authentication connectedUser){
        
        var user = (User)connectedUser.getPrincipal();
        var book = toBook(request);
        book.setOwner(user);

        return bookRepository.save(book).getId();
    }

    public BookResponse findById(Long bookId){

        return bookRepository.findById(bookId)
                .map(this::toBookResponse)
                .orElseThrow(() -> new GeneralException("No Book found. Id: " + bookId, HttpStatus.NOT_FOUND));
        
    }

    public PageResponse<BookResponse> findAllBooks(int page, int size, Authentication connectedUser){
        
        var user = (User) connectedUser.getPrincipal();
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<Book> books = bookRepository.findAllDisplayableBooks(pageable, user.getId());
        List<BookResponse> bookResponse = books.stream()
                                                .map(this::toBookResponse)
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
                                                .map(this::toBookResponse)
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


    private Book toBook(BookRequest request){

        return Book.builder()
            .id(request.id())
            .title(request.title())
            .authorName(request.authorName())
            .synopsis(request.synopsis())
            .archived(false)
            .shareable(request.shareable())
            .build();
    }

    private BookResponse toBookResponse(Book book){
        return BookResponse.builder()
            .id(book.getId())
            .title(book.getTitle())
            .authourName(book.getAuthorName())
            .isbn(book.getIsbn())
            .synopsis(book.getSynopsis())
            .rate(book.getRate())
            .archived(book.isArchived())
            .shareable(book.isShareable())
            .owner(book.getOwner().getFullName())
            .build();
    }
}
