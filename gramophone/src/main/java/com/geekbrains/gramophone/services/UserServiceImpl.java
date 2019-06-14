package com.geekbrains.gramophone.services;

import com.geekbrains.gramophone.entities.*;
import com.geekbrains.gramophone.repositories.RoleRepository;
import com.geekbrains.gramophone.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private BCryptPasswordEncoder passwordEncoder;

    private PlaylistService playlistService;

    @Autowired
    public void setPlaylistService(PlaylistService playlistService) {
        this.playlistService = playlistService;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setRoleRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Autowired
    public void setPasswordEncoder(BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public User findByUsername(String username) {
        return userRepository.findOneByUsername(username);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    @Transactional
    public boolean save(SystemUser systemUser) {
        if (userRepository.findOneByUsername(systemUser.getUsername()) != null) {
            return false;
        }
        User user = new User();
        user.setUsername(systemUser.getUsername());
        user.setPassword(passwordEncoder.encode(systemUser.getPassword()));
        user.setEmail(systemUser.getEmail());
        user.setRoles(Arrays.asList(roleRepository.findOneByName("ROLE_USER")));
        // todo check username is exists
        userRepository.save(user);
        playlistService.addPlaylist(user, "default");

        return true;
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public List<User> findAll() {
        return (List<User>) userRepository.findAll();
    }

    @Override
    public void subscribeOnUser(User currentUser, Long subscribeOnUserId) {
        User user = userRepository.findById(subscribeOnUserId).get();
        user.getSubscribers().add(currentUser);
        userRepository.save(user);
    }

    @Override
    public void unsubscribeOnUser(User currentUser, Long unsubscribeOnUserId) {
        User user = userRepository.findById(unsubscribeOnUserId).get();
        user.getSubscribers().remove(currentUser);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findOneByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("Invalid username or password");
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                mapRolesToAuthorities(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }

    @Override
    public List<Track> allUserTracksFromPlaylists(Long userId) {
        User user = userRepository.findById(userId).get();
        List<Track> tracks = new ArrayList<>();
        List<Playlist> playlistList = playlistService.findAllPlaylistsByUser(user);

        for (Playlist p : playlistList){
            if(!p.getTracks().isEmpty()){
                tracks.addAll(p.getTracks());
            }
        }

        return tracks;
    }

}
