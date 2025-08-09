package com.masteranything.security.service;

import com.masteranything.security.dao.User;

public interface UserService {

    User findUserByEmail(String email);

    User saveUser(User user);
}
