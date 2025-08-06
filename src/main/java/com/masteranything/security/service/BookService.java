package com.masteranything.security.service;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.masteranything.security.dao.Book;
import com.masteranything.security.dao.User;
import com.masteranything.security.dto.BookRequest;
import com.masteranything.security.repository.BookRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    public Long save(BookRequest request, Authentication connectedUser){
        
        var user = (User)connectedUser.getPrincipal();
        var book = tBook(request);
        book.setOwner(user);

        return bookRepository.save(book).getId();
    }


    private Book tBook(BookRequest request){

        return Book.builder()
            .id(request.id())
            .title(request.title())
            .authorName(request.authorName())
            .synopsis(request.synopsis())
            .archived(false)
            .shareable(request.shareable())
            .build();
    }
}
