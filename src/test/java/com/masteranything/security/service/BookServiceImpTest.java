/*
 * Unit tests for BookServiceTest
 */

 package com.masteranything.security.service;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;

import com.masteranything.security.dao.Book;
import com.masteranything.security.dao.User;
import com.masteranything.security.dto.BookRequest;
import com.masteranything.security.dto.BookResponse;
import com.masteranything.security.exception.GeneralException;
import com.masteranything.security.repository.BookRepository;
import com.masteranything.security.repository.BookTransactionHistoryRepository;

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
    void whenSaveWithValidRequest_ThenReturnsBookId() {
        when(connectedUser.getPrincipal()).thenReturn(user);
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        Long savedBookId = bookService.save(bookRequest, connectedUser);

        assertEquals(book.getId(), savedBookId);
        verify(bookRepository).save(any(Book.class));
    }

    @Test
    void whenFindByIdWithExistingBook_ThenReturnsBookResponse(){

        // prepare
        Long bookId = 1L;
        when(bookRepository.findById(any(Long.class))).thenReturn(Optional.of(book));
        
        // test
        BookResponse bookResponse = bookService.findById(bookId);

        // verify
        assertNotNull(bookResponse);
        assertEquals(book.getId(), bookResponse.id());
        verify(bookRepository).findById(bookId);

    }

    @Test
    void whenFindByIdWithNoExistingBook_ThenThrowsGeneralException(){

        // prepare
        Long bookId = 2L;
        when(bookRepository.findById(any(Long.class))).thenReturn(Optional.empty());
        
        // test
        GeneralException bookNotFoundException = assertThrows(GeneralException.class,
                    () -> bookService.findById(bookId));

        // verify
        assertEquals("No Book found. Id: 2", bookNotFoundException.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, bookNotFoundException.getStatus());
        verify(bookRepository).findById(bookId);

    }

}