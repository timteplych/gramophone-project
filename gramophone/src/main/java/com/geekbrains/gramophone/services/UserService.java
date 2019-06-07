package com.geekbrains.gramophone.services;

import com.geekbrains.gramophone.entities.SystemUser;
import com.geekbrains.gramophone.entities.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

public interface UserService extends UserDetailsService {
    User findByUsername(String username);
    Optional<User> findById(Long id);
    boolean save(SystemUser systemUser);
    void save(User user);

}
