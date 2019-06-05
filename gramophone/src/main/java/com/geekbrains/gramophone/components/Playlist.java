package com.geekbrains.gramophone.components;

import com.geekbrains.gramophone.entities.Track;

import java.util.List;

public interface Playlist {
    void addTrack(Track track);
    void removeTrack(Track track);
    List<Track> getTracks();
    String getName();
}
