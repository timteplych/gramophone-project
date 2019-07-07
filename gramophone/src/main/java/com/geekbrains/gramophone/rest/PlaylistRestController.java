package com.geekbrains.gramophone.rest;

import com.geekbrains.gramophone.entities.Playlist;
import com.geekbrains.gramophone.entities.User;
import com.geekbrains.gramophone.services.PlaylistService;
import com.geekbrains.gramophone.services.SearchFilterService;
import com.geekbrains.gramophone.services.TrackService;
import com.geekbrains.gramophone.services.UserService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = {"http://localhost:4200"})
@RequestMapping("api/v1/playlists")
@Api(tags = "Playlists")
public class PlaylistRestController {

    private static final int INITIAL_PAGE = 0;
    private static final int PAGE_SIZE = 100;

    private UserService userService;

    private PlaylistService playlistService;

    private TrackService trackService;

    private SearchFilterService searchFilterService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setPlaylistService(PlaylistService playlistService) {
        this.playlistService = playlistService;
    }

    @Autowired
    public void setTrackService(TrackService trackService) {
        this.trackService = trackService;
    }

    @Autowired
    public void setSearchFilterService(SearchFilterService searchFilterService) {
        this.searchFilterService = searchFilterService;
    }

    @GetMapping("")
    public Iterable<Playlist> getAllPlaylists(@RequestParam(value = "page", required = false) Optional<Integer> page,
                                              @RequestParam(name = "search", required = false) String byPlaylist,
                                              @RequestParam(name = "user", required = false) String user) {
        final int currentPage = (page.orElse(0) < 1) ? INITIAL_PAGE : page.get() - 1;

        if (byPlaylist != null && user != null) {
            return searchFilterService.searchByTitleContainingUser(byPlaylist, userService.findByUsername(user));
        }

        return playlistService.getPlaylistsWithPaging(currentPage, PAGE_SIZE).getContent();
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getPlaylistById(@PathVariable("id") Long id) {
        Playlist playlist = playlistService.findPlaylistById(id);
        return new ResponseEntity<>(playlist, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public void deletePlaylistById(@PathVariable("id") Long id) {
        playlistService.removePlaylist(id);
    }

    @PostMapping("")
    public String setPlaylist(@RequestPart("name") String name,
                              @RequestPart("userId") String userId) {
        Playlist playlist = playlistService.buildPlaylist(
                name,
                userId
        );
        return "success";
    }

    @GetMapping("/{id}/playlists")
    public List<Playlist> getPerformerPlaylists(@PathVariable("id") Long id) {
        return playlistService.findAllPlaylistsByUser(userService.findById(id));
    }

    @PutMapping("/{id}")
    public void updatePlaylistById(@PathVariable("id") Long id,
                                   @RequestPart("name") String name,
                                   @RequestPart("userId") String userId) {
        Playlist updatePlaylist = playlistService.updatePlaylist(
                id,
                name,
                userId
        );
    }

    @GetMapping("/users")
    public Iterable<User> getAllUser() {
        return userService.findAll();
    }
}
