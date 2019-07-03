package com.geekbrains.gramophone.rest;

import com.geekbrains.gramophone.entities.Genre;
import com.geekbrains.gramophone.entities.Track;
import com.geekbrains.gramophone.entities.User;
import com.geekbrains.gramophone.exceptions.NotFoundException;
import com.geekbrains.gramophone.services.*;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;


@RestController
@CrossOrigin(origins = {"http://localhost:4200"})
@RequestMapping("api/v1/tracks")
@Api(tags = "Tracks")
public class TrackRestController {

    private static final int INITIAL_PAGE = 0;
    private static final int PAGE_SIZE = 100;

    private UserService userService;

    private TrackService trackService;

    private SearchFilterService searchFilterService;

    private GenreService genreService;

    private UploadService uploadService;

    @Autowired
    private LikeService likeService;

    @Autowired
    public void setTrackService(TrackService trackService) {
        this.trackService = trackService;
    }

    @Autowired
    public void setSearchFilterService(SearchFilterService searchFilterService) {
        this.searchFilterService = searchFilterService;
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
    public Iterable<Track> getAllTracks(@RequestParam(value = "page", required = false) Optional<Integer> page,
                                        @RequestParam(name = "search", required = false) String byAuthorOrByTrack,
                                        @RequestParam(name = "genre", required = false) String genre) {

        final int currentPage = (page.orElse(0) < 1) ? INITIAL_PAGE : page.get() - 1;

        if (byAuthorOrByTrack != null && genre != null) {
            return searchFilterService.searchByWordAuthorAndSongTitle(byAuthorOrByTrack, genreService.findByTitle(genre));
        }


//        if (byAuthorOrByTrack != null) {
//            trackList = trackList.stream()
//                    .filter(track -> trackService.find(track, byAuthorOrByTrack))
//                    .collect(Collectors.toList());
//        }

//        if (genre != null) {
//            trackList = trackList.stream()
//                    .filter(track -> genre.equals(track.getGenre().getTitle()))
//                    .collect(Collectors.toList());
//        }

        return trackService.getTracksWithPaging(currentPage, PAGE_SIZE).getContent();
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
    public String setTrack(@RequestPart("title") String title,
                           @RequestPart("wordAuthor") String wordAuthor,
                           @RequestPart("musicAuthor") String musicAuthor,
                           @RequestPart("genreId") String genreId,
                           @RequestPart("performerId") String performerId,
                           @RequestPart(value = "file") MultipartFile file) {

        Track t = trackService.buildTrack(
                title,
                wordAuthor,
                musicAuthor,
                genreId,
                performerId,
                file);

        return "success";
    }

    @GetMapping("/{id}/tracks")
    public List<Track> getPerformerTracks(@PathVariable("id") Long id) {
        return trackService.findAllSingerUserTracks(userService.findById(id));
    }

    @PutMapping("/{id}")
    public void updateTrackById(@PathVariable("id") Long id,
                                @RequestPart("title") String title,
                                @RequestPart("wordAuthor") String wordAuthor,
                                @RequestPart("musicAuthor") String musicAuthor,
                                @RequestPart("genreId") String genreId,
                                @RequestParam(value = "file", required = false) MultipartFile file) {
        Track updatedTrack = trackService.updateTrack(
                id,
                title,
                wordAuthor,
                musicAuthor,
                genreId,
                file.getOriginalFilename()
        );

        if (!file.isEmpty()) {
            if (uploadService.remove(updatedTrack.getPerformer().getUsername(), file, "uploads/") &&
                    uploadService.upload(updatedTrack.getPerformer().getUsername(), file, "uploads/")) {
                trackService.save(updatedTrack);
            }
        }
    }

    @PatchMapping("/{id}")
    public void updateListenAmountOfTrack(@PathVariable("id") Long id,
                                          @RequestParam(value = "listeningAmount") String listeningAmount) {
        Track track = trackService.findTrackById(id);
        track.setListeningAmount(Long.parseLong(listeningAmount));
        System.out.println(track.getListeningAmount());
        trackService.save(track);
    }


    @GetMapping("/genres")
    public Iterable<Genre> getAllGenre() {
        return genreService.findAll();
    }


    @PatchMapping("/{id}/like")
    public void likeTrack(@PathVariable(value = "id") Long id,
                          @RequestParam(value = "userId") Long userId) {
        trackService.changeLike(id, userId);
    }

    @PatchMapping("/{id}/dislike")
    public void dislikeTrack(@PathVariable(value = "id") Long id,
                             @RequestParam(value = "userId") Long userId) {
        trackService.changeDislike(id, userId);
    }
}
