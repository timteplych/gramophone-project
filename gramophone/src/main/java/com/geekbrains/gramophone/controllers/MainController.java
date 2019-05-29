package com.geekbrains.gramophone.controllers;

import com.geekbrains.gramophone.entities.Track;
import com.geekbrains.gramophone.services.GenreService;
import com.geekbrains.gramophone.services.TrackService;
import com.geekbrains.gramophone.services.UserService;
import com.geekbrains.gramophone.utils.TrackSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.Optional;

@Controller
public class MainController {

    private static final int INITIAL_PAGE = 0;
    private static final int PAGE_SIZE = 5;

    private TrackService trackService;
    private GenreService genreService;
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

    @GetMapping
    public String index(
            @RequestParam(value = "page") Optional<Integer> page,
            @RequestParam(value = "word", required = false) String word,
            Model model,
            Principal principal
    ) {
        final int currentPage = (page.orElse(0) < 1) ? INITIAL_PAGE : page.get() - 1;

        if(principal != null){
            String username = principal.getName();
            model.addAttribute("user", userService.findByUsername(username));
        }

        Specification<Track> spec = Specification.where(null);
        StringBuilder filters = new StringBuilder();
        if (word != null) {
            spec = spec.and(TrackSpecification.titleContains(word));
            filters.append("&word=" + word);
        }

        Page<Track> tracks = trackService.getProductsWithPagingAndFiltering(currentPage, PAGE_SIZE, spec);

        model.addAttribute("page", currentPage);
        model.addAttribute("totalPage", tracks.getTotalPages());
        model.addAttribute("tracks", tracks.getContent());
        model.addAttribute("genres", genreService.findAll());
        model.addAttribute("filters", filters.toString());
        model.addAttribute("word", word);

        return "index";
    }

}
