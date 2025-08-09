package com.masteranything.security.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.masteranything.security.dao.BookTransactionHistory;

public interface BookTransactionHistoryRepository extends JpaRepository<BookTransactionHistory, Long>{
    @Query("""
             SELECT history
             FROM BookTransactionHistory
             where history.owner.id = :userId
            """)
    Page<BookTransactionHistory> findAllBorrowedBooks(Pageable pageable, Long userId);
}
