package com.geekbrains.gramophone.services;

import com.geekbrains.gramophone.entities.Comment;
import com.geekbrains.gramophone.entities.LikeType;
import com.geekbrains.gramophone.entities.Track;
import com.geekbrains.gramophone.entities.User;
import com.geekbrains.gramophone.exceptions.NotFoundException;
import com.geekbrains.gramophone.repositories.CommentRepository;
import com.geekbrains.gramophone.repositories.GenreRepository;
import com.geekbrains.gramophone.repositories.TrackRepository;
import com.geekbrains.gramophone.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LikeService likeService;

    private CommentRepository commentRepository;

    @Autowired
    public void setCommentRepository(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }


    @Override
    public boolean save(Comment comment) {
        commentRepository.save(comment);
        return true;
    }

    @Override
    public Page<Comment> getCommentsWithPagingAndFiltering(int pageNumber, int pageSize) {
        return commentRepository.findAll(PageRequest.of(pageNumber, pageSize));
    }

    @Override
    public List<Comment> findByUserAndTrack(User user, Track track) {
        return commentRepository.findByUserAndTrack(user, track);
    }

    @Override
    public List<Comment> findByTrack(Track track) {
        return commentRepository.findByTrack(track);
    }


    @Override
    public void changeLike(Long id, Long userId) {
        Comment comment = findById(id);
        likeService.changeLike(userId, id, LikeType.COMMENT);
    }


    public Comment findById(Long id) {
        return commentRepository.findById(id).orElseThrow(() -> new NotFoundException("Comment", id));
    }


    @Override
    public void changeDislike(Long id, Long userId) {
        Comment comment = findById(id);
        likeService.changeDislike(userId, id, LikeType.COMMENT);
    }


    public void remove(Comment comment) {
        commentRepository.delete(comment);
    }

    public void deleteById(Long id) {commentRepository.deleteById(id);}
}
