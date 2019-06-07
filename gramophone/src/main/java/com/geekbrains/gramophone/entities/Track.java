package com.geekbrains.gramophone.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "tracks")
@Data
public class Track {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "word_author")
    private String wordAuthor;

    @Column(name = "music_author")
    private String musicAuthor;

    @Column(name = "location_on_server")
    private String locationOnServer;

    @OneToOne
    @JoinColumn(name = "genre_id")
    private Genre genre;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_at")
    private Date createAt;

    @Column(name = "listening_amount")
    private Long listeningAmount;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id")
//    @JsonBackReference
//    private User performer;

    @Column(name = "performer")
    private String performer;

    @Column(name = "cover")
    private String cover;

    @OneToMany
    @JoinTable(name = "tracks_likes",
            joinColumns = @JoinColumn(name = "track_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> likes;

    @OneToMany(mappedBy = "track")
    private Set<Comment> comments;

}