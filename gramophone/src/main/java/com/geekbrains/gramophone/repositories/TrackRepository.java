package com.geekbrains.gramophone.repositories;

import com.geekbrains.gramophone.entities.Genre;
import com.geekbrains.gramophone.entities.Track;
import com.geekbrains.gramophone.entities.User;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrackRepository extends PagingAndSortingRepository<Track, Long>, JpaSpecificationExecutor<Track> {
    List<Track> findAllByTitleContainingOrMusicAuthorContaining(String search, String author);
    List<Track> findAllByTitleContaining(String search);
    List<Track> findAllByMusicAuthorContaining(String author);
    List<Track> findAllByGenre(Genre genre);
    List<Track> findAllByPerformer(User user);
    List<Track> findAllByTitleContainingOrMusicAuthorContainingAndGenre(String search, String author, Genre genre);

    @Query(value = "select count(*) from tracks_likes tl where track_id = ?1 and user_id = ?2", nativeQuery = true)
    int trackLikedBy(Long trackId, Long userId);

    @Query(value = "select count(*) from tracks_dislikes tl where track_id = ?1 and user_id = ?2", nativeQuery = true)
    int trackDislikedBy(Long trackId, Long userId);

    @Modifying
    @Query(value = "DELETE FROM playlist_tracks WHERE track_id = :id", nativeQuery = true)
    void deleteTrackFromAllPlaylists(@Param("id") Long id);
}
