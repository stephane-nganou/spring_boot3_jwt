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

import com.masteranything.security.dto.BookRequest;
import com.masteranything.security.dto.BookResponse;
import com.masteranything.security.dto.PageResponse;
import com.masteranything.security.service.BookService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @PostMapping
    public ResponseEntity<Long> saveBook(
        @Valid @RequestBody BookRequest request, Authentication connectedUser
    ){
        return new ResponseEntity<>(bookService.save(request, connectedUser), HttpStatus.CREATED);
    }

    @GetMapping("{book-id}")
    public ResponseEntity<BookResponse> findBookById(@PathVariable("book-id") Long bookId ){
        return new ResponseEntity<>(bookService.findById(bookId), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<PageResponse<BookResponse>> getAllBooks(
        @RequestParam(name = "page", defaultValue="0", required=false) int page,
        @RequestParam(name = "size", defaultValue="10", required=false) int size,
        Authentication connectedUser
    ){
        return new ResponseEntity<>(bookService.findAllBooks(page, size, connectedUser), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<PageResponse<BookResponse>> getAllBooksByOwner(
        @RequestParam(name = "page", defaultValue="0", required=false) int page,
        @RequestParam(name = "size", defaultValue="10", required=false) int size,
        Authentication connectedUser
    ){
        return new ResponseEntity<>(bookService.findAllBooksByOwner(page, size, connectedUser), HttpStatus.OK);
    }
}
