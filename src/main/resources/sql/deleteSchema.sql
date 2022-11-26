DELETE FROM USER_FRIENDS WHERE USERFRIENDS_ID <100;
DELETE  FROM USER_LIST WHERE USER_ID<100;


alter table USER_LIST alter column USER_ID restart with 1;
alter table USER_FRIENDS alter column USERFRIENDS_ID restart with 1;


DELETE  FROM LIKEFILM WHERE film_ID<100;
alter table LIKEFILM alter column film_ID restart with 1;
alter table LIKEFILM alter column user_ID restart with 1;

DELETE  FROM GENRE_FILM WHERE film_ID<100;
alter table GENRE_FILM alter column film_ID restart with 1;

DELETE  from MPA_FILM WHERE MPA_ID <100;
alter table MPA_FILM alter column MPA_ID restart with 1;

DELETE  FROM FILMS WHERE film_ID<100;
alter table FILMS alter column film_ID restart with 1;

DELETE  from GENRE WHERE GENRE_ID <100;
alter table GENRE alter column GENRE_ID restart with 1;

DELETE  from MPA WHERE MPA_ID <100;
alter table MPA alter column MPA_ID restart with 1;