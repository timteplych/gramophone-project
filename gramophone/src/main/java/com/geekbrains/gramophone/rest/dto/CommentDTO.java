package com.geekbrains.gramophone.rest.dto;

import com.geekbrains.gramophone.entities.Comment;
import lombok.Data;
import java.util.Set;
import java.util.stream.Collectors;

@Data
public class CommentDTO {

    private Long id;
    private String content;
    private Set<UserDTO> likes;
    private Set<UserDTO> dislikes;
    private UserDTO user;
    private TrackDTO track;

    public CommentDTO() {
    }

    public CommentDTO(Comment comment) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.user = new UserDTO(comment.getUser());
//        this.likes = comment.getLikes().stream().map(UserDTO::new).collect(Collectors.toSet());
//        this.dislikes = comment.getDislikes().stream().map(UserDTO::new).collect(Collectors.toSet());
        this.track = new TrackDTO(comment.getTrack().getId());
    }

}
