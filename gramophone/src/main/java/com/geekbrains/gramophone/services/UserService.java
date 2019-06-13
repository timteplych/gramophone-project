package com.geekbrains.gramophone.services;

import com.geekbrains.gramophone.entities.SystemUser;
import com.geekbrains.gramophone.entities.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    User findByUsername(String username);
    User findById(Long id);
    boolean save(SystemUser systemUser);
    void save(User user);
    List<User> findAll();

    void subscribeOnUser(User currentUser, Long subscribeUserId);
    void unsubscribeOnUser(User currentUser, Long subscribeUserId);

}
