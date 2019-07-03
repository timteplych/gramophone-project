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
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private BCryptPasswordEncoder passwordEncoder;
    private PlaylistService playlistService;
    private MailSenderService mailSenderService;

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

    @Autowired
    public void setMailSenderService(MailSenderService mailSenderService) {
        this.mailSenderService = mailSenderService;
    }

    @Override
    @Transactional
    public User findByUsername(String username) {
        return userRepository.findOneByUsername(username);
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public User save(SystemUser systemUser) {
        if (userRepository.findOneByUsername(systemUser.getUsername()) != null) {
            return null;
        }
        User user = new User();
        user.setUsername(systemUser.getUsername());
        user.setPassword(passwordEncoder.encode(systemUser.getPassword()));
        user.setEmail(systemUser.getEmail());
        user.setRoles(Collections.singletonList(roleRepository.findOneByName("ROLE_USER")));
        user.setActivationCode(UUID.randomUUID().toString());
        User savedUser = userRepository.save(user);
        playlistService.addPlaylist(user, "default");
        sendActivationCode(user);

        return savedUser;
    }

    private void sendActivationCode(User user) {
        if (!StringUtils.isEmpty(user.getEmail())) {
            String message = String.format(
                    "Привет, %s! \n" +
                            "Рады видеть вас на нашей музыкальной площадке Gramophone! \n" +
                            "Пожалуйста перейдите по ссылке \nhttp://localhost:4200/api/v1/users/activate/%s\n" +
                            "для подтверждения вашего почтового ящика.",
                    user.getUsername(), user.getActivationCode()
            );
            mailSenderService.send(user.getEmail(), "Activation code", message);
        }
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
    public User findByEmail(String email, String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        User user = userRepository.findByEmail(email);
        if (user != null && encoder.matches(password, user.getPassword())) {
            return user;
        }
        return null;
    }

    @Override
    public void subscribeOnUser(User currentUser, Long subscribeOnUserId) {
        User user = userRepository.findById(subscribeOnUserId).orElse(null);
        assert user != null;
        user.getSubscribers().add(currentUser);
        userRepository.save(user);
    }

    @Override
    public void unsubscribeOnUser(User currentUser, Long unsubscribeOnUserId) {
        User user = userRepository.findById(unsubscribeOnUserId).orElse(null);
        assert user != null;
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

        for (Playlist p : playlistList) {
            if (!p.getTracks().isEmpty()) {
                tracks.addAll(p.getTracks());
            }
        }

        return tracks;
    }

    @Override
    public boolean activateUser(String code) {
        User user = userRepository.findByActivationCode(code);
        if(user == null){
            return false;
        }
        user.setActivationCode(null);
        userRepository.save(user);

        return true;
    }

    @Override
    public void changeAvatar(User currentUser, String filename) {
        currentUser.setAvatar("images/" + currentUser.getUsername() + "/" + filename);
        userRepository.save(currentUser);
    }
}
