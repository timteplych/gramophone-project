package com.geekbrains.gramophone.services;

import com.geekbrains.gramophone.entities.Track;
import com.geekbrains.gramophone.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

public interface TrackService {
    List<Track> findAll();
    boolean save(Track track);
    Page<Track> getTracksWithPagingAndFiltering(int pageNumber, int pageSize, Specification<Track> trackSpecification);
    List<Track> findByTitle(String title);
    List<Track> findByAuthor(String author);
    List<Track> findByGenreTitle(String title);
    List<Track> findByGenreId(Long id);
    Page<Track> getTracksWithPaging(int pageNumber, int pageSize);
    Track findTrackById(Long id);
    void changeLike(Long id, Long userId);
    void changeDislike(Long id, Long userId);
    void deleteById(Long id);
    Track updateTrack(Long id, String title, String wordAuthor, String musicAuthor, String genreId, String fileName);
    Track buildTrack(String title, String wordAuthor, String musicAuthor, String genreId, String performerId, MultipartFile file);
    List<Track> findAllSingerUserTracks(User user);
    void deleteTrack(Long id);
    boolean isThere(Track track, String searchStr);
}
