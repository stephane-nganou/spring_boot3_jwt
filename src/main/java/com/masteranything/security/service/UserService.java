package com.masteranything.security.service;

import com.masteranything.security.dao.User;
import com.masteranything.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;

  public User findUserByEmail(String email) {
    return this.userRepository.findByEmail(email)
        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
  }

  public User saveUser(User user) {
    return this.userRepository.save(user);
  }
}
