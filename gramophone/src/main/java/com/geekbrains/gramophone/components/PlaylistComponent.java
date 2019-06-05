package com.geekbrains.gramophone.components;

import com.geekbrains.gramophone.entities.Track;
import com.geekbrains.gramophone.entities.User;
import com.geekbrains.gramophone.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.List;


@Component
public class PlaylistComponent implements Playlist {

    private UserRepository userRepository;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private String name = "default";
    private User user;
    private List<Track> tracks;

    public PlaylistComponent() {
    }

    public PlaylistComponent(User user) {
        this.user = user;
        tracks = user.getPlaylist();
    }

    @Override
    @Transactional
    public void addTrack(Track track){
        tracks.add(track);
        user.getPlaylist().add(track);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void removeTrack(Track track) {
        tracks.remove(track);
        user.getPlaylist().remove(track);
        userRepository.save(user);
    }

    @Override
    public List<Track> getTracks() {
        return tracks;
    }

    @Override
    public String getName() {
        return name;
    }
}
