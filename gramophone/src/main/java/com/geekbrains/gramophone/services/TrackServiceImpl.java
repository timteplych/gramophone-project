package com.geekbrains.gramophone.services;

import com.geekbrains.gramophone.entities.Genre;
import com.geekbrains.gramophone.entities.Track;
import com.geekbrains.gramophone.entities.User;
import com.geekbrains.gramophone.repositories.GenreRepository;
import com.geekbrains.gramophone.repositories.TrackRepository;
import com.geekbrains.gramophone.repositories.UserRepository;
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

    private UserRepository userRepository;

    @Autowired
    public void setTrackService(TrackRepository trackRepository) {
        this.trackRepository = trackRepository;
    }


    @Autowired
    public void setGenreRepository(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }


    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
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
        if ("".equals(title)) {
            return Collections.emptyList();
        }
        return trackRepository.findAllByTitleContaining(title);
    }

    @Override
    public List<Track> findByAuthor(String author) {
        if ("".equals(author)) {
            return Collections.emptyList();
        }
        return trackRepository.findAllByMusicAuthorContaining(author);
    }

    @Override
    public List<Track> findByGenreTitle(String title) {
        if ("".equals(title)) {
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
        if (trackRepository.trackLikedBy(track.getId(), user.getId()) > 0) //(track.getLikes().contains(user))
            removeLike(id, user);
        else {
            setLike(id, user);
            removeDislike(id, user);
        }
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

    public void changeDislike(Long id, User user) {
        Track track = findTrackById(id);
        if (trackRepository.trackDislikedBy(track.getId(), user.getId()) > 0) //(track.getLikes().contains(user))
            removeDislike(id, user);
        else {
            setDislike(id, user);
            removeLike(id, user);
        }
    }

    public void setDislike(Long id, User user) {
        Track track = findTrackById(id);
        track.getDislikes().add(user);
        trackRepository.save(track);
    }

    public void removeDislike(Long id, User user) {
        Track track = findTrackById(id);
        track.getDislikes().remove(user);
        trackRepository.save(track);
    }

    @Override
    public void deleteById(Long id) {
        trackRepository.deleteById(id);
    }

    public Track buildTrack(String title,
                            String wordAuthor,
                            String musicAuthor,
                            String genreId,
                            String performerId,
                            String fileName) {

        Genre genre = genreRepository.findById(Long.parseLong(genreId)).orElse(null);
        User performer = userRepository.findById(Long.parseLong(performerId)).orElse(null);

        Track newTrack = new Track();
        newTrack.setTitle(title);
        newTrack.setWordAuthor(wordAuthor);
        newTrack.setMusicAuthor(musicAuthor);
        newTrack.setGenre(genre);
        newTrack.setPerformer(performer);
        newTrack.setCreateAt(new Date());
        newTrack.setListeningAmount(0L);

        newTrack.setLocationOnServer("uploads/" + newTrack.getPerformer().getUsername() + "/" + fileName);
        newTrack.setDownloadUrl(ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/uploads/")
                .path(newTrack.getPerformer().getUsername() + "/" + fileName)
                .toUriString());

        return newTrack;
    }

    @Override
    public List<Track> findAllSingerUserTracks(User user) {
        return trackRepository.findAllByPerformer(user);
    }

    @Override
    @Transactional
    public void deleteTrack(Long id) {
        Track track = trackRepository.findById(id).orElse(null);
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

    public Track updateTrack(Long id,
                             String title,
                             String wordAuthor,
                             String musicAuthor,
                             String genreId,
                             String fileName) {
        Track updatedTrack = trackRepository.findById(id).orElse(null);
        if (updatedTrack != null) {
            updatedTrack.setTitle(title);
            updatedTrack.setMusicAuthor(musicAuthor);
            updatedTrack.setWordAuthor(wordAuthor);
            updatedTrack.setGenre(genreRepository.findById(Long.parseLong(genreId)).orElse(null));

            if (!updatedTrack.getLocationOnServer().equals("uploads/" + updatedTrack.getPerformer().getUsername() + "/" + fileName)) {
                updatedTrack.setLocationOnServer("uploads/" + updatedTrack.getPerformer().getUsername() + "/" + fileName);
                updatedTrack.setDownloadUrl(ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path("/uploads/")
                        .path(updatedTrack.getPerformer().getUsername() + "/" + fileName)
                        .toUriString());
            }
        }
        return updatedTrack;
    }


    @Override
    public boolean isThere(Track track, String searchStr) {
        return searchStr.equals(track.getTitle()) || searchStr.equals(track.getMusicAuthor()) ||
                searchStr.equals(track.getWordAuthor()) || searchStr.equals(track.getPerformer().getUsername());
    }


}
