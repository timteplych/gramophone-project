package com.geekbrains.gramophone.services;


import com.geekbrains.gramophone.entities.Genre;
import com.geekbrains.gramophone.entities.Track;
import com.geekbrains.gramophone.repositories.PlaylistRepository;
import com.geekbrains.gramophone.repositories.TrackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.List;

@Service
public class SearchFilterService {



    private TrackRepository trackRepository;

    @Autowired
    public void setTrackRepository(TrackRepository trackRepository) {
        this.trackRepository = trackRepository;
    }


    public List<Track> searchByWordAuthorAndSongTitle(@Nullable String searchTerm,@Nullable Genre genre) {

        List<Track> trackList = new ArrayList<>();

        if (genre != null && searchTerm != null) {
            return trackRepository.findAllByTitleContainingOrMusicAuthorContainingAndGenre(searchTerm, searchTerm, genre);
        }

        if (genre != null) {
            trackList = trackRepository.findAllByGenre(genre);
        }

        if (searchTerm != null) {
           trackList = trackRepository.findAllByTitleContainingOrMusicAuthorContaining(searchTerm, searchTerm);

        }
        return trackList;
    }
}
