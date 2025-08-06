package com.masteranything.security.repository;

import com.masteranything.security.dao.Role;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.masteranything.security.dao.Book;

public interface BookRepository extends JpaRepository<Book, Long> {

  
}
