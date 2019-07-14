package com.geekbrains.gramophone.entities;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    @Column(name = "avatar")
    private String avatar;




    @Column(name = "activation_code")
    private String activationCode;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "info_singer_id")
    private InfoSinger infoSinger;

    @Column(name = "create_at")
    @CreationTimestamp
    private LocalDateTime createAt;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Collection<Role> roles;

    @OneToMany
    @JoinTable(name = "users_playlists",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "playlist_id"))
    private List<Playlist> playlistList = new ArrayList<>();

    // подписчики
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_subscriptions",
            joinColumns = {@JoinColumn(name = "singer_id")}, //я певец
            inverseJoinColumns = {@JoinColumn(name = "subscriber_id")} //на меня подписаны
    )
    private Set<User> subscribers = new HashSet<>();

    // собственные подписки пользователя
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_subscriptions",
            joinColumns = {@JoinColumn(name = "subscriber_id")}, //я подписан
            inverseJoinColumns = {@JoinColumn(name = "singer_id")} //на вот этих певцов
    )
    private Set<User> subscriptions = new HashSet<>();

    public User() {
    }

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public User(String username, String password, String email, Collection<Role> roles) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.roles = roles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                '}';
    }
}
