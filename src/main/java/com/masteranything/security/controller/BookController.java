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
import com.masteranything.security.dto.BorrowedBookResponse;
import com.masteranything.security.dto.PageResponse;
import com.masteranything.security.service.BookService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping
    public ResponseEntity<PageResponse<BookResponse>> getAllBooks(
        @RequestParam(name = "page", defaultValue="0", required=false) int page,
        @RequestParam(name = "size", defaultValue="10", required=false) int size,
        Authentication connectedUser
    ){
        // return new ResponseEntity<>(bookService.findAllBooks(page, size, connectedUser), HttpStatus.OK);
        return ResponseEntity.status(HttpStatus.OK)
            .body(bookService.findAllBooks(page, size, connectedUser));
    }

    @PostMapping
    public ResponseEntity<Long> saveBook(
        @Valid @RequestBody BookRequest request, Authentication connectedUser
    ){
        //return new ResponseEntity<>(bookService.save(request, connectedUser), HttpStatus.CREATED);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(bookService.save(request, connectedUser));
    }

    @GetMapping("{book-id}")
    public ResponseEntity<BookResponse> findBookById(@PathVariable("book-id") Long bookId ){
        // return new ResponseEntity<>(bookService.findById(bookId), HttpStatus.OK);
        return ResponseEntity.status(HttpStatus.OK)
            .body(bookService.findById(bookId));
    }

    @GetMapping("/owner")
    public ResponseEntity<PageResponse<BookResponse>> getAllBooksByOwner(
        @RequestParam(name = "page", defaultValue="0", required=false) int page,
        @RequestParam(name = "size", defaultValue="10", required=false) int size,
        Authentication connectedUser
    ){
        //return new ResponseEntity<>(bookService.findAllBooksByOwner(page, size, connectedUser), HttpStatus.OK);

        return ResponseEntity.status(HttpStatus.OK)
            .body(bookService.findAllBooksByOwner(page, size, connectedUser));
    }

    @GetMapping("/borrowed")
    public ResponseEntity<PageResponse<BorrowedBookResponse>> getAllBorrowedBooks(
        @RequestParam(name = "page", defaultValue="0", required=false) int page,
        @RequestParam(name = "size", defaultValue="10", required=false) int size,
        Authentication connectedUser
    ){
        return ResponseEntity.status(HttpStatus.OK)
            .body(bookService.findAllBorrowedBooks(page, size, connectedUser));
    }

    @GetMapping("/returned")
    public ResponseEntity<PageResponse<BorrowedBookResponse>> getAllReturnedBooks(
        @RequestParam(name = "page", defaultValue="0", required=false) int page,
        @RequestParam(name = "size", defaultValue="10", required=false) int size,
        Authentication connectedUser
    ){
        return ResponseEntity.status(HttpStatus.OK)
            .body(bookService.findAllReturnedBooks(page, size, connectedUser));
    }

}
