package com.geekbrains.gramophone.repositories;

import com.geekbrains.gramophone.entities.Comment;
import com.geekbrains.gramophone.entities.Track;
import com.geekbrains.gramophone.entities.User;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends PagingAndSortingRepository<Comment, Long>, JpaSpecificationExecutor<Comment> {
    List<Comment> findByUserAndTrack(User user, Track track);
    List<Comment> findByTrack(Track track);

    @Query(value = "select count(*) from comments_likes tl where comment_id = ?1 and user_id = ?2", nativeQuery = true)
    int commentLikedBy(Long commentId, Long userId);

    @Query(value = "select count(*) from comments_dislikes tl where comment_id = ?1 and user_id = ?2", nativeQuery = true)
    int commentDislikedBy(Long commentId, Long userId);
}
