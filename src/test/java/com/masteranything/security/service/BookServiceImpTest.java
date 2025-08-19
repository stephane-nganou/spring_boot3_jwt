/*
 * Unit tests for BookServiceTest
 */

 package com.masteranything.security.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import com.masteranything.security.dao.Book;
import com.masteranything.security.dao.BookTransactionHistory;
import com.masteranything.security.dao.User;
import com.masteranything.security.dto.BookRequest;
import com.masteranything.security.repository.BookRepository;
import com.masteranything.security.repository.BookTransactionHistoryRepository;
import com.masteranything.security.util.BookUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

/**
 *
 * @author StephaneWafo
 */
@ExtendWith(MockitoExtension.class)
class BookServiceImpTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookTransactionHistoryRepository transactionHistoryRepository;

    @Mock
    private FileStorageService fileStorageService;

    @Mock
    private Authentication connectedUser;

    @InjectMocks
    private BookServiceImp bookService;

    private User user;
    private Book book;
    private BookRequest bookRequest;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        book = new Book();
        book.setId(1L);
        book.setOwner(user);
        book.setShareable(true);
        book.setArchived(false);
        bookRequest = BookRequest.builder()
                        .title("Test Book")
                        .authorName("Test Author")
                        .isbn("1234567890")
                        .synopsis("Test Synopsis")
                        .shareable(true)
                        .build();

        
    }


    @Test
    void save_ValidRequest_ReturnsBookId() {
        when(connectedUser.getPrincipal()).thenReturn(user);
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        Long savedBookId = bookService.save(bookRequest, connectedUser);

        assertEquals(book.getId(), savedBookId);
        verify(bookRepository).save(any(Book.class));
    }

}