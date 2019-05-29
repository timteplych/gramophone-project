package com.geekbrains.gramophone.controllers;

import com.geekbrains.gramophone.entities.Track;
import com.geekbrains.gramophone.services.GenreService;
import com.geekbrains.gramophone.services.TrackService;
import com.geekbrains.gramophone.services.UserService;
import com.geekbrains.gramophone.utils.FileUploader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.Date;

@Controller
public class TrackController {

    private TrackService trackService;
    private GenreService genreService;
    private FileUploader fileUploader;
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
    public void setFileUploader(FileUploader fileUploader) {
        this.fileUploader = fileUploader;
    }

    @PostMapping("/upload")
    public String upload(
            @RequestParam("file") MultipartFile file,
            @RequestParam("title") String title,
            @RequestParam("wordAuthor") String wordAuthor,
            @RequestParam("musicAuthor") String musicAuthor,
            @RequestParam("genre") String genre,
            Principal principal
    ) {

        if (!file.isEmpty()) {

            String username = principal.getName();

            Track track = new Track();
            track.setTitle(title);
            track.setWordAuthor(wordAuthor);
            track.setMusicAuthor(musicAuthor);
            track.setGenre(genreService.findByTitle(genre));
            track.setCreateAt(new Date());
            track.setListeningAmount(0L);
            track.setUser(userService.findByUsername(username));
            track.setLocationOnServer("uploads/" + username + file.getOriginalFilename());

            fileUploader.upload(username, file);

            trackService.save(track);
        }

        return "redirect:";
    }
}
