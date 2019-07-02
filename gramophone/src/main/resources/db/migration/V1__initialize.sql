drop table IF EXISTS users CASCADE;
create TABLE users
(
    id              SERIAL,
    username        VARCHAR(50) NOT NULL,
    password        VARCHAR(80) NOT NULL,
    email           VARCHAR(50) NOT NULL,
    activation_code VARCHAR(255),
    info_singer_id  INTEGER,
    avatar          VARCHAR(100),
    create_at       TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);

drop table IF EXISTS info_singers CASCADE;
create TABLE info_singers
(
    id         SERIAL,
    first_name VARCHAR(50),
    last_name  VARCHAR(50),
    phone      VARCHAR(15),
    PRIMARY KEY (id)
);

drop table IF EXISTS roles CASCADE;
create TABLE roles
(
    id   SERIAL,
    name VARCHAR(50) DEFAULT NULL,
    PRIMARY KEY (id)
);

drop table IF EXISTS users_roles CASCADE;
create TABLE users_roles
(
    user_id INTEGER NOT NULL,
    role_id INTEGER NOT NULL,

    PRIMARY KEY (user_id, role_id),
    CONSTRAINT FK_USER_ID_01 FOREIGN KEY (user_id)
        REFERENCES users (id)
        ON delete NO ACTION ON update NO ACTION,
    CONSTRAINT FK_ROLE_ID FOREIGN KEY (role_id)
        REFERENCES roles (id)
        ON delete NO ACTION ON update NO ACTION
);

drop table IF EXISTS genres CASCADE;
create TABLE genres
(
    id    SERIAL,
    title VARCHAR(50) NOT NULL,
    PRIMARY KEY (id)
);

drop table IF EXISTS tracks CASCADE;
create TABLE tracks
(
    id                 SERIAL,
    title              VARCHAR(50)  NOT NULL,
    word_author        VARCHAR(100) NOT NULL,
    music_author       VARCHAR(100) NOT NULL,
    location_on_server VARCHAR(100) NOT NULL,
    download_url       VARCHAR(250) NOT NULL,
    genre_id           INTEGER      NOT NULL,
    create_at          DATE         NOT NULL,
    delete_at          DATE,
    deleted            BOOLEAN DEFAULT false,
    listening_amount   INTEGER,
    user_id            INTEGER      NOT NULL,
    cover              VARCHAR(100),
    PRIMARY KEY (id),
    CONSTRAINT FK_GENRE_ID FOREIGN KEY (genre_id)
        REFERENCES genres (id)
        ON delete NO ACTION ON update NO ACTION,
    CONSTRAINT FK_PERFORMER_ID FOREIGN KEY (user_id)
        REFERENCES users (id)
        ON delete NO ACTION ON update NO ACTION
);

drop table IF EXISTS comments CASCADE;
create TABLE comments
(
    id       SERIAL,
    content  VARCHAR(5000) NOT NULL,
    user_id  INTEGER,
    track_id INTEGER,
    PRIMARY KEY (id),
    CONSTRAINT FK_USER_COMMENT FOREIGN KEY (user_id)
        REFERENCES users (id)
        ON delete NO ACTION ON update NO ACTION,
    CONSTRAINT FK_TRACK_COMMENT FOREIGN KEY (track_id)
        REFERENCES tracks (id)
        ON delete NO ACTION ON update NO ACTION
);

drop table IF EXISTS playlist CASCADE;
create TABLE playlist
(
    id      SERIAL,
    user_id INTEGER     NOT NULL,
    name    VARCHAR(50) NOT NULL DEFAULT 'default',
    PRIMARY KEY (id),
    CONSTRAINT FK_USER_ID_PLAYLIST FOREIGN KEY (user_id)
        REFERENCES users (id)
        ON delete NO ACTION ON update NO ACTION,
    CONSTRAINT UNIQUE_UID_NAME UNIQUE (user_id, name)
);

drop table IF EXISTS playlist_tracks CASCADE;
create TABLE playlist_tracks
(
    playlist_id INTEGER NOT NULL,
    track_id    INTEGER NOT NULL,
    PRIMARY KEY (playlist_id, track_id),
    CONSTRAINT FK_PLAYLIST_ID_TRACKS FOREIGN KEY (playlist_id)
        REFERENCES playlist (id)
        ON delete NO ACTION ON update NO ACTION,
    CONSTRAINT FK_TRACK_ID_TRACKS FOREIGN KEY (track_id)
        REFERENCES tracks (id)
        ON delete NO ACTION ON update NO ACTION
);

drop table IF EXISTS users_playlists CASCADE;
create TABLE users_playlists
(
    user_id     INTEGER NOT NULL,
    playlist_id INTEGER NOT NULL,
    PRIMARY KEY (user_id, playlist_id),
    CONSTRAINT FK_PLAYLISTS_ID_USER FOREIGN KEY (user_id)
        REFERENCES users (id)
        ON delete NO ACTION ON update NO ACTION,
    CONSTRAINT FK_USER_ID_PLAYLISTS FOREIGN KEY (playlist_id)
        REFERENCES playlist (id)
        ON delete NO ACTION ON update NO ACTION
);

drop table IF EXISTS likes CASCADE;
create TABLE likes
(
    id               SERIAL,
    like_type        VARCHAR(50) NOT NULL,
    user_id          INTEGER     NOT NULL,
    target_id        INTEGER     NOT NULL,
    mark  SMALLINT    NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT FK_USER_ID_LIKE FOREIGN KEY (user_id)
        REFERENCES users (id)
        ON delete NO ACTION ON update NO ACTION,
    CONSTRAINT UNIQUE_LIKE_TYPE UNIQUE (like_type, user_id, target_id)
);

insert into roles (name)
values ('ROLE_USER'),
       ('ROLE_MUSICIAN'),
       ('ROLE_ADMIN');

insert into info_singers (first_name, last_name, phone)
values ('Admin', 'Adminoff', '+79881111111');

insert into users (username, password, info_singer_id, email)
values ('admin', '$2a$04$Fx/SX9.BAvtPlMyIIqqFx.hLY2Xp8nnhpzvEEVINvVpwIPbA3v/.i', 1, 'admin@gmail.com');

insert into playlist (user_id)
values (1);


insert into info_singers (first_name, last_name, phone)
values ('Singer', 'Singeroff', '+79881111111');

insert into users (username, password, info_singer_id, email)
values ('singer', '$2a$04$Fx/SX9.BAvtPlMyIIqqFx.hLY2Xp8nnhpzvEEVINvVpwIPbA3v/.i', 2, 'singer@gmail.com');

insert into playlist (user_id)
values (2);


insert into users (username, password, email)
values ('user', '$2a$04$Fx/SX9.BAvtPlMyIIqqFx.hLY2Xp8nnhpzvEEVINvVpwIPbA3v/.i', 'user@gmail.com');

insert into playlist (user_id)
values (3);

insert into users_roles (user_id, role_id)
values (1, 1),
       (1, 2),
       (1, 3),
       (2, 1),
       (2, 2),
       (3, 1);

insert into genres (title)
values ('Блюз'),
       ('Джаз'),
       ('Кантри'),
       ('Попса'),
       ('Реп'),
       ('Ритм-н-блюз'),
       ('Рок'),
       ('Романс'),
       ('Шансон'),
       ('Электронная музыка');