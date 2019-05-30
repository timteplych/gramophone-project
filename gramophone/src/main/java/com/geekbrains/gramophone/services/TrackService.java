package com.geekbrains.gramophone.services;

import com.geekbrains.gramophone.entities.Track;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface TrackService {
    List<Track> findAll();
    boolean save(Track track);
}
