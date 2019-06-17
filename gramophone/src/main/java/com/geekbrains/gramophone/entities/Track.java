package com.geekbrains.gramophone.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
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

    @Column(name = "download_url")
    private String downloadUrl;

    @OneToOne
    @JoinColumn(name = "genre_id")
    private Genre genre;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_at")
    private Date createAt;

    @Column(name = "listening_amount")
    private Long listeningAmount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User performer;

    @Column(name = "cover")
    private String cover;

    @OneToMany
    @JoinTable(name = "tracks_likes",
            joinColumns = @JoinColumn(name = "track_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> likes = new HashSet<>();

    @OneToMany
    @JoinTable(name = "tracks_dislikes",
            joinColumns = @JoinColumn(name = "track_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> dislikes = new HashSet<>();

    @OneToMany(mappedBy = "track")
    private Set<Comment> comments = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Track track = (Track) o;
        return Objects.equals(id, track.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override public String toString() {
        return "Track: " + this.id;
    }
}