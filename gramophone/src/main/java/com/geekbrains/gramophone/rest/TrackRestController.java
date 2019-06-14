package com.geekbrains.gramophone.rest;

import com.geekbrains.gramophone.entities.Track;
import com.geekbrains.gramophone.services.TrackService;
import com.geekbrains.gramophone.services.UploadService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@CrossOrigin(origins = {"http://localhost:4200"})
@RequestMapping("api/tracks")
@Api(tags = "Tracks")
public class TrackRestController {

    private TrackService trackService;

    private UploadService uploadService;

    @Autowired
    public void setTrackService(TrackService trackService) {
        this.trackService = trackService;
    }

    @Autowired
    public void setUploadService(UploadService uploadService) {
        this.uploadService = uploadService;
    }

    @GetMapping("")
    public Iterable<Track> getAllTracks(@RequestParam(name = "search", required = false) String byAutorOrByTrack,
                                        @RequestParam(name = "genre", required = false) String genre) {

        List<Track> trackList = trackService.findAll();

        if (byAutorOrByTrack != null) {
            trackList = trackList.stream()
                        .filter(track -> isThere(track, byAutorOrByTrack))
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
    public Track getTrackById(@PathVariable("id") Long id) {
        return trackService.findTrackById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteTrackById(@PathVariable("id") Long id) {
         trackService.deleteById(id);
    }

    @PostMapping("/")
    public void setTrack(@RequestPart("track") Track track, @RequestPart(value = "file", required = false) MultipartFile file) throws IOException {
        Track updatedTrack = trackService.buildTrack(
                track,
                track.getPerformer(),
                file.getOriginalFilename()
        );

        if (!file.isEmpty()) {
            if (uploadService.upload(track.getPerformer().getUsername(), file, "uploads/")) {
                trackService.save(updatedTrack);
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

    private boolean isThere(Track track, String searchStr) {
        return searchStr.equals(track.getTitle()) || searchStr.equals(track.getMusicAuthor()) ||
                searchStr.equals(track.getWordAuthor()) || searchStr.equals(track.getPerformer().getUsername());
    }

}
