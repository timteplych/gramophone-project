package com.geekbrains.gramophone.services;

import com.geekbrains.gramophone.entities.Playlist;
import com.geekbrains.gramophone.entities.Track;
import com.geekbrains.gramophone.entities.User;

public interface PlaylistService {

    void addTrack(User user,  Track track);
    void removeTrack(User user,  Track track);
    void savePlaylist(Playlist playlist);
}
