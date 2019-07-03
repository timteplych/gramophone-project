package com.geekbrains.gramophone.services;

import com.geekbrains.gramophone.entities.Comment;
import com.geekbrains.gramophone.entities.Track;
import com.geekbrains.gramophone.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

public interface CommentService {
    boolean save(Comment comment);
    Page<Comment> getCommentsWithPagingAndFiltering(int pageNumber, int pageSize); //TODO добавить критерии отбора
    List<Comment> findByUserAndTrack(User user, Track track);
    List<Comment> findByTrack(Track track);
    Comment findById(Long id);
    void changeLike(Long id, Long userId);
    void changeDislike(Long id, Long userId);
    void remove(Comment comment);
    void deleteById(Long id);
}
