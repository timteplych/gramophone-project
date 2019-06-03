package com.geekbrains.gramophone.repositories;

import com.geekbrains.gramophone.entities.Track;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrackRepository extends PagingAndSortingRepository<Track, Long>, JpaSpecificationExecutor<Track> {
}
