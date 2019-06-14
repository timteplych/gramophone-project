package com.geekbrains.gramophone.services;

import com.geekbrains.gramophone.entities.Playlist;
import com.geekbrains.gramophone.entities.User;

import java.util.List;

public interface PlaylistService {
    void addTrack(User user, Long playlistId, Long trackId);
    void removeTrack(User user, Long playlistId, Long trackId);
    List<Playlist> findAllPlaylistsByUser(User user);
    boolean addPlaylist(User currentUser, String playlistName);
    void removePlaylist(Long playlistId);
}
