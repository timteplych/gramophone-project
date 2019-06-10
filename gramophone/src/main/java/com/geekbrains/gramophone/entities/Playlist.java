package com.geekbrains.gramophone.entities;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "playlist")
@Data
public class Playlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name = "default";

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "playlist_tracks",
            joinColumns = @JoinColumn(name = "playlist_id"),
            inverseJoinColumns = @JoinColumn(name = "track_id"))
    private List<Track> tracks;

}
