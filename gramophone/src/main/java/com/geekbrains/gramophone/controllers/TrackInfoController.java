package com.geekbrains.gramophone.controllers;

import com.geekbrains.gramophone.entities.Comment;
import com.geekbrains.gramophone.entities.Track;
import com.geekbrains.gramophone.entities.User;
import com.geekbrains.gramophone.services.CommentService;
import com.geekbrains.gramophone.services.TrackService;
import com.geekbrains.gramophone.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;
import java.util.Locale;

@Controller
public class TrackInfoController {

    @Value("${comment.min.length}")
    private Integer MIN_COMMENT;

    @Autowired
    private MessageSource messageSource;

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

    @GetMapping("/track/{id}/info")
    public String trackInfo(Model model, Principal principal,@PathVariable(value = "id") Long id)
    {
        String error = (String) model.asMap().get("error");
        Track track = trackService.findTrackById(id);
        List<Comment> comments = commentService.findByTrack(track);
        if (principal != null) {
            User user = userService.findByUsername(principal.getName());
            model.addAttribute("user",user);
            model.addAttribute("allowComment", true);
            model.addAttribute("comment", new Comment(user, track));
        }
        model.addAttribute("error", error);
        model.addAttribute("track",track);
        model.addAttribute("comments", comments);
        return "track-info";

    }

//    @PostMapping("/track/{id}/like")
//    public String likeTrack(Principal principal,  Locale locale,  RedirectAttributes redir,
//                            @PathVariable(value = "id") Long id) {
//        if (principal != null) {
//            User user = userService.findByUsername(principal.getName());
//            trackService.changeLike(id, user.getId());
//        } else {
//            redir.addFlashAttribute("error", messageSource.getMessage("error.notAuthorized", null, locale));
//        }
//        return "redirect:/track/" + id + "/info";
//    }

//    @PostMapping("/comment/{id}/dislike")
//    public String dislikeComment(Principal principal,  Locale locale,  RedirectAttributes redir,
//                              @PathVariable(value = "id") Long id) {
//        if (principal != null) {
//            User user = userService.findByUsername(principal.getName());
//            commentService.changeDislike(id, user.getId());
//        } else {
//            redir.addFlashAttribute("error", messageSource.getMessage("error.notAuthorized", null, locale));
//        }
//        return "redirect:/track/" + commentService.findById(id).getTrack().getId() + "/info";
//    }

//    @PostMapping("/track/{id}/dislike")
//    public String dislikeTrack(Principal principal,  Locale locale,  RedirectAttributes redir,
//                            @PathVariable(value = "id") Long id) {
//        if (principal != null) {
//            User user = userService.findByUsername(principal.getName());
//            trackService.changeDislike(id, user.getId());
//        } else {
//            redir.addFlashAttribute("error", messageSource.getMessage("error.notAuthorized", null, locale));
//        }
//        return "redirect:/track/" + id + "/info";
//    }

//    @PostMapping("/comment/{id}/like")
//    public String likeComment(Principal principal,  Locale locale,  RedirectAttributes redir,
//                              @PathVariable(value = "id") Long id) {
//        if (principal != null) {
//            User user = userService.findByUsername(principal.getName());
//            commentService.changeLike(id, user.getId());
//        } else {
//            redir.addFlashAttribute("error", messageSource.getMessage("error.notAuthorized", null, locale));
//        }
//        return "redirect:/track/" + commentService.findById(id).getTrack().getId() + "/info";
//    }

    @PostMapping("/comment/add")
    public String commentSubmit(Locale locale,  RedirectAttributes redir,
                                @ModelAttribute Comment comment) {
        String errorCode = null;
        if (comment.getContent().length() > MIN_COMMENT) {
            commentService.save(comment);
        } else {
            errorCode = "error.comment.minLength";
        }
        if (errorCode != null)
            redir.addFlashAttribute("error", messageSource.getMessage(errorCode, null, locale));
        return "redirect:/track/" + comment.getTrack().getId() + "/info";
    }

    @PostMapping("/comment/{id}/remove")
    public String removeComment(Principal principal, Locale locale,  RedirectAttributes redir,
                                @PathVariable(value = "id") Long id,
                                Long trackId) {
        String errorCode = null;
        Comment comment = commentService.findById(id);
        if (comment == null) {
            errorCode = "error.accessDenied";
        }
        if (comment.getTrack().getId() != trackId) {
            errorCode = "error.internal";
        }
        if (principal != null) {
            User user = userService.findByUsername(principal.getName());
            if (comment !=null && comment.getUser().equals(user)) {
                commentService.remove(comment);
            } else {
                errorCode = "error.accessDenied";
            }
        } else {
            errorCode = "error.notAuthorized";
        }
        if (errorCode != null)
            redir.addFlashAttribute("error", messageSource.getMessage(errorCode, null, locale));
        return "redirect:/track/" + trackId + "/info";
    }
}
