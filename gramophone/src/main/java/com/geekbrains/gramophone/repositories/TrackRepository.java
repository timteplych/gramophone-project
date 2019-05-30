package com.geekbrains.gramophone.repositories;

import com.geekbrains.gramophone.entities.Track;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrackRepository extends CrudRepository<Track, Long> {
}
