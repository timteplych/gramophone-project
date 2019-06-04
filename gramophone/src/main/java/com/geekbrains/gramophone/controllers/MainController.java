package com.geekbrains.gramophone.controllers;

import com.geekbrains.gramophone.entities.Track;
import com.geekbrains.gramophone.repositories.specifications.TrackSpecs;
import com.geekbrains.gramophone.services.TrackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
@RequestMapping("/")
public class MainController {

    private static final int INITIAL_PAGE = 0;
    private static final int PAGE_SIZE = 2;

    private TrackService trackService;

    @Autowired
    public void setTrackService(TrackService trackService) {
        this.trackService = trackService;
    }

    @GetMapping("/")
    public String index(Model model,
                        @RequestParam(value = "page") Optional<Integer> page,
                        @RequestParam(value = "title", required = false) String title,
                        @RequestParam(value = "wordAuthor", required = false) String wordAuthor,
                        @RequestParam(value = "musicAuthor", required = false) String musicAuthor
    ) {
        final int currentPage = (page.orElse(0) < 1) ? INITIAL_PAGE : page.get() - 1;

        Specification<Track> spec = Specification.where(null);
        StringBuilder filters = new StringBuilder();
        if (title != null) {
            spec = spec.and(TrackSpecs.titleContains(title));
            filters.append("&title=" + title);
        }
        if (wordAuthor != null) {
            spec = spec.and(TrackSpecs.wordAuthorContains(wordAuthor));
            filters.append("&wordAuthor=" + wordAuthor);
        }
        if (musicAuthor != null) {
            spec = spec.and(TrackSpecs.musicAuthorContains(musicAuthor));
            filters.append("&album=" + musicAuthor);
        }

        Page<Track> tracks = trackService.getTracksWithPagingAndFiltering(currentPage, PAGE_SIZE, spec);

        model.addAttribute("tracks", tracks.getContent());
        model.addAttribute("page", currentPage);
        model.addAttribute("totalPage", tracks.getTotalPages());

        model.addAttribute("filters", filters.toString());

        model.addAttribute("wordAuthor", wordAuthor);
        model.addAttribute("musicAuthor", musicAuthor);
        model.addAttribute("title", title);
        return "index";
    }
}
