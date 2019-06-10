DROP TABLE IF EXISTS users;
CREATE TABLE users
(
    id          SERIAL,
    username    VARCHAR(50) NOT NULL,
    password    VARCHAR(80) NOT NULL,
    first_name  VARCHAR(50),
    last_name   VARCHAR(50),
    singer      BOOL        NOT NULL,
    email       VARCHAR(50) NOT NULL,
    phone       VARCHAR(15),
    avatar      VARCHAR(100),
    playlist_id INTEGER,
    PRIMARY KEY (id)
);

DROP TABLE IF EXISTS roles;
CREATE TABLE roles
(
    id   SERIAL,
    name VARCHAR(50) DEFAULT NULL,
    PRIMARY KEY (id)
);

DROP TABLE IF EXISTS users_roles;
CREATE TABLE users_roles
(
    user_id INTEGER NOT NULL,
    role_id INTEGER NOT NULL,

    PRIMARY KEY (user_id, role_id),

--  KEY FK_ROLE_idx (role_id),

    CONSTRAINT FK_USER_ID_01 FOREIGN KEY (user_id)
        REFERENCES users (id)
        ON DELETE NO ACTION ON UPDATE NO ACTION,

    CONSTRAINT FK_ROLE_ID FOREIGN KEY (role_id)
        REFERENCES roles (id)
        ON DELETE NO ACTION ON UPDATE NO ACTION
);

DROP TABLE IF EXISTS genres;
CREATE TABLE genres
(
    id    SERIAL,
    title VARCHAR(50) NOT NULL,
    PRIMARY KEY (id)
);

DROP TABLE IF EXISTS tracks;
CREATE TABLE tracks
(
    id                 SERIAL,
    title              VARCHAR(50)  NOT NULL,
    word_author        VARCHAR(100) NOT NULL,
    music_author       VARCHAR(100) NOT NULL,
    location_on_server VARCHAR(100) NOT NULL,
    genre_id           INTEGER      NOT NULL,
    create_at          DATE         NOT NULL,
    listening_amount   INTEGER,
    performer          VARCHAR(50)  NOT NULL,
    cover              VARCHAR(100),
    PRIMARY KEY (id),
    CONSTRAINT FK_GENRE_ID FOREIGN KEY (genre_id)
        REFERENCES genres (id)
        ON DELETE NO ACTION ON UPDATE NO ACTION
);

DROP TABLE IF EXISTS comments;
CREATE TABLE comments
(
    id       SERIAL,
    content  VARCHAR(5000) NOT NULL,
    user_id  INTEGER,
    track_id INTEGER,
    PRIMARY KEY (id),
    CONSTRAINT FK_USER_COMMENT FOREIGN KEY (user_id)
        REFERENCES users (id)
        ON DELETE NO ACTION ON UPDATE NO ACTION,
    CONSTRAINT FK_TRACK_COMMENT FOREIGN KEY (track_id)
        REFERENCES tracks (id)
        ON DELETE NO ACTION ON UPDATE NO ACTION
);

DROP TABLE IF EXISTS tracks_likes;
CREATE TABLE tracks_likes
(
    track_id INTEGER NOT NULL,
    user_id  INTEGER NOT NULL,
    PRIMARY KEY (track_id, user_id),
    CONSTRAINT FK_TRACK_ID_FOR_TRACK FOREIGN KEY (track_id)
        REFERENCES tracks (id)
        ON DELETE NO ACTION ON UPDATE NO ACTION,
    CONSTRAINT FK_USER_ID_FOR_TRACK FOREIGN KEY (user_id)
        REFERENCES users (id)
        ON DELETE NO ACTION ON UPDATE NO ACTION
);

DROP TABLE IF EXISTS comments_likes;
CREATE TABLE comments_likes
(
    comment_id INTEGER NOT NULL,
    user_id    INTEGER NOT NULL,
    PRIMARY KEY (comment_id, user_id),
    CONSTRAINT FK_COMMENT_ID_FOR_COMMENT FOREIGN KEY (comment_id)
        REFERENCES comments (id)
        ON DELETE NO ACTION ON UPDATE NO ACTION,
    CONSTRAINT FK_USER_ID_FOR_COMMENT FOREIGN KEY (user_id)
        REFERENCES users (id)
        ON DELETE NO ACTION ON UPDATE NO ACTION
);

DROP TABLE IF EXISTS playlist;
CREATE TABLE playlist
(
    id   SERIAL,
    name VARCHAR(50) NOT NULL,
    PRIMARY KEY (id)
);

DROP TABLE IF EXISTS playlist_tracks;
CREATE TABLE playlist_tracks
(
    playlist_id INTEGER NOT NULL,
    track_id    INTEGER NOT NULL,
    PRIMARY KEY (playlist_id, track_id),
    CONSTRAINT FK_PLAYLIST_ID_TRACKS FOREIGN KEY (playlist_id)
        REFERENCES playlist (id)
        ON DELETE NO ACTION ON UPDATE NO ACTION,
    CONSTRAINT FK_TRACK_ID_TRACKS FOREIGN KEY (track_id)
        REFERENCES tracks (id)
        ON DELETE NO ACTION ON UPDATE NO ACTION
);

INSERT INTO roles (name)
VALUES ('ROLE_USER'),
       ('ROLE_MUSICIAN'),
       ('ROLE_ADMIN');


INSERT INTO playlist(name)
VALUES ('default');

INSERT INTO users (username, password, first_name, last_name, singer, email, phone, playlist_id)
VALUES ('admin', '$2a$04$Fx/SX9.BAvtPlMyIIqqFx.hLY2Xp8nnhpzvEEVINvVpwIPbA3v/.i', 'Admin', 'Admin', true,
        'admin@gmail.com', '+79881111111', 1);

INSERT INTO playlist(name)
VALUES ('default');

INSERT INTO users (username, password, first_name, last_name, singer, email, phone, playlist_id)
VALUES ('singer', '$2a$04$Fx/SX9.BAvtPlMyIIqqFx.hLY2Xp8nnhpzvEEVINvVpwIPbA3v/.i', 'Singer', 'Singer', true,
        'singer@gmail.com', '+79881111111', 2);

INSERT INTO playlist(name)
VALUES ('default');

INSERT INTO users (username, password, first_name, last_name, singer, email, phone, playlist_id)
VALUES ('user', '$2a$04$Fx/SX9.BAvtPlMyIIqqFx.hLY2Xp8nnhpzvEEVINvVpwIPbA3v/.i', 'User', 'User', false,
        'user@gmail.com', '+79881111111', 3);

INSERT INTO users_roles (user_id, role_id)
VALUES (1, 1),
       (1, 2),
       (1, 3),
       (2, 1),
       (2, 2),
       (3, 1);

INSERT INTO genres (title)
VALUES ('Попса'),
       ('Реп'),
       ('Шансон'),
       ('Рок');