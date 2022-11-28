DELETE FROM USER_FRIENDS WHERE USER_FRIENDS_ID <100;
DELETE  FROM USER_LIST WHERE USER_ID<100;


alter table USER_LIST alter column USER_ID restart with 1;
alter table USER_FRIENDS alter column USER_FRIENDS_ID restart with 1;


DELETE  FROM LIKE_FILM WHERE film_ID<100;
alter table LIKE_FILM alter column film_ID restart with 1;
alter table LIKE_FILM alter column user_ID restart with 1;

DELETE  FROM GENRE_FILM WHERE ID_GENRE_FILM<100;
alter table GENRE_FILM alter column film_ID restart with 1;

DELETE  from MPA_FILM WHERE MPA_ID <100;
alter table MPA_FILM alter column MPA_ID restart with 1;

DELETE  FROM FILMS WHERE film_ID<100;
alter table FILMS alter column film_ID restart with 1;