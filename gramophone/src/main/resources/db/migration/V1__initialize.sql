SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS users;

CREATE TABLE users
(
    id         INT(11)     NOT NULL AUTO_INCREMENT,
    username   VARCHAR(50) NOT NULL,
    password   CHAR(80)    NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name  VARCHAR(50) NOT NULL,
    singer     BOOL        NOT NULL,
    email      VARCHAR(50) NOT NULL,
    phone      VARCHAR(15) NOT NULL,
    avatar     VARCHAR(50),
    PRIMARY KEY (id)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS roles;

CREATE TABLE roles
(
    id   INT(11) NOT NULL AUTO_INCREMENT,
    name VARCHAR(50) DEFAULT NULL,
    PRIMARY KEY (id)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS users_roles;

CREATE TABLE users_roles
(
    user_id INT(11) NOT NULL,
    role_id INT(11) NOT NULL,

    PRIMARY KEY (user_id, role_id),

--  KEY FK_ROLE_idx (role_id),

    CONSTRAINT FK_USER_ID_01 FOREIGN KEY (user_id)
        REFERENCES users (id)
        ON DELETE NO ACTION ON UPDATE NO ACTION,

    CONSTRAINT FK_ROLE_ID FOREIGN KEY (role_id)
        REFERENCES roles (id)
        ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

INSERT INTO roles (name)
VALUES ('ROLE_USER'),
       ('ROLE_MUSICIAN'),
       ('ROLE_ADMIN');


INSERT INTO users (username, password, first_name, last_name, singer, email, phone)
VALUES ('admin', '$2a$04$Fx/SX9.BAvtPlMyIIqqFx.hLY2Xp8nnhpzvEEVINvVpwIPbA3v/.i', 'Admin', 'Admin', true,
        'admin@gmail.com', '+79881111111');

INSERT INTO users_roles (user_id, role_id)
VALUES (1, 1),
       (1, 2),
       (1, 3);


CREATE TABLE tracks
(
    id                 INT(11)      NOT NULL AUTO_INCREMENT,
    title              VARCHAR(50)  NOT NULL,
    word_author        VARCHAR(100) NOT NULL,
    music_author       VARCHAR(100) NOT NULL,
    location_on_server VARCHAR(100) NOT NULL,
    genre_id           INT(11)      NOT NULL,
    create_at          DATE         NOT NULL,
    listening_amount   INT(11),
    user_id            INT(11)      NOT NULL,
    cover              VARCHAR(50),
    PRIMARY KEY (id),
    CONSTRAINT FK_GENRE_ID FOREIGN KEY (genre_id)
        REFERENCES genres (id)
        ON DELETE NO ACTION ON UPDATE NO ACTION,
    CONSTRAINT FK_USER_ID FOREIGN KEY (user_id)
        REFERENCES users (id)
        ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8;


CREATE TABLE genres
(
    id    INT(11)     NOT NULL AUTO_INCREMENT,
    title VARCHAR(50) NOT NULL,
    PRIMARY KEY (id)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8;


CREATE TABLE comments
(
    id      INT(11)     NOT NULL AUTO_INCREMENT,
    content VARCHAR(50) NOT NULL,
    user_id INT(11),
    PRIMARY KEY (id),
    CONSTRAINT FK_USER_COMMENT FOREIGN KEY (user_id)
        REFERENCES users (id)
        ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8;


CREATE TABLE tracks_comments
(
    track_id   INT(11) NOT NULL,
    comment_id INT(11) NOT NULL,
    PRIMARY KEY (track_id, comment_id),
    CONSTRAINT FK_TRACK_ID_TRACK_COMMENT FOREIGN KEY (track_id)
        REFERENCES tracks (id)
        ON DELETE NO ACTION ON UPDATE NO ACTION,
    CONSTRAINT FK_COMMENT_ID_TRACK_COMMENT FOREIGN KEY (comment_id)
        REFERENCES comments (id)
        ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;


CREATE TABLE tracks_likes
(
    track_id INT(11) NOT NULL,
    user_id  INT(11) NOT NULL,
    PRIMARY KEY (track_id, user_id),
    CONSTRAINT FK_TRACK_ID_FOR_TRACK FOREIGN KEY (track_id)
        REFERENCES tracks (id)
        ON DELETE NO ACTION ON UPDATE NO ACTION,
    CONSTRAINT FK_USER_ID_FOR_TRACK FOREIGN KEY (user_id)
        REFERENCES users (id)
        ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;


CREATE TABLE comments_likes
(
    comment_id INT(11) NOT NULL,
    user_id    INT(11) NOT NULL,
    PRIMARY KEY (comment_id, user_id),
    CONSTRAINT FK_COMMENT_ID_FOR_COMMENT FOREIGN KEY (comment_id)
        REFERENCES comments (id)
        ON DELETE NO ACTION ON UPDATE NO ACTION,
    CONSTRAINT FK_USER_ID_FOR_COMMENT FOREIGN KEY (user_id)
        REFERENCES users (id)
        ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;


CREATE TABLE playlist
(
    track_id INT(11) NOT NULL,
    user_id  INT(11) NOT NULL,
    PRIMARY KEY (track_id, user_id),
    CONSTRAINT FK_TRACK_ID_PLAYLIST FOREIGN KEY (track_id)
        REFERENCES tracks (id)
        ON DELETE NO ACTION ON UPDATE NO ACTION,
    CONSTRAINT FK_USER_ID_PLAYLIST FOREIGN KEY (user_id)
        REFERENCES users (id)
        ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;


INSERT INTO genres (title)
VALUES ('Попса'),
       ('Реп'),
       ('Шансон'),
       ('Рок');