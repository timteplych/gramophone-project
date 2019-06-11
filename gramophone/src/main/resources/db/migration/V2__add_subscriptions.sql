DROP TABLE IF EXISTS user_subscriptions CASCADE;
CREATE TABLE user_subscriptions
(
    singer_id     INTEGER NOT NULL,
    subscriber_id INTEGER NOT NULL,
    PRIMARY KEY (singer_id, subscriber_id),
    CONSTRAINT FK_SINGER_ID_SUBSCRIBE FOREIGN KEY (singer_id)
        REFERENCES users (id)
        ON DELETE NO ACTION ON UPDATE NO ACTION,
    CONSTRAINT FK_SUBSCRIBER_ID_SINGER FOREIGN KEY (subscriber_id)
        REFERENCES users (id)
        ON DELETE NO ACTION ON UPDATE NO ACTION
);

