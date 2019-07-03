package com.geekbrains.gramophone.rest.dto;

import com.geekbrains.gramophone.entities.Comment;
import com.geekbrains.gramophone.entities.LikeType;
import com.geekbrains.gramophone.entities.User;
import com.geekbrains.gramophone.services.LikeService;
import lombok.Data;

import java.util.List;
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

    public CommentDTO(Comment comment, LikeService likeService) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.user = new UserDTO(comment.getUser());

        List<User> likes = likeService.getLikes(id, LikeType.COMMENT);
        if (likes != null) this.likes = likes.stream().map(UserDTO::new).collect(Collectors.toSet());
        List<User> dislikes = likeService.getDislikes(id, LikeType.COMMENT);
        if (dislikes != null) this.dislikes = dislikes.stream().map(UserDTO::new).collect(Collectors.toSet());

        this.track = new TrackDTO(comment.getTrack().getId());
    }

}
