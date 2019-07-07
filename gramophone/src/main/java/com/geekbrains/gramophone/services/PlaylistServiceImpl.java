package com.geekbrains.gramophone.services;

import com.geekbrains.gramophone.entities.Playlist;
import com.geekbrains.gramophone.entities.Track;
import com.geekbrains.gramophone.entities.User;
import com.geekbrains.gramophone.repositories.PlaylistRepository;
import com.geekbrains.gramophone.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    public Playlist findPlaylistById(Long id) {
        return playlistRepository.findById(id).orElse(null);
    }

    @Override
    public List<Playlist> findAllPlaylistsByUser(User user) {
        return playlistRepository.findAllByUser(user);
    }

    @Override
    public Page<Playlist> getPlaylistsWithPaging(int pageNumber, int pageSize) {
        return playlistRepository.findAll(PageRequest.of(pageNumber, pageSize));
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

    @Override
    public Playlist buildPlaylist(String name, String userId) {

        User user = userRepository.findById(Long.parseLong(userId)).orElse(null);

        Playlist newPlaylist = new Playlist();
        newPlaylist.setName(name);
        newPlaylist.setUser(user);
        return newPlaylist;
    }

    @Override
    public Playlist updatePlaylist(Long id, String name, String userId) {
        Playlist updatePlaylist = playlistRepository.findById(id).orElse(null);
        if (updatePlaylist != null) {
            updatePlaylist.setName(name);
            updatePlaylist.setUser(userRepository.findById(Long.valueOf(userId)).orElse(null));
        }
        return updatePlaylist;
    }

}
