package com.masteranything.security.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.masteranything.security.dto.BookRequest;
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
}
