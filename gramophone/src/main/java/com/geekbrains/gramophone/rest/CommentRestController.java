package com.geekbrains.gramophone.rest;

import com.geekbrains.gramophone.entities.Comment;
import com.geekbrains.gramophone.entities.Track;
import com.geekbrains.gramophone.entities.User;
import com.geekbrains.gramophone.exceptions.NotFoundException;
import com.geekbrains.gramophone.rest.dto.CommentDTO;
import com.geekbrains.gramophone.rest.dto.UserDTO;
import com.geekbrains.gramophone.services.*;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

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

    @GetMapping("/{id}/comments")
    public Iterable<CommentDTO> getAllComments(@PathVariable(value = "id") Long id) {
        Track track = trackService.findTrackById(id);
        if (track == null) {
            throw new NotFoundException("Track", id);
        }
        return commentService.findByTrack(track).stream().map(CommentDTO::new).collect(Collectors.toList());
    }

    @GetMapping("{id}/comments/{commentId}")
    public CommentDTO getComment(@PathVariable(value = "id") Long id,
                                 @PathVariable(value = "commentId") Long commentId) {
        if (trackService.findTrackById(id) == null) {
            throw new NotFoundException("Track", id);
        }
        Comment comment = commentService.findById(commentId);
        return new CommentDTO(comment);
    }

    @PostMapping("/{id}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public void postNewComment(@PathVariable(value = "id") Long id,
                               @RequestBody CommentDTO commentDTO) {
        if (trackService.findTrackById(id) == null) {
            throw new NotFoundException("Track", id);
        }
        Comment comment = new Comment();
        comment.setContent(commentDTO.getContent());
        comment.setUser(userService.findById(commentDTO.getUser().getId()));
        comment.setTrack(trackService.findTrackById(id));
        commentService.save(comment);
    }

    @PutMapping("/{id}/comments/{commentId}")
    public void changeComment(@PathVariable(value = "id") Long id,
                              @PathVariable(value = "commentId") Long commentId,
                              @RequestBody CommentDTO comment) {
        if (trackService.findTrackById(id) == null) {
            throw new NotFoundException("Track", id);
        }
        Comment oldComment = commentService.findById(commentId);
        if (comment.getUser().getId() == oldComment.getUser().getId()
                && oldComment.getTrack().getId() == id) {
            //единственное, что можно поменять
            oldComment.setContent(comment.getContent());
            commentService.save(oldComment);
        }
    }

    @DeleteMapping("/{id}/comments/{commentId}")
    public void deleteComment(@PathVariable(value = "id") Long id,
                              @PathVariable(value = "commentId") Long commentId) {
        if (trackService.findTrackById(id) == null) {
            throw new NotFoundException("Track", id);
        }
        commentService.deleteById(commentId);
    }


//    @PutMapping("/{id}/comments/{commentId}/like")
//    public void likeComment(@PathVariable(value = "id") Long id,
//                            @PathVariable(value = "commentId") Long commentId,
//                            @RequestParam(value = "userId") Long userId) {
//        if (trackService.findTrackById(id) == null) {
//            throw new NotFoundException("Track", id);
//        }
//        commentService.changeLike(commentId, userId);
//    }
//
//    @PutMapping("/{id}/comments/{commentId}/dislike")
//    public void dislikeComment(@PathVariable(value = "id") Long id,
//                               @PathVariable(value = "commentId") Long commentId,
//                               @RequestParam(value = "userId") Long userId) {
//        if (trackService.findTrackById(id) == null) {
//            throw new NotFoundException("Track", id);
//        }
//        commentService.changeDislike(commentId, userId);
//    }

}
