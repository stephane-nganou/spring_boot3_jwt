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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/books")
@RequiredArgsConstructor
@Tag(name="Book", description="Handles everything related to book component")
public class BookController {

    private final BookService bookService;

    @GetMapping("/")
    // Endpoint tested
    @Operation(summary="getAllBooks", description="Returns a list of all available books")
    @ApiResponses({
        @ApiResponse(responseCode="200", description="List return successfully")
    })
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
    @Operation(summary="saveBook", description="Submit data to save a new book")
    @ApiResponses({
        @ApiResponse(responseCode="201", description="Book register successfully")
    })
    public ResponseEntity<Long> saveBook(
        @Valid @RequestBody BookRequest request, Authentication connectedUser
    ){
        //return new ResponseEntity<>(bookService.save(request, connectedUser), HttpStatus.CREATED);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(bookService.save(request, connectedUser));
    }

    @GetMapping("/{book-id}")
    // Endpoint tested
    @Operation(summary="findBookById", description="Search for a book by Id")
    @ApiResponses({
        @ApiResponse(responseCode="200", description="Book found"),
        @ApiResponse(responseCode="404", description="Book not found")
    })
    public ResponseEntity<BookResponse> findBookById(@PathVariable("book-id") Long bookId ){
        // return new ResponseEntity<>(bookService.findById(bookId), HttpStatus.OK);
        return ResponseEntity.status(HttpStatus.OK)
            .body(bookService.findById(bookId));
    }

    @GetMapping("/owner")
    // Endpoint tested
    @Operation(summary="getAllBooksByOwner", description="Get all books belonging to the authenticated user")
    @ApiResponses({
        @ApiResponse(responseCode="200", description="List of books")
    })
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
    @Operation(summary="getAllBorrowedBooks", description="Get all books borrowed by the authenticated user")
    @ApiResponses({
        @ApiResponse(responseCode="200", description="List of books")
    })
    public ResponseEntity<PageResponse<BorrowedBookResponse>> getAllBorrowedBooks(
        @RequestParam(name = "page", defaultValue="0", required=false) int page,
        @RequestParam(name = "size", defaultValue="10", required=false) int size,
        Authentication connectedUser
    ){
        return ResponseEntity.status(HttpStatus.OK)
            .body(bookService.findAllBorrowedBooks(page, size, connectedUser));
    }

    @GetMapping("/returned")
    @Operation(summary="getAllReturnedBooks", description="Get all books returned by the authenticated user")
    @ApiResponses({
        @ApiResponse(responseCode="200", description="List of books")
    })
    public ResponseEntity<PageResponse<BorrowedBookResponse>> getAllReturnedBooks(
        @RequestParam(name = "page", defaultValue="0", required=false) int page,
        @RequestParam(name = "size", defaultValue="10", required=false) int size,
        Authentication connectedUser
    ){
        return ResponseEntity.status(HttpStatus.OK)
            .body(bookService.findAllReturnedBooks(page, size, connectedUser));
    }

    @PatchMapping("/shareable/{book-id}")
    @Operation(summary="updateShareableStatus", description="Update the shareable status of a targeted book")
    @ApiResponses({
        @ApiResponse(responseCode="200", description="Book updated successfully"),
        @ApiResponse(responseCode="403", description="Operation not permitted because not book owner"),
        @ApiResponse(responseCode="404", description="Book not found")
    })
    public ResponseEntity<Long> updateShareableStatus(@PathVariable Long bookId, Authentication connectedUser){
        
        return ResponseEntity.status(HttpStatus.OK)
            .body(bookService.updateShareableStatus(bookId, connectedUser));
    }

    @PatchMapping("/archive/{book-id}")
    @Operation(summary="updateArchivedStatus", description="Update the archive status of a targeted book")
    @ApiResponses({
        @ApiResponse(responseCode="200", description="Book updated successfully"),
        @ApiResponse(responseCode="403", description="Operation not permitted because not book owner"),
        @ApiResponse(responseCode="404", description="Book not found")
    })
    public ResponseEntity<Long> updateArchivedStatus(@PathVariable Long bookId, Authentication connectedUser){
        
        return ResponseEntity.status(HttpStatus.OK)
            .body(bookService.updateArchivedStatus(bookId, connectedUser));
    }

    @PostMapping("/borrow/{book-id}")
    @Operation(summary="borrowBook", description="borrow a desired book")
    @ApiResponses({
        @ApiResponse(responseCode="200", description="Book borrowed successfully"),
        @ApiResponse(responseCode="403", description="Operation not permitted because book owner"),
        @ApiResponse(responseCode="404", description="Book not found"),
        @ApiResponse(responseCode="406", description="Book can not be borrowed")
    })
    public ResponseEntity<Long> borrowBook(@PathVariable Long bookId, Authentication connectedUser){
        
        return ResponseEntity.status(HttpStatus.OK)
            .body(bookService.borrowBook(bookId, connectedUser));
    }

    @PatchMapping("/borrow/return/{book-id}")
    @Operation(summary="returnBorrowBook", description="Return a borrowed book")
    @ApiResponses({
        @ApiResponse(responseCode="200", description="Return operation successfully"),
        @ApiResponse(responseCode="404", description="Book not found"),
        @ApiResponse(responseCode="406", description="Book can not be returned")
    })
    public ResponseEntity<Long> returnBorrowBook(@PathVariable Long bookId, Authentication connectedUser){
        
        return ResponseEntity.status(HttpStatus.OK)
            .body(bookService.returnBorrowBook(bookId, connectedUser));
    }

    @PatchMapping("/borrow/return/approve/{book-id}")
    @Operation(summary="approveReturnBorrowBook", description="Approve the Return of a borrowed book")
    @ApiResponses({
        @ApiResponse(responseCode="200", description="Approve successfully"),
        @ApiResponse(responseCode="403", description="Operation not permitted because not book owner"),
        @ApiResponse(responseCode="404", description="Book not found"),
        @ApiResponse(responseCode="406", description="Return can not be approved")
    })
    public ResponseEntity<Long> approveReturnBorrowBook(@PathVariable Long bookId, Authentication connectedUser){
        
        return ResponseEntity.status(HttpStatus.OK)
            .body(bookService.approveReturnBook(bookId, connectedUser));
    }

    @PostMapping(value="/cover/{book-id}", consumes="multipart/form-data")
    @Operation(summary="uploadBookCoverPicture", description="Update book cover")
    @ApiResponses({
        @ApiResponse(responseCode="200", description="Update successfully"),
        @ApiResponse(responseCode="403", description="Operation not permitted because not book owner"),
        @ApiResponse(responseCode="404", description="Book not found")
    })
    public ResponseEntity<?> uploadBookCoverPicture(@PathVariable Long bookId,
        @RequestPart("file") MultipartFile file, Authentication connectedUser){
        
        bookService.uploadBookCoverPicture(file, bookId, connectedUser);
        
        return ResponseEntity.ok().build();
    }


}
