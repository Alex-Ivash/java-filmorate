INSERT INTO mpa(name) VALUES
('G'),
('PG');

INSERT INTO users(email, login, name, birthday) VALUES
('foo@bar.com', 'fooBar', 'Alex', '1998-04-23'),
('he@eh.ee', 'hee', 'hhe', '1966-11-02'),
('john.doe@example.com', 'johndoe', 'John Doe', '1990-01-01'),
('tony.stark@avengers.com', 'ironman', 'Tony Stark', '1970-05-29');

INSERT INTO films(name, description, release_date, duration, mpa) VALUES
('Inception', 'A thief who steals corporate secrets through the use of dream-sharing technology.', '2010-07-16', 148, 1),
('The Matrix', 'A computer hacker learns from mysterious rebels about the true nature of his reality.', '1999-03-31', 136, 2),
('Interstellar', 'A team of explorers travel through a wormhole in space in an attempt to ensure humanity’s survival.', '2014-11-07', 148, 1);

INSERT INTO films_likes(film_id, user_id) VALUES
(1, 1),
(1, 2),
(2, 2);

INSERT INTO genres(name) VALUES
('Комедия'),
('Драма'),
('Мультфильм'),
('Триллер');

INSERT INTO films_genres(film_id, genre_id) VALUES
(1, 1),
(1, 2),
(2, 2);

INSERT INTO users_friendships(user_id, friend_id) VALUES
(1, 2),
(1, 3),
(2, 3);
