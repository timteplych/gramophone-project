package com.geekbrains.gramophone.services;

import com.geekbrains.gramophone.entities.SystemUser;
import com.geekbrains.gramophone.entities.Track;
import com.geekbrains.gramophone.entities.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Optional;

public interface UserService extends UserDetailsService {
    User findByUsername(String username);
    Optional<User> findById(Long id);
    boolean save(SystemUser systemUser);
    void save(User user);
    List<User> findAll();
    void subscribeOnUser(User currentUser, Long subscribeUserId);
    void unsubscribeOnUser(User currentUser, Long subscribeUserId);

    List<Track> allUserTracksFromPlaylists(Long userId);
}
