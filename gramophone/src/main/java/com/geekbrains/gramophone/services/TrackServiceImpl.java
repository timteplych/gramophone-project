package com.geekbrains.gramophone.services;

import com.geekbrains.gramophone.entities.*;
import com.geekbrains.gramophone.exceptions.NotFoundException;
import com.geekbrains.gramophone.repositories.GenreRepository;
import com.geekbrains.gramophone.repositories.TrackRepository;
import com.geekbrains.gramophone.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.transaction.Transactional;
import java.io.File;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class TrackServiceImpl implements TrackService {

    private UserRepository userRepository;
    private TrackRepository trackRepository;
    private GenreRepository genreRepository;

    private UploadService uploadService;

    @Autowired
    private LikeService likeService;


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

    @Autowired
    public void setUploadService(UploadService uploadService) {
        this.uploadService = uploadService;
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
        return trackRepository.findAll(PageRequest.of(pageNumber, pageSize));
    }

    public Page<Track> getTracksWithPaging(int pageNumber, int pageSize) {
        return trackRepository.findAll(PageRequest.of(pageNumber, pageSize));
    }

    @Override
    public List<Track> findByTitle(String title) {
        if ("".equals(title)) {
            return Collections.emptyList();
        }
        return null;
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


    @Override
    public void changeLike(Long id, Long userId) {
        trackRepository.findById(id).orElseThrow(() -> new NotFoundException("Track", id));
        likeService.changeDislike(userId, id, LikeType.SONG);
    }

    @Override
    public void changeDislike(Long id, Long userId) {
        trackRepository.findById(id).orElseThrow(() -> new NotFoundException("Track", id));
        likeService.changeDislike(userId, id, LikeType.SONG);

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
                            MultipartFile file) {

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


        newTrack.setLocationOnServer("uploads/" + performer.getUsername() + "/" + file.getOriginalFilename());
        newTrack.setDownloadUrl(ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/uploads/")
                .path(performer.getUsername() + "/" + file.getOriginalFilename())
                .toUriString());


        if (!file.isEmpty()) {
            if (uploadService.upload(performer.getUsername(), file, "uploads/")) {
                trackRepository.save(newTrack);
            }
        }


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
        if (track != null) {
            track.setDeleted(true);
            track.setDeleteAt(new Date());
            trackRepository.deleteTrackFromAllPlaylists(id); // оставить сообщение, что трек был удален(название исполнителя и трека)
            trackRepository.deleteById(id);
        }
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
