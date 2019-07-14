package com.geekbrains.gramophone.entities;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "likes")
@Data
public class Like {

    public static final byte LIKE = 1;
    public static final byte DISLIKE = -1;
    public static final byte EMPTY = 0;

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

    @Column(name = "mark")
    private byte mark;


    public Like(LikeType likeType, User user, Long targetId, byte mark) {
        this.likeType = likeType;
        this.user = user;
        this.targetId = targetId;
        this.mark = mark;
    }

    public Like() {};
}
