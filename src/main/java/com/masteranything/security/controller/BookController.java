package com.masteranything.security.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.masteranything.security.dto.BookRequest;
import com.masteranything.security.dto.BookResponse;
import com.masteranything.security.dto.BorrowedBookResponse;
import com.masteranything.security.dto.PageResponse;
import com.masteranything.security.service.BookService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping("/")
    // Endpoint tested
    public ResponseEntity<PageResponse<BookResponse>> getAllBooks(
        @RequestParam(name = "page", defaultValue="0", required=false) int page,
        @RequestParam(name = "size", defaultValue="10", required=false) int size,
        Authentication connectedUser
    ){
        // return new ResponseEntity<>(bookService.findAllBooks(page, size, connectedUser), HttpStatus.OK);
        return ResponseEntity.status(HttpStatus.OK)
            .body(bookService.findAllBooks(page, size, connectedUser));
    }

    @PostMapping("/")
    // Endpoint tested
    public ResponseEntity<Long> saveBook(
        @Valid @RequestBody BookRequest request, Authentication connectedUser
    ){
        //return new ResponseEntity<>(bookService.save(request, connectedUser), HttpStatus.CREATED);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(bookService.save(request, connectedUser));
    }

    @GetMapping("/{book-id}")
    // Endpoint tested
    public ResponseEntity<BookResponse> findBookById(@PathVariable("book-id") Long bookId ){
        // return new ResponseEntity<>(bookService.findById(bookId), HttpStatus.OK);
        return ResponseEntity.status(HttpStatus.OK)
            .body(bookService.findById(bookId));
    }

    @GetMapping("/owner")
    // Endpoint tested
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

    @PatchMapping("/shareable/{book-id}")
    public ResponseEntity<Long> updateShareableStatus(@PathVariable Long bookId, Authentication connectedUser){
        
        return ResponseEntity.status(HttpStatus.OK)
            .body(bookService.updateShareableStatus(bookId, connectedUser));
    }

    @PostMapping("/borrow/{book-id}")
    public ResponseEntity<Long> borrowBook(@PathVariable Long bookId, Authentication connectedUser){
        
        return ResponseEntity.status(HttpStatus.OK)
            .body(bookService.borrowBook(bookId, connectedUser));
    }

    @PatchMapping("/borrow/return/{book-id}")
    public ResponseEntity<Long> returnBorrowBook(@PathVariable Long bookId, Authentication connectedUser){
        
        return ResponseEntity.status(HttpStatus.OK)
            .body(bookService.returnBorrowBook(bookId, connectedUser));
    }

    @PatchMapping("/borrow/return/approve/{book-id}")
    public ResponseEntity<Long> approveReturnBorrowBook(@PathVariable Long bookId, Authentication connectedUser){
        
        return ResponseEntity.status(HttpStatus.OK)
            .body(bookService.approveReturnBook(bookId, connectedUser));
    }

    @PostMapping(value="/cover/{book-id}", consumes="multipart/form-data")
    public ResponseEntity<?> uploadBookCoverPicture(@PathVariable Long bookId,
        @RequestPart("file") MultipartFile file, Authentication connectedUser){
        
        bookService.uploadBookCoverPicture(file, bookId, connectedUser);
        
        return ResponseEntity.ok().build();
    }


}
