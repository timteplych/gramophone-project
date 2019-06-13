package com.geekbrains.gramophone.controllers;

import com.geekbrains.gramophone.entities.Playlist;
import com.geekbrains.gramophone.entities.Track;
import com.geekbrains.gramophone.entities.User;
import com.geekbrains.gramophone.services.PlaylistService;
import com.geekbrains.gramophone.services.TrackService;
import com.geekbrains.gramophone.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
public class PlaylistController {

    private UserService userService;
    private PlaylistService playlistService;
    private TrackService trackService;

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

    @PostMapping("/playlists/add")
    public String addPlaylist(
            @RequestParam("playlist_name") String playlistName,
            Principal principal
    ){
        // сделать проверку, если пользователь хочет создать плейлист с существующим именем

        User currentUser = userService.findByUsername(principal.getName());
        playlistService.addPlaylist(currentUser, playlistName);

        return "redirect:/users/" + currentUser.getId() + "/playlists";
    }

    @RequestMapping("/users/{user_id}/playlists")
    public String showPlaylistPage(
            @PathVariable("user_id") Long userId,
            Principal principal,
            Model model
    ) {
        User currentUser = userService.findByUsername(principal.getName());
        User user = userService.findById(userId).get();
        List<Playlist> playlistList = playlistService.findAllPlaylistsByUser(user);

        List<Track> allGramophoneTracks = trackService.findAll();

        model.addAttribute("user", user);
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("playlistList", playlistList);
        model.addAttribute("isCurrentUser", currentUser.getId().equals(user.getId()));
        model.addAttribute("allGramophoneTracks", allGramophoneTracks);

        return "playlist-page";
    }

    @RequestMapping("/playlists/add/{track_id}")
    public String showAddTrackPage(
            @PathVariable("track_id") Long trackId,
            Model model,
            Principal principal
    ){
        User currentUser = userService.findByUsername(principal.getName());
        Track track = trackService.findTrackById(trackId);
        List<Playlist> playlistList = playlistService.findAllPlaylistsByUser(currentUser);

        model.addAttribute("playlistList", playlistList);
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("track", track);
        return "add-track";
    }

    @PostMapping("/playlists/track/add")
    public String addTrackInPlaylist(
            @RequestParam("playlist_id") Long playlistId,
            @RequestParam("track_id") Long trackId,
            Principal principal
    ) {
        User currentUser = userService.findByUsername(principal.getName());
        playlistService.addTrack(currentUser, playlistId, trackId);

        return "redirect:/users/" + currentUser.getId() + "/playlists"; // переделать возврат на ту страницу с которой был добавлен трек
    }

    @RequestMapping("/playlists/{playlist_id}/{track_id}/remove")
    public String removeTrackFromPlaylist(
            @PathVariable("playlist_id") Long playlistId,
            @PathVariable("track_id") Long trackId,
            Principal principal
    ) {
        User currentUser = userService.findByUsername(principal.getName());
        playlistService.removeTrack(currentUser, playlistId, trackId);

        return "redirect:/users/" + currentUser.getId() + "/playlists";
    }

}
