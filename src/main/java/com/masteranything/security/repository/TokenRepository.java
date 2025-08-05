package com.masteranything.security.repository;

import com.masteranything.security.dao.Role;
import com.masteranything.security.dao.Token;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<Token, Long> {

  Optional<Token> findByToken(String token);
}
