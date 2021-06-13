CREATE TABLE user_dreamjob
(
    id       serial PRIMARY KEY,
    name     varchar(55) UNIQUE,
    email    varchar(55),
    password varchar(55)
);

insert into user_dreamjob values (1, 'Admin', 'root@local', 'root');