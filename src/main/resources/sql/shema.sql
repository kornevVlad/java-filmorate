--ФИЛЬМ
create table FILMS
(
    FILM_ID      INTEGER auto_increment
        unique,
    FILM_NAME    CHARACTER VARYING(50)  not null,
    DESCRIPTION  CHARACTER VARYING(201) not null,
    RELEASE_DATE DATE                   not null,
    DURATION     INTEGER                not null,
    MPA          CHARACTER VARYING(40),
    constraint FILM_PK
        primary key (FILM_ID)
);

--ЖАНР
create table GENRE
(
    GENRE_ID   INTEGER auto_increment
        unique,
    GENRE_NAME CHARACTER VARYING(40) not null,
    constraint "CENRE_pk"
        primary key (GENRE_ID)
);

--MPA
create table MPA
(
    MPA_ID   INTEGER auto_increment
        unique,
    MPA_NAME CHARACTER VARYING(40) not null,
    constraint "MPA_pk"
        primary key (MPA_ID)
);

--ЖАНР_ФИЛЬМА
create table GENRE_FILM
(
    FILM_ID  INTEGER auto_increment,
    GENRE_ID INTEGER not null,
    constraint "GENRE_FILM_FILMS_null_fk"
        foreign key (FILM_ID) references FILMS,
    constraint "GENRE_FILM_GENRE_null_fk"
        foreign key (GENRE_ID) references GENRE
);

--MPA_ФИЛЬМА
create table MPA_FILM
(
    FILM_ID INTEGER not null,
    MPA_ID  INTEGER not null,
    constraint MPA_FILM_FILMS_FILM_ID_FK
        foreign key (FILM_ID) references FILMS (FILM_ID),
    constraint "mpa_film_MPA_null_fk"
        foreign key (MPA_ID) references MPA (MPA_ID)
);

--ПОЛЬЗОВАТЕЛЬ
CREATE TABLE  USER_LIST
(
    USER_ID   INTEGER auto_increment
        unique,
    EMAIL     CHARACTER VARYING(70) not null,
    LOGIN     CHARACTER VARYING(70) not null
        unique,
    USER_NAME CHARACTER VARYING(70) not null,
    BIRTHDAY  DATE                  not null,
    constraint USER_PK
        primary key (USER_ID)
);

--ДРУЗЬЯ_ПОЛЬЗОВАТЕЛЯ
create table USER_FRIENDS
(
    USERFRIENDS_ID   INTEGER auto_increment
        unique,
    FRIEND_ID        INTEGER not null,
    USER_ID          INTEGER not null,
    constraint USER_FRIENDS_PK
        primary key (USERFRIENDS_ID),
    constraint USER_FRIENDS_USER_LIST_NULL_FK
        foreign key (USER_ID) references USER_LIST
);

--ЛАЙКИ
create table LIKEFILM
(
    FILM_ID INTEGER auto_increment,
    USER_ID INTEGER auto_increment,
    constraint LIKEFILM_COPY_6_2_CONSTRAINT_7
        primary key (USER_ID),
    constraint "LIKEFILM_COPY_6_2_likefilm_FILMS_null_fk"
        foreign key (FILM_ID) references FILMS,
    constraint "LIKEFILM_COPY_6_2_likefilm_USER_LIST_null_fk"
        foreign key (USER_ID) references USER_LIST
);

--ЗАПОЛНЕНИЕ ДАННЫХ
--ДАННЫЕ MPA
INSERT INTO MPA (MPA_NAME) VALUES ('G');
INSERT INTO MPA (MPA_NAME) VALUES ('PG');
INSERT INTO MPA (MPA_NAME) VALUES ('PG-13');
INSERT INTO MPA (MPA_NAME) VALUES ('R');
INSERT INTO MPA (MPA_NAME) VALUES ('NC-17');

--ДАННЫЕ ЖАНРА
INSERT INTO GENRE (GENRE_NAME) VALUES ('Комедия');
INSERT INTO GENRE (GENRE_NAME) VALUES ('Драма');
INSERT INTO GENRE (GENRE_NAME) VALUES ('Мультфильм');
INSERT INTO GENRE (GENRE_NAME) VALUES ('Триллер');
INSERT INTO GENRE (GENRE_NAME) VALUES ('Документальный');
INSERT INTO GENRE (GENRE_NAME) VALUES ('Боевик');