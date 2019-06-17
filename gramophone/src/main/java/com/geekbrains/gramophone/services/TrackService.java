package com.geekbrains.gramophone.services;

import com.geekbrains.gramophone.entities.Track;
import com.geekbrains.gramophone.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface TrackService {
    List<Track> findAll();
    boolean save(Track track);
    Page<Track> getTracksWithPagingAndFiltering(int pageNumber, int pageSize, Specification<Track> trackSpecification);
    List<Track> findByTitle(String title);
    List<Track> findByAuthor(String author);
    List<Track> findByGenreTitle(String title);
    List<Track> findByGenreId(Long id);
    Track findTrackById(Long id);
    void changeLike(Long id, User user);
    void setLike(Long id, User user);
    void removeLike(Long id, User user);
    void changeDislike(Long id, User user);
    void setDislike(Long id, User user);
    void removeDislike(Long id, User user);
    void deleteById(Long id);
    Track updateTrack(Long id, Track trackFromForm, String fileName);
    Track buildTrack(Track trackFromForm, User user, String fileName);
    List<Track> findAllSingerUserTracks(User user);
    void deleteTrack(Long id);
}
