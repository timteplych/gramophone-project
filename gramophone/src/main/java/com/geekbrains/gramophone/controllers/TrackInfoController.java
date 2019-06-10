package com.geekbrains.gramophone.controllers;

import com.geekbrains.gramophone.entities.Comment;
import com.geekbrains.gramophone.entities.Track;
import com.geekbrains.gramophone.entities.User;
import com.geekbrains.gramophone.services.CommentService;
import com.geekbrains.gramophone.services.TrackService;
import com.geekbrains.gramophone.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
public class TrackInfoController {

    @Value("${comment.min.length}")
    private Integer MIN_COMMENT;

    private TrackService trackService;
    private UserService userService;
    private CommentService commentService;

    @Autowired
    public void setTrackService(TrackService trackService) {
        this.trackService = trackService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setCommentService(CommentService commentService) {this.commentService = commentService;}

    @GetMapping("/track-info/{id}")
    public String trackInfo(Model model, Principal principal, @PathVariable(value = "id") Long id)
    {
        Track track = trackService.findTrackById(id);
        model.addAttribute("track",track);
        User user;
        if (principal != null) {
            user = userService.findByUsername(principal.getName());
            model.addAttribute("user",user);
            boolean allowComment = commentService.findByUserAndTrack(user, track).isEmpty();
            model.addAttribute("allowComment", allowComment);
            model.addAttribute("comment", new Comment(user, track));
        }
        List<Comment> comments = commentService.findByTrack(track);
        model.addAttribute("comments", comments);
        return "track-info";

    }

    @GetMapping("/like-track/{id}")
    public String likeTrack(Principal principal,  @PathVariable(value = "id") Long id) {
        if (principal != null) {
            User user = userService.findByUsername(principal.getName());
            trackService.changeLike(id, user);
        }
        return "redirect:/track-info/"+id.toString();
    }

    @GetMapping("/like-comment/{id}")
    public String likeComment(Principal principal,  @PathVariable(value = "id") Long id) {
        if (principal != null) {
            User user = userService.findByUsername(principal.getName());
            commentService.changeLike(id, user);
        }
        return "redirect:/track-info/"+commentService.findCommentById(id).getTrack().getId().toString();
    }

    @PostMapping("/add-comment")
    public String commentSubmit(@ModelAttribute Comment comment) {
        if (comment.getContent().length() > MIN_COMMENT) {
            commentService.save(comment);
        }
        return "redirect:/track-info/"+comment.getTrack().getId().toString();
    }

    @GetMapping("/remove-comment/{id}")
    public String removeComment(Principal principal,  @PathVariable(value = "id") Long id) {
        Comment comment = commentService.findCommentById(id);
        if (comment == null) return "redirect:/";
        Long trackId = comment.getTrack().getId();
        if (principal != null) {
            User user = userService.findByUsername(principal.getName());
            if (comment !=null && comment.getUser().equals(user)) {
                commentService.remove(comment);
            }
        }
        return "redirect:/track-info/" + trackId.toString();
    }
}
