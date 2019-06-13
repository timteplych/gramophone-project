package com.geekbrains.gramophone.services;

import com.geekbrains.gramophone.entities.Playlist;
import com.geekbrains.gramophone.entities.User;

import java.util.List;

public interface PlaylistService {
    void addTrack(User user, Long playlistId, Long trackId);
    void removeTrack(User user, Long playlistId, Long trackId);
    void savePlaylist(Playlist playlist);
    void addPlaylist(User currentUser, String playlistName);
    List<Playlist> findAllPlaylistsByUser(User user);
}
