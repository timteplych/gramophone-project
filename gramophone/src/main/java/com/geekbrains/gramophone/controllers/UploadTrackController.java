package com.geekbrains.gramophone.controllers;

import com.geekbrains.gramophone.entities.Track;
import com.geekbrains.gramophone.services.GenreService;
import com.geekbrains.gramophone.services.TrackService;
import com.geekbrains.gramophone.services.UploadService;
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
    private UploadService uploadService;

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
    public void setUploadService(UploadService uploadService) {
        this.uploadService = uploadService;
    }

    @GetMapping("/upload/track")
    public String showUploadPage(
            Principal principal,
            Model model) {

        String username = principal.getName();

        model.addAttribute("user", userService.findByUsername(username));
        model.addAttribute("genres", genreService.findAll());
        model.addAttribute("track", new Track());
        return "upload-page";
    }

    @PostMapping("/upload/track")
    public String uploadProcessing(
            @ModelAttribute("track") Track trackFromForm,
            @RequestParam("file") MultipartFile file,
            @RequestParam("genre") Long genreId,
            Principal principal
    ) {

        // временно пришлось реализовать загрузку файла таким плохим способом, поскольку
        // метод buildTrack() в TrackService полностью был изменен
        // решил оставить так, поскольку всё-равно этот контроллер нужно будет переделывать в RestController

        Track track = trackService.buildTrack(
                trackFromForm.getTitle(),
                trackFromForm.getWordAuthor(),
                trackFromForm.getMusicAuthor(),
                genreService.findById(genreId).getId().toString(),
                userService.findByUsername(principal.getName()).getId().toString(),
                file
        );

//        Track track = trackService.buildTrack(
//                trackFromForm,
//                userService.findByUsername(principal.getName()),
//                file.getOriginalFilename()
//        );

        if (!file.isEmpty()) {

            if (uploadService.upload(principal.getName(), file, "uploads/")) {
                trackService.save(track);
                return "upload-success";
            } else {
                return "upload-fail";
            }
        }
        return "redirect:/"; // сообщить, что файл пустой трек не загружен
    }
}
