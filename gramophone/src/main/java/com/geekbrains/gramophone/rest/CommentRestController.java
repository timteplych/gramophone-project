package com.geekbrains.gramophone.rest;

import com.geekbrains.gramophone.entities.User;
import com.geekbrains.gramophone.exceptions.NotFoundException;
import com.geekbrains.gramophone.services.*;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = {"http://localhost:4200"})
@RequestMapping("api/tracks")
@Api(tags = "Tracks")
public class CommentRestController {

    @Autowired
    private UserService userService;

    @Autowired
    private TrackService trackService;

    @Autowired
    private CommentService commentService;


    @PutMapping("/{id}/comments/{commentId}/like")
    public void likeComment(@PathVariable(value = "id") Long id,
                            @PathVariable(value = "commentId") Long commentId,
                            @RequestParam(value = "userId") Long userId) {
        User user = userService.findById(userId);
        if (user == null) {
            throw new NotFoundException("User", userId);
        }
        if (trackService.findTrackById(id) == null) {
            throw new NotFoundException("Track", id);
        }
        if (commentService.findCommentById(commentId) == null) {
            throw new NotFoundException("Comment", commentId);
        }
        commentService.changeLike(commentId, user); 
    }

    @PutMapping("/{id}/comments/{commentId}/dislike")
    public void dislikeComment(@PathVariable(value = "id") Long id,
                            @PathVariable(value = "commentId") Long commentId,
                            @RequestParam(value = "userId") Long userId) {
        User user = userService.findById(userId);
        if (user == null) {
            throw new NotFoundException("User", userId);
        }
        if (trackService.findTrackById(id) == null) {
            throw new NotFoundException("Track", id);
        }
        if (commentService.findCommentById(commentId) == null) {
            throw new NotFoundException("Comment", commentId);
        }
        trackService.changeDislike(id, user);
    }
}
