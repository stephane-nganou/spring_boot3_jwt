package com.masteranything.security.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.masteranything.security.dao.Feedback;

public interface FeedbackRepository extends  JpaRepository<Feedback, Long>{

    @Query("""
            SELECT feedback
            FROM Feedback feedback
            WHERE feedback.book.id = :bookId
            """)
    Page<Feedback> findAllByBookId(Pageable pageable, Long bookId);

}
