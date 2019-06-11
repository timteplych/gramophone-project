package com.geekbrains.gramophone.repositories;

import com.geekbrains.gramophone.entities.Playlist;
import com.geekbrains.gramophone.entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaylistRepository extends CrudRepository<Playlist, Long> {

}
