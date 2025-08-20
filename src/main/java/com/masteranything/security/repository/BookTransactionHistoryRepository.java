package com.masteranything.security.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.masteranything.security.dao.BookTransactionHistory;

public interface BookTransactionHistoryRepository extends JpaRepository<BookTransactionHistory, Long>{
    @Query("""
             SELECT history
             FROM BookTransactionHistory history
             where history.user.id = :userId
            """)
    Page<BookTransactionHistory> findAllBorrowedBooks(Pageable pageable, Long userId);

    @Query("""
             SELECT history
             FROM BookTransactionHistory history
             where history.user.id = :userId
             AND history.returned = true
             AND history.returnApproved = true
            """)
    Page<BookTransactionHistory> findAllReturnedBooks(Pageable pageable, Long userId);

    @Query("""
            SELECT
            (COUNT (*) > 0) AS isBorrowed
            FROM BookTransactionHistory history
            WHERE history.book.id = :bookId
            AND history.returnApproved = false
            """)
    boolean isAlreadyBorrowedByUser(Long bookId);

    @Query("""
            SELECT history
            FROM BookTransactionHistory history
            WHERE history.book.id = :bookId
            AND history.user.id = :userId
            AND history.returnApproved = false
            AND history.returned = false
            """)
    Optional<BookTransactionHistory> findByBookIdAndUserId(Long bookId, Long userId);

    @Query("""
            SELECT history
            FROM BookTransactionHistory history
            WHERE history.book.owner.id = :ownerId
            AND history.book.id = :bookId
            AND history.returnApproved = false
            AND history.returned = true
            """)
    Optional<BookTransactionHistory> findByBookIdAndOwnerId(Long bookId, Long ownerId);
}
