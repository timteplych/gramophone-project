package com.geekbrains.gramophone.services;

import com.geekbrains.gramophone.entities.Comment;
import com.geekbrains.gramophone.entities.Genre;
import com.geekbrains.gramophone.entities.Track;
import com.geekbrains.gramophone.entities.User;
import com.geekbrains.gramophone.repositories.GenreRepository;
import com.geekbrains.gramophone.repositories.TrackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@Service
public class TrackServiceImpl implements TrackService {

    private TrackRepository trackRepository;
    private GenreRepository genreRepository;

    @Autowired
    public void setTrackRepository(TrackRepository trackRepository) {
        this.trackRepository = trackRepository;
    }

    @Autowired
    public void setGenreRepository(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    @Override
    public List<Track> findAll() {
        return (List<Track>) trackRepository.findAll();
    }

    @Override
    public boolean save(Track track) {
        trackRepository.save(track);
        return true;
    }

    public Page<Track> getTracksWithPagingAndFiltering(int pageNumber, int pageSize, Specification<Track> trackSpecification) {
        return trackRepository.findAll(trackSpecification, PageRequest.of(pageNumber, pageSize));
    }

    @Override
    public List<Track> findByTitle(String title) {
        if("".equals(title)){
            return Collections.emptyList();
        }
        return trackRepository.findAllByTitleContaining(title);
    }

    @Override
    public List<Track> findByAuthor(String author) {
        if("".equals(author)){
            return Collections.emptyList();
        }
        return trackRepository.findAllByMusicAuthorContaining(author);
    }

    @Override
    public List<Track> findByGenreTitle(String title) {
        if("".equals(title)){
            return Collections.emptyList();
        }
        Genre genre = genreRepository.findByTitle(title);
        if (genre == null){
            return Collections.emptyList();
        }

        return trackRepository.findAllByGenre(genre);
    }

    @Override
    public List<Track> findByGenreId(Long id) {
        Genre genre = genreRepository.findById(id).orElse(null);
        if(genre == null){
            return Collections.emptyList();
        }
        return trackRepository.findAllByGenre(genre);
    }

    @Override
    public Track findTrackById(Long id) {
        return trackRepository.findById(id).orElse(null);
    }

    public void changeLike(Long id, User user) {
        Track track = findTrackById(id);
        if (track.getLikes().contains(user))
            removeLike(id, user);
        else
            setLike(id, user);
    }

    public void setLike(Long id, User user) {
        Track track = findTrackById(id);
        track.getLikes().add(user);
        trackRepository.save(track);
    }

    public void removeLike(Long id, User user) {
        Track track = findTrackById(id);
        track.getLikes().remove(user);
        trackRepository.save(track);
    }

    @Override
    public List<Track> findAllSingerUserTracks(User user) {
        return trackRepository.findAllByPerformer(user);
    }

    @Override
    @Transactional
    public void deleteTrack(Long id) {
        trackRepository.deleteTrackFromAllPlaylists(id);
        trackRepository.deleteById(id);
        //удалить файл трека с сервера
    }

}
