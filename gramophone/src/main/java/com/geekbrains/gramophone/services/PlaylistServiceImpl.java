package com.geekbrains.gramophone.services;

import com.geekbrains.gramophone.entities.Playlist;
import com.geekbrains.gramophone.entities.Track;
import com.geekbrains.gramophone.entities.User;
import com.geekbrains.gramophone.repositories.PlaylistRepository;
import com.geekbrains.gramophone.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;


@Service
public class PlaylistServiceImpl implements PlaylistService {

    private PlaylistRepository playlistRepository;
    private UserRepository userRepository;
    private TrackService trackService;

    @Autowired
    public void setPlaylistRepository(PlaylistRepository playlistRepository) {
        this.playlistRepository = playlistRepository;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setTrackService(TrackService trackService) {
        this.trackService = trackService;
    }

    @Override
    @Transactional
    public void addTrack(User user, Long playlistId, Long trackId) {
        Track track = trackService.findTrackById(trackId);
        Playlist playlist = playlistRepository.findById(playlistId).get();
        if (playlist.getTracks().contains(track)) {
            return;
        }
        playlist.getTracks().add(track);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void removeTrack(User user, Long playlistId, Long trackId) {
        Track track = trackService.findTrackById(trackId);
        Playlist playlist = playlistRepository.findById(playlistId).get();
        playlist.getTracks().remove(track);
        userRepository.save(user);
    }

    @Override
    public List<Playlist> findAllPlaylistsByUser(User user) {
        return playlistRepository.findAllByUser(user);
    }

    @Override
    public boolean addPlaylist(User currentUser, String playlistName) {
        for (Playlist p : playlistRepository.findAllByUser(currentUser)) {
            if (playlistName.equals(p.getName())) {
                return false;
            }
        }
        Playlist playlist = new Playlist();
        playlist.setUser(currentUser);
        playlist.setName(playlistName);
        playlistRepository.save(playlist);

        return true;
    }

    @Override
    public void removePlaylist(Long playlistId) {
        playlistRepository.deleteById(playlistId);
    }

}
