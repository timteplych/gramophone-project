package com.geekbrains.gramophone.services;

import com.geekbrains.gramophone.entities.SystemUser;
import com.geekbrains.gramophone.entities.Track;
import com.geekbrains.gramophone.entities.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    User findByUsername(String username);
    User findById(Long id);
    User save(SystemUser systemUser);
    void save(User user);
    List<User> findAll();
    User findByEmail(String email, String password);
    void subscribeOnUser(User currentUser, Long subscribeUserId);
    void unsubscribeOnUser(User currentUser, Long subscribeUserId);
    List<Track> allUserTracksFromPlaylists(Long userId);
    boolean activateUser(String code);
    void changeAvatar(User currentUser, String originalFilename);
}
