package com.geekbrains.gramophone.repositories;

import com.geekbrains.gramophone.entities.Playlist;
import com.geekbrains.gramophone.entities.User;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaylistRepository extends PagingAndSortingRepository<Playlist, Long>, JpaSpecificationExecutor<Playlist> {

    List<Playlist> findAllByUser(User user);
//    List<Playlist> findAllByTitleContainingUser(String search, User user);

}
