-- v1.0.0_1 - First version of database (basic tables)

-- Bot config

DROP TABLE IF EXISTS t_configuration;
CREATE TABLE IF NOT EXISTS t_configuration
(
    conf_key   TEXT NOT NULL,
    conf_value TEXT NOT NULL,
    PRIMARY KEY (conf_key)
);

-- Basic tables

DROP TABLE IF EXISTS t_user;
CREATE TABLE IF NOT EXISTS t_user
(
    id     INTEGER NOT NULL,
    name   TEXT    NOT NULL,
    active INTEGER DEFAULT 1,
    PRIMARY KEY (id)
);

CREATE INDEX user_idx_id ON t_user (id);

DROP TABLE IF EXISTS t_room;
CREATE TABLE IF NOT EXISTS t_room
(
    id       INTEGER NOT NULL,
    name     TEXT    NOT NULL,
    owner_id INTEGER NOT NULL,
    active   INTEGER DEFAULT 1,
    PRIMARY KEY (id),
    FOREIGN KEY (owner_id) REFERENCES t_user (id)
);

CREATE INDEX room_idx_id ON t_room (id);
CREATE INDEX room_owner_idx_id ON t_room (owner_id);

DROP TABLE IF EXISTS t_dictionary;
CREATE TABLE IF NOT EXISTS t_dictionary
(
    id         INTEGER NOT NULL,
    name       TEXT    NOT NULL UNIQUE,
    creator_id INTEGER NOT NULL,
    published  INTEGER DEFAULT 0,
    shared     INTEGER DEFAULT 0,
    PRIMARY KEY (id),
    FOREIGN KEY (creator_id) REFERENCES t_user (id)
);

CREATE INDEX dictionary_idx_id ON t_dictionary (id);
CREATE INDEX dictionary_creator_idx_id ON t_dictionary (creator_id);

DROP TABLE IF EXISTS t_card;
CREATE TABLE IF NOT EXISTS t_card
(
    id            INTEGER NOT NULL,
    text          TEXT    NOT NULL,
    type          INTEGER NOT NULL,
    dictionary_id INTEGER NOT NULL,
    PRIMARY KEY (id),
    UNIQUE (text, type, dictionary_id),
    FOREIGN KEY (dictionary_id) REFERENCES t_dictionary (id)
);

CREATE INDEX card_idx_id ON t_card (id);
CREATE INDEX card_dictionary_idx_id ON t_card (dictionary_id);

-- Tables with inheritance

DROP TABLE IF EXISTS t_game;
CREATE TABLE IF NOT EXISTS t_game
(
    id             INTEGER NOT NULL,
    room_id        INTEGER NOT NULL,
    creator_id     INTEGER NOT NULL,
    status         INTEGER NOT NULL,
    type           INTEGER NOT NULL,
    dictionary_id  INTEGER NOT NULL,
    n_cards_to_win INTEGER NOT NULL,
    n_players      INTEGER,
    PRIMARY KEY (id),
    FOREIGN KEY (room_id) REFERENCES t_room (id),
    FOREIGN KEY (creator_id) REFERENCES t_user (id),
    FOREIGN KEY (dictionary_id) REFERENCES t_dictionary (id),
    UNIQUE (room_id)
);

CREATE INDEX game_idx_id ON t_game (id);
CREATE INDEX game_room_idx_id ON t_game (room_id);
CREATE INDEX game_creator_idx_id ON t_game (creator_id);

DROP TABLE IF EXISTS t_player;
CREATE TABLE IF NOT EXISTS t_player
(
    id         INTEGER NOT NULL,
    user_id    INTEGER NOT NULL,
    game_id    INTEGER NOT NULL,
    join_order INTEGER NOT NULL,
    points     INTEGER DEFAULT 0,
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES t_user (id),
    FOREIGN KEY (game_id) REFERENCES t_game (id),
    UNIQUE (user_id)
);

CREATE INDEX player_idx_id ON t_player (id);
CREATE INDEX player_user_idx_id ON t_player (user_id);
CREATE INDEX player_game_idx_id ON t_player (game_id);