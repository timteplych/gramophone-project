package com.geekbrains.gramophone.controllers;

import com.geekbrains.gramophone.entities.Track;
import com.geekbrains.gramophone.services.GenreService;
import com.geekbrains.gramophone.services.TrackService;
import com.geekbrains.gramophone.services.UploadTrackService;
import com.geekbrains.gramophone.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

@Controller
public class UploadTrackController {

    private TrackService trackService;
    private GenreService genreService;
    private UploadTrackService uploadTrackService;
    private UserService userService;

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
    public void setUploadTrackService(UploadTrackService uploadTrackService) {
        this.uploadTrackService = uploadTrackService;
    }

    @GetMapping("/upload-track")
    public String showUploadPage(
            Principal principal,
            Model model) {

        String username = principal.getName();

        model.addAttribute("user", userService.findByUsername(username));
        model.addAttribute("genres", genreService.findAll());
        model.addAttribute("track", new Track());
        return "upload-page";
    }

    @PostMapping("/upload-process")
    public String uploadProcessing(
            @ModelAttribute("track") Track trackFromForm,
            @RequestParam("file") MultipartFile file,
            Principal principal
    ) {

        Track track = uploadTrackService.buildTrack(
                trackFromForm,
                userService.findByUsername(principal.getName()),
                file.getOriginalFilename()
        );

        if (!file.isEmpty()) {

            if (uploadTrackService.upload(principal.getName(), file)) {
                trackService.save(track);
                return "upload-success";
            } else {
                return "upload-fail";
            }
        }
        return "redirect:/";
    }
}
