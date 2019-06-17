package com.geekbrains.gramophone.services;

import com.geekbrains.gramophone.entities.Comment;
import com.geekbrains.gramophone.entities.Track;
import com.geekbrains.gramophone.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface CommentService {
    boolean save(Comment comment);
    Page<Comment> getCommentsWithPagingAndFiltering(int pageNumber, int pageSize); //TODO добавить критерии отбора
    List<Comment> findByUserAndTrack(User user, Track track);
    List<Comment> findByTrack(Track track);
    Comment findCommentById(Long id);
    void changeLike(Long id, User user);
    void setLike(Long id, User user);
    void removeLike(Long id, User user);
    void changeDislike(Long id, User user);
    void setDislike(Long id, User user);
    void removeDislike(Long id, User user);
    void remove(Comment comment);
}
