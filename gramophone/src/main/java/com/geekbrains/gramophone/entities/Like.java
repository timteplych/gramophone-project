package com.geekbrains.gramophone.entities;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "likes")
@Data
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "like_type")
    @Enumerated(EnumType.STRING)
    private LikeType likeType;

    @ManyToOne()
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "target_id")
    private Long targetId;

    @Column(name = "like_or_dislike")
    private byte likeOrDislike;

}
