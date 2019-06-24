package com.geekbrains.gramophone.rest;

import com.geekbrains.gramophone.entities.Genre;
import com.geekbrains.gramophone.entities.Track;
import com.geekbrains.gramophone.services.GenreService;
import com.geekbrains.gramophone.services.TrackService;
import com.geekbrains.gramophone.services.UploadService;
import com.geekbrains.gramophone.services.UserService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@CrossOrigin(origins = {"http://localhost:4200"})
@RequestMapping("api/v1/tracks")
@Api(tags = "Tracks")
public class TrackRestController {

    private TrackService trackService;

    private GenreService genreService;

    private UserService userService;

    private UploadService uploadService;

    @Autowired
    public void setTrackService(TrackService trackService) {
        this.trackService = trackService;
    }

    @Autowired
    public void setGenreService(GenreService genreService) {
        this.genreService = genreService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }


    @Autowired
    public void setUploadService(UploadService uploadService) {
        this.uploadService = uploadService;
    }

    @GetMapping("")
    public Iterable<Track> getAllTracks(@RequestParam(name = "search", required = false) String byAuthorOrByTrack,
                                        @RequestParam(name = "genre", required = false) String genre) {

        List<Track> trackList = trackService.findAll();

        if (byAuthorOrByTrack != null) {
            trackList = trackList.stream()
                    .filter(track -> trackService.isThere(track, byAuthorOrByTrack))
                    .collect(Collectors.toList());
        }

        if (genre != null) {
            trackList = trackList.stream()
                    .filter(track -> genre.equals(track.getGenre().getTitle()))
                    .collect(Collectors.toList());
        }

        return trackList;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTrackById(@PathVariable("id") Long id) {
        Track track = trackService.findTrackById(id);
        System.out.println(track.getPerformer().getUsername());
        return new ResponseEntity<>(track, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public void deleteTrackById(@PathVariable("id") Long id) {
        trackService.deleteById(id);
    }

    @PostMapping("")
    public void setTrack(@RequestPart("title") String title,
                         @RequestPart("wordAuthor") String wordAuthor,
                         @RequestPart("musicAuthor") String musicAuthor,
                         @RequestPart("genreId") String genreId,
                         @RequestPart("performerId") String performerId,
                         @RequestPart(value = "file", required = false) MultipartFile file) {

        Track createdTrack = trackService.buildTrack(
                title,
                wordAuthor,
                musicAuthor,
                genreId,
                performerId,
                file.getOriginalFilename()
        );
        System.out.println(createdTrack.getPerformer().getUsername());
        if (!file.isEmpty()) {
            if (uploadService.upload(createdTrack.getPerformer().getUsername(), file, "uploads/")) {
                trackService.save(createdTrack);
            }
        }
    }

    @PutMapping("/{id}")
    public void updateTrackById(@PathVariable("id") Long id, @RequestPart("track") Track track, @RequestParam(value = "file", required = false) MultipartFile file) {
        Track updatedTrack = trackService.updateTrack(
                id,
                track,
                file.getOriginalFilename()
        );

        if (!file.isEmpty()) {
            if (uploadService.remove(track.getPerformer().getUsername(), file, "uploads/") &&
                    uploadService.upload(track.getPerformer().getUsername(), file, "uploads/")) {
                trackService.save(updatedTrack);
            }
        }
    }

    @PatchMapping("/{id}")
    public String updateListenAmountOfTrack(@PathVariable("id") Long id,
                                                    @RequestParam(value = "listeningAmount") String listeningAmount) {
        Track track = trackService.findTrackById(id);
        track.setListeningAmount(Long.parseLong(listeningAmount));
        System.out.println(track.getListeningAmount());
        trackService.save(track);
        return "success";
    }


    @GetMapping("/genres")
    public Iterable<Genre> getAllGenre() {
        return genreService.findAll();
    }

}
