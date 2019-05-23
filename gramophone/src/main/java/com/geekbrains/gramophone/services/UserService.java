package com.geekbrains.gramophone.services;

import com.geekbrains.gramophone.entities.SystemUser;
import com.geekbrains.gramophone.entities.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    User findByUsername(String username);
    boolean save(SystemUser systemUser);
}
