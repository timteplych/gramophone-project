package com.geekbrains.gramophone.controllers;

import com.geekbrains.gramophone.entities.Track;
import com.geekbrains.gramophone.entities.User;
import com.geekbrains.gramophone.services.PlaylistService;
import com.geekbrains.gramophone.services.TrackService;
import com.geekbrains.gramophone.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
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

    @RequestMapping("/playlist-page/user/{userId}")
    public String showPlaylistPage(
            @PathVariable("userId") Long userId,
            Principal principal,
            Model model
    ) {
        User currentUser = userService.findByUsername(principal.getName());
        User user = userService.findById(userId).get();

        List<Track> allGramophoneTracks = trackService.findAll();
        List<Track> userTracks = user.getPlaylist().getTracks();

        model.addAttribute("user", user);
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("isCurrentUser", currentUser.getId().equals(user.getId()));
        model.addAttribute("allGramophoneTracks", allGramophoneTracks);
        model.addAttribute("userTracks", userTracks);

        return "playlist-page";
    }

    @RequestMapping("/add-track-to-playlist")
    public String addTrackInPlaylist(
            @RequestParam("trackId") Long trackId,
            Principal principal
    ) {
        User currentUser = userService.findByUsername(principal.getName());

        Track track = trackService.findTrackById(trackId);
        playlistService.addTrack(currentUser, track);

        return "redirect:/playlist-page/user/" + currentUser.getId(); // переделать возврат на ту страницу с которой был добавлен трек
    }

    @RequestMapping("/remove-track-to-playlist")
    public String removeTrackFromPlaylist(
            @RequestParam("trackId") Long trackId,
            Principal principal
    ) {
        User currentUser = userService.findByUsername(principal.getName());

        Track track = trackService.findTrackById(trackId);
        playlistService.removeTrack(currentUser, track);

        return "redirect:/playlist-page/user/" + currentUser.getId();
    }

}
