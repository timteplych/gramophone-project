package com.geekbrains.gramophone.services;

import com.geekbrains.gramophone.entities.Genre;
import com.geekbrains.gramophone.entities.Track;
import com.geekbrains.gramophone.entities.User;
import com.geekbrains.gramophone.repositories.GenreRepository;
import com.geekbrains.gramophone.repositories.TrackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class TrackServiceImpl implements TrackService {

    private TrackRepository trackRepository;

    private GenreRepository genreRepository;

    @Autowired
    public void setTrackService(TrackRepository trackRepository) {
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
        if ("" .equals(title)) {
            return Collections.emptyList();
        }
        return trackRepository.findAllByTitleContaining(title);
    }

    @Override
    public List<Track> findByAuthor(String author) {
        if ("" .equals(author)) {
            return Collections.emptyList();
        }
        return trackRepository.findAllByMusicAuthorContaining(author);
    }

    @Override
    public List<Track> findByGenreTitle(String title) {
        if ("" .equals(title)) {
            return Collections.emptyList();
        }
        Genre genre = genreRepository.findByTitle(title);
        if (genre == null) {
            return Collections.emptyList();
        }

        return trackRepository.findAllByGenre(genre);
    }

    @Override
    public List<Track> findByGenreId(Long id) {
        Genre genre = genreRepository.findById(id).orElse(null);
        if (genre == null) {
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
    public void deleteById(Long id) {
        trackRepository.deleteById(id);
    }

    public Track buildTrack(Track trackFromForm, User user, String fileName) {
        trackFromForm.setPerformer(user);
        trackFromForm.setCreateAt(new Date());
        trackFromForm.setListeningAmount(0L);
        trackFromForm.setGenre(trackFromForm.getGenre());

        trackFromForm.setLocationOnServer("uploads/" + user.getUsername() + "/" + fileName);
        trackFromForm.setDownloadUrl(ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/uploads/")
                .path(user.getUsername() + "/" + fileName)
                .toUriString());


        return trackFromForm;
    }

    @Override
    public List<Track> findAllSingerUserTracks(User user) {
        return trackRepository.findAllByPerformer(user);
    }

    @Override
    @Transactional
    public void deleteTrack(Long id) {
        Track track = trackRepository.findById(id).get();
        deleteTrackFromServer(track.getLocationOnServer());
        trackRepository.deleteTrackFromAllPlaylists(id);
        trackRepository.deleteById(id);
    }

    private void deleteTrackFromServer(String locationOnServer) {
        // НЕ ПОЛУЧАЕТСЯ УДАЛИТЬ
//        try {
//            Files.delete(Paths.get(locationOnServer));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    public Track updateTrack(Long id, Track trackFromForm, String fileName) {
        trackRepository.findById(id).ifPresent(updatedTrack -> {
            updatedTrack.setListeningAmount(trackFromForm.getListeningAmount());
            updatedTrack.setWordAuthor(trackFromForm.getWordAuthor());
            updatedTrack.setMusicAuthor(trackFromForm.getWordAuthor());
            updatedTrack.setTitle(trackFromForm.getTitle());
            updatedTrack.setCover(trackFromForm.getCover());
            updatedTrack.setGenre(trackFromForm.getGenre());

            if (!updatedTrack.getLocationOnServer().equals(trackFromForm.getLocationOnServer())) {
                trackFromForm.setLocationOnServer("uploads/" + trackFromForm.getPerformer().getUsername() + "/" + fileName);
                updatedTrack.setLocationOnServer(trackFromForm.getLocationOnServer());
                trackFromForm.setDownloadUrl(ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path("/uploads/")
                        .path(trackFromForm.getPerformer().getUsername() + "/" + fileName)
                        .toUriString());
                updatedTrack.setDownloadUrl(trackFromForm.getDownloadUrl());
            }
        });
        return trackFromForm;
    }
}
