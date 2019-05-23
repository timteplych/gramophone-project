-- Музыкальные треки
CREATE TABLE tracks ( 
  id                    INT(11) NOT NULL AUTO_INCREMENT,
  title              VARCHAR(50) NOT NULL,
  duration             INT(11) NOT NULL,
  word_author                 VARCHAR(100) NOT NULL,
  music_author                 VARCHAR(100) NOT NULL,
  genre_id                 INT(11) NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT FK_CATEGORY_ID FOREIGN KEY (genre_id)
  REFERENCES genres (id),
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;


-- Жанр песни
CREATE TABLE genres (
  id                    INT(11) NOT NULL AUTO_INCREMENT,
  title                  VARCHAR(50) NOT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;


-- Комментарии
CREATE TABLE comments (
  id                    INT(11) NOT NULL AUTO_INCREMENT,
  content                  VARCHAR(50) NOT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;


-- Комментарии, которые будут оставлять к трекам (связываем трек и комментарий)
CREATE TABLE tracks_comments (
  track_id               INT(11) NOT NULL,
  comment_id               INT(11) NOT NULL,
  PRIMARY KEY (track_id, comment_id),
  CONSTRAINT FK_TRACK_ID_TRACKCOMMENT FOREIGN KEY (track_id)
  REFERENCES tracks (id)
  ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT FK_COMMENT_ID_TRACKCOMMENT FOREIGN KEY (comment_id)
  REFERENCES comments (id)
  ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE = InnoDB DEFAULT CHARSET = utf8;


-- Для учета лайков песен создаем таблицу, которая связывает трек и юзера поставившего лайк
CREATE TABLE tracks_likes (
  track_id               INT(11) NOT NULL,
  user_id               INT(11) NOT NULL,
  PRIMARY KEY (track_id, user_id),
  CONSTRAINT FK_TRACK_ID_FOR_TRACK FOREIGN KEY (track_id)
  REFERENCES tracks (id)
  ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT FK_USER_ID_FOR_TRACK FOREIGN KEY (comment_id)
  REFERENCES users (id)
  ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE = InnoDB DEFAULT CHARSET = utf8;

-- Для учета лайков комментариев, создаем таблицу, которая связывает комментарий и юзера поставившего лайк
CREATE TABLE comments_likes (
  comment_id               INT(11) NOT NULL,
  user_id               INT(11) NOT NULL,
  PRIMARY KEY (comment_id, user_id),
  CONSTRAINT FK_COMMENT_ID_FOR_COMMENT FOREIGN KEY (comment_id)
  REFERENCES comments (id)
  ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT FK_USER_ID_FOR_COMMENT FOREIGN KEY (user_id)
  REFERENCES users (id)
  ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE = InnoDB DEFAULT CHARSET = utf8;