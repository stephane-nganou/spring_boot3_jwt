/*
 * Unit tests for BookServiceTest
 */

 package com.masteranything.security.service;

import java.util.List;
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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;

import com.masteranything.security.dao.Book;
import com.masteranything.security.dao.BookTransactionHistory;
import com.masteranything.security.dao.User;
import com.masteranything.security.dto.BookRequest;
import com.masteranything.security.dto.BookResponse;
import com.masteranything.security.dto.BorrowedBookResponse;
import com.masteranything.security.dto.PageResponse;
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
        assertEquals("No Book found with ID: " + bookId, bookNotFoundException.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, bookNotFoundException.getStatus());
        verify(bookRepository).findById(bookId);

    }

    @Test
    void whenFindAllBookWithValidPageRequest_ThenReturnValidPageResponse(){

        // prepare
        Page<Book> page = new PageImpl<>(List.of(book));
        when(connectedUser.getPrincipal()).thenReturn(user);
        when(bookRepository.findAllDisplayableBooks(any(Pageable.class), any(Long.class)))
            .thenReturn(page);

        // test
        PageResponse<BookResponse> response = bookService.findAllBooks(0, 10, connectedUser);

        // verify
        assertNotNull(response);
        assertEquals(0, response.number());
        assertEquals(1, response.size());
        assertEquals(1L, response.totalElements());
        assertEquals(1L, response.totalPages());
        assertEquals(true, response.first());
        assertEquals(true, response.last());
        assertEquals(true, response.last());
        assertEquals(1, response.content().size());
        assertEquals(book.getId(), response.content().getFirst().id());
        verify(bookRepository, times(1))
                .findAllDisplayableBooks(any(Pageable.class), any(Long.class));
    }

    @SuppressWarnings("unchecked")
    @Test
    void whenFindAllBooksByOwnerWithValidPageRequest_ThenReturnValidPageResponse(){

        // prepare
        Page<Book> page = new PageImpl<>(List.of(book));
        when(connectedUser.getPrincipal()).thenReturn(user);
        when(bookRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);

        // test
        PageResponse<BookResponse> response = bookService.findAllBooksByOwner(0, 10, connectedUser);

        // verify
        assertNotNull(response);
        verify(bookRepository, times(1))
                .findAll(any(Specification.class), any(Pageable.class));
        assertEquals(1, response.content().size());
        assertEquals(1L, response.totalElements());
        assertEquals(1L, response.totalPages());
    }

    @SuppressWarnings("unchecked")
    @Test
    void whenFindAllBooksByOwnerWithValidPageRequestAndNoOwnBook_ThenReturnEmptyValidPageResponse(){

        // prepare
        Page<Book> page = new PageImpl<>(List.of());
        when(connectedUser.getPrincipal()).thenReturn(user);
        when(bookRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);

        // test
        PageResponse<BookResponse> response = bookService.findAllBooksByOwner(0, 10, connectedUser);

        // verify
        assertNotNull(response);
        verify(bookRepository, times(1))
                .findAll(any(Specification.class), any(Pageable.class));
        assertEquals(0, response.content().size());
        assertEquals(0L, response.totalElements());
        assertEquals(1L, response.totalPages());
    }

    @Test
    void WhenFindAllBorrowedBooksWithValidPageRequest_ThenReturnsPageResponse(){
        // prepare
        BookTransactionHistory transactionHistory = BookTransactionHistory.builder()
                                                        .book(book)
                                                        .user(user)
                                                        .returned(false)
                                                        .returnApproved(false)
                                                        .build();

        Page<BookTransactionHistory> page = new PageImpl<>(List.of(transactionHistory));
        when(connectedUser.getPrincipal()).thenReturn(user);
        when(transactionHistoryRepository.findAllBorrowedBooks(any(Pageable.class), any(Long.class))).thenReturn(page);

        // test
        PageResponse<BorrowedBookResponse> response = bookService.findAllBorrowedBooks(0, 10, connectedUser);

        // verify
        assertNotNull(response);
        verify(transactionHistoryRepository,
            times(1)).findAllBorrowedBooks(
                any(Pageable.class),
                any(Long.class)
            );
        assertEquals(1, response.content().size());
        assertEquals(1L, response.totalElements());
        assertEquals(1L, response.totalPages());
    }

    @Test
    void whenFindAllReturnedBooksWithValidPageRequest_ThenReturnsPageResponse(){
        
        // prepare
        BookTransactionHistory transactionHistory = BookTransactionHistory.builder()
                                                        .book(book)
                                                        .user(user)
                                                        .returned(true)
                                                        .returnApproved(true)
                                                        .build();

        Page<BookTransactionHistory> page = new PageImpl<>(List.of(transactionHistory));
        when(connectedUser.getPrincipal()).thenReturn(user);
        when(transactionHistoryRepository.findAllReturnedBooks(any(Pageable.class), any(Long.class))).thenReturn(page);

        // test
        PageResponse<BorrowedBookResponse> response = bookService.findAllReturnedBooks(0, 10, connectedUser);

        // verify
        assertNotNull(response);
        verify(transactionHistoryRepository,
            times(1)).findAllReturnedBooks(
                any(Pageable.class),
                any(Long.class)
            );
        assertEquals(1, response.content().size());
        assertEquals(1L, response.totalElements());
        assertEquals(1L, response.totalPages());
        assertEquals(true, response.content().getFirst().returnApproval());
        assertEquals(true, response.content().getFirst().returned());
    }

    @Test
    void whenUpdateShareableStatusWithExistingBookAndOwner_ThenReturnBookId(){
        
        // prepare
        boolean oldStatus = book.isShareable();
        when(bookRepository.findById(any(Long.class))).thenReturn(Optional.of(book));
        when(connectedUser.getPrincipal()).thenReturn(user);

        // test
        Long updatedBookId = bookService.updateShareableStatus(book.getId(), connectedUser);

        // verify
        verify(bookRepository, times(1)).save(any(Book.class));
        assertEquals(book.getId(), updatedBookId);
        assertEquals(!oldStatus, book.isShareable());
    }

    @Test
    void whenUpdateShareableStatusWithExistingBookAndNotOwner_ThenThrowsGeneralException(){
        
        // prepare
        User user_2 = User.builder()
                        .id(2L)
                        .build();
        when(bookRepository.findById(any(Long.class))).thenReturn(Optional.of(book));
        when(connectedUser.getPrincipal()).thenReturn(user_2);

        // test
        GeneralException updateBookStatusNotPermit = assertThrows(GeneralException.class,
                    () -> bookService.updateShareableStatus(book.getId(), connectedUser));


        // verify
        assertEquals("Operation Not Permitted", updateBookStatusNotPermit.getMessage());
        assertEquals(HttpStatus.FORBIDDEN, updateBookStatusNotPermit.getStatus());
        verify(bookRepository).findById(any(Long.class));
    }

    @Test
    void whenUpdateShareableStatusWithNonExistingBook_ThenThrowsGeneralException(){
        
        // prepare
        when(bookRepository.findById(any(Long.class))).thenReturn(Optional.empty());
        
        // test
        GeneralException bookNotFoundException = assertThrows(GeneralException.class,
                    () -> bookService.updateShareableStatus(book.getId(), connectedUser));

        // verify
        assertEquals("No Book found with ID: " + book.getId(), bookNotFoundException.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, bookNotFoundException.getStatus());
        verify(bookRepository).findById(any(Long.class));

    }

    @Test
    void whenUpdateArchivedStatusStatusWithExistingBookAndOwner_ThenReturnBookId(){
        
        // prepare
        boolean oldArchivedStatus = book.isArchived();
        when(bookRepository.findById(any(Long.class))).thenReturn(Optional.of(book));
        when(connectedUser.getPrincipal()).thenReturn(user);

        // test
        Long updatedBookId = bookService.updateArchivedStatus(book.getId(), connectedUser);

        // verify
        verify(bookRepository, times(1)).save(any(Book.class));
        assertEquals(book.getId(), updatedBookId);
        assertEquals(!oldArchivedStatus, book.isArchived());
    }

    @Test
    void whenUpdateArchivedStatusWithExistingBookAndNotOwner_ThenThrowsGeneralException(){
        // prepare
        User user_2 = User.builder()
                        .id(2L)
                        .build();
        when(bookRepository.findById(any(Long.class))).thenReturn(Optional.of(book));
        when(connectedUser.getPrincipal()).thenReturn(user_2);

        // test
        GeneralException updateBookStatusNotPermit = assertThrows(GeneralException.class,
                    () -> bookService.updateArchivedStatus(book.getId(), connectedUser));


        // verify
        assertEquals("Operation Not Permitted", updateBookStatusNotPermit.getMessage());
        assertEquals(HttpStatus.FORBIDDEN, updateBookStatusNotPermit.getStatus());
        verify(bookRepository).findById(any(Long.class));
    }

    @Test
    void whenUpdateArchivedStatusWithNonExistingBook_ThenThrowsGeneralException(){
        
        // prepare
        when(bookRepository.findById(any(Long.class))).thenReturn(Optional.empty());
        
        // test
        GeneralException bookNotFoundException = assertThrows(GeneralException.class,
                    () -> bookService.updateArchivedStatus(book.getId(), connectedUser));

        // verify
        assertEquals("No Book found with ID: " + book.getId(), bookNotFoundException.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, bookNotFoundException.getStatus());
        verify(bookRepository).findById(any(Long.class));

    }

    @Test
    void whenBorrowBook_WithValidBookAndNotOwnBook_ThenReturnsTransactionId(){
        
        // prepare
        User user_2 = User.builder()
                        .id(2L)
                        .build();
        BookTransactionHistory transactionHistory = BookTransactionHistory.builder()
                                                        .id(1L)
                                                        .book(book)
                                                        .user(user_2)
                                                        .returned(false)
                                                        .returnApproved(false)
                                                        .build();
        when(bookRepository.findById(any(Long.class))).thenReturn(Optional.of(book));
        when(connectedUser.getPrincipal()).thenReturn(user_2);
        when(transactionHistoryRepository.isAlreadyBorrowedByUser(any(Long.class))).thenReturn(false);
        when(transactionHistoryRepository.save(any(BookTransactionHistory.class))).thenReturn(transactionHistory);

        // test
        Long transactionId = bookService.borrowBook(book.getId(), connectedUser);

        // verify
        assertEquals(transactionId, transactionHistory.getId());
        verify(transactionHistoryRepository).save(any(BookTransactionHistory.class));
    }

    @Test
    void whenBorrowBook_WithValidBookAndOwnBook_ThenThrowsGeneralException(){
        
        // prepare
        when(bookRepository.findById(any(Long.class))).thenReturn(Optional.of(book));
        when(connectedUser.getPrincipal()).thenReturn(user);

        // test
        GeneralException bookNotFoundException = assertThrows(GeneralException.class,
                    () -> bookService.borrowBook(book.getId(), connectedUser));

        
        // verify
        assertEquals("Can not update own book", bookNotFoundException.getMessage());
        assertEquals(HttpStatus.NOT_ACCEPTABLE, bookNotFoundException.getStatus());
        verify(bookRepository).findById(any(Long.class));
    }

    @Test
    void whenBorrowBook_WithArchivedBookAndNotOwnBook_ThenThrowsGeneralException(){
        
        // prepare
        Book archivedBook = Book.builder()
                            .id(1L)
                            .owner(user)
                            .shareable(true)
                            .archived(true)
                            .build();
        when(bookRepository.findById(any(Long.class))).thenReturn(Optional.of(archivedBook));
        when(connectedUser.getPrincipal()).thenReturn(user);

        // test
        GeneralException bookNotFoundException = assertThrows(GeneralException.class,
                    () -> bookService.borrowBook(book.getId(), connectedUser));

        
        // verify
        assertEquals("Book unavailable", bookNotFoundException.getMessage());
        assertEquals(HttpStatus.NOT_ACCEPTABLE, bookNotFoundException.getStatus());
        verify(bookRepository).findById(any(Long.class));
    }

    @Test
    void whenBorrowBook_WithAlreadyBorrowedBookAndNotOwnBook_ThenThrowsGeneralException(){
        
        // prepare
        User user_2 = User.builder()
                        .id(2L)
                        .build();
        Book archivedBook = Book.builder()
                            .id(1L)
                            .owner(user_2)
                            .shareable(false)
                            .archived(false)
                            .build();
        when(bookRepository.findById(any(Long.class))).thenReturn(Optional.of(archivedBook));
        when(connectedUser.getPrincipal()).thenReturn(user);

        // test
        GeneralException bookNotFoundException = assertThrows(GeneralException.class,
                    () -> bookService.borrowBook(book.getId(), connectedUser));

        
        // verify
        assertEquals("Book unavailable", bookNotFoundException.getMessage());
        assertEquals(HttpStatus.NOT_ACCEPTABLE, bookNotFoundException.getStatus());
        verify(bookRepository).findById(any(Long.class));
    }
        
    

}