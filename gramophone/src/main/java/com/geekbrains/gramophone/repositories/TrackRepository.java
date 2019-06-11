package com.geekbrains.gramophone.repositories;

import com.geekbrains.gramophone.entities.Genre;
import com.geekbrains.gramophone.entities.Track;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrackRepository extends PagingAndSortingRepository<Track, Long>, JpaSpecificationExecutor<Track> {
    List<Track> findAllByTitleContaining(String title);

    List<Track> findAllByMusicAuthorContaining(String author);

    List<Track> findAllByGenre(Genre genre);

}
