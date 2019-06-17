package com.geekbrains.gramophone.controllers;

import com.geekbrains.gramophone.services.TrackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class TrackController {

    private TrackService trackService;

    @Autowired
    public void setTrackService(TrackService trackService) {
        this.trackService = trackService;
    }

    @GetMapping("/users/{user_id}/tracks/remove/{track_id}")
    public String removeSingerTrack(
            @PathVariable("track_id") Long trackId,
            @PathVariable("user_id") Long userId
    ){

        trackService.deleteTrack(trackId);

        return "redirect:/users/" + userId;
    }
}
