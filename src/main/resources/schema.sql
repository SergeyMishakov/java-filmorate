DROP TABLE IF EXISTS films, users, friend_list, like_list, genre, genre_of_film, rating_MPA;


CREATE TABLE IF NOT EXISTS rating_MPA  (
                                           MPA_id INT PRIMARY KEY,
                                           MPA_name varchar(100)  NOT NULL
);

CREATE TABLE IF NOT EXISTS films (
    film_id      int          PRIMARY KEY AUTO_INCREMENT,
    name         varchar(200) NOT NULL,
    description  varchar(300) NOT NULL,
    release_date date         NOT NULL,
    duration     int          NOT NULL,
    MPA_id       int          NOT NULL
);


CREATE TABLE IF NOT EXISTS users  (
    user_id int  PRIMARY KEY AUTO_INCREMENT ,
    email varchar(200)  NOT NULL ,
    login varchar(200)  NOT NULL ,
    name varchar(200)  NOT NULL ,
    birthday date  NOT NULL
);

CREATE TABLE IF NOT EXISTS friend_list  (
    user_id int  NOT NULL ,
    friend_id int  NOT NULL ,
    confirmed boolean,
    CONSTRAINT pk_friend_list PRIMARY KEY (user_id, friend_id)
);

CREATE TABLE IF NOT EXISTS like_list (
    film_id int  NOT NULL ,
    user_id int  NOT NULL ,
    CONSTRAINT pk_like_list PRIMARY KEY (film_id, user_id)
);

CREATE TABLE IF NOT EXISTS genre (
    genre_id int  PRIMARY KEY AUTO_INCREMENT ,
    genre_name varchar(200)  NOT NULL
);

CREATE TABLE IF NOT EXISTS genre_of_film (
    film_id int  NOT NULL ,
    genre_id int  NOT NULL ,
    CONSTRAINT pk_genre_of_film PRIMARY KEY (film_id,genre_id)
);

ALTER TABLE friend_list ADD CONSTRAINT fk_table1_user_id FOREIGN KEY (user_id) REFERENCES users (user_id);
ALTER TABLE friend_list ADD CONSTRAINT fk_table2_user_id FOREIGN KEY (friend_id) REFERENCES users (user_id);

