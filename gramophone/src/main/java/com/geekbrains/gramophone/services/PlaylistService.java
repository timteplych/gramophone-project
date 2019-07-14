package com.geekbrains.gramophone.services;

import com.geekbrains.gramophone.entities.Playlist;
import com.geekbrains.gramophone.entities.User;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PlaylistService {
    void addTrack(User user, Long playlistId, Long trackId);
    void removeTrack(User user, Long playlistId, Long trackId);
    Playlist findPlaylistById(Long id);
    List<Playlist> findAllPlaylistsByUser(User user);
    Page<Playlist> getPlaylistsWithPaging(int pageNumber, int pageSize);
    boolean addPlaylist(User currentUser, String playlistName);
    void removePlaylist(Long playlistId);
    Playlist buildPlaylist(String name, String userId);
    Playlist updatePlaylist(Long id, String name, String userId);
}
