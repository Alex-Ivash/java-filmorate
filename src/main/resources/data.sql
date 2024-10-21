-- Инициализация таблицы ratings
MERGE INTO mpa AS r
USING (VALUES
    ('G'),
    ('PG'),
    ('PG-13'),
    ('R'),
    ('NC-17')
) AS v(name)
ON r.name = v.name
WHEN NOT MATCHED THEN
INSERT (name) VALUES (v.name);

-- Инициализация таблицы users
MERGE INTO users AS u
USING (VALUES
    ('john.doe@example.com', 'johndoe', 'John Doe', '1990-01-01'),
    ('jane.smith@example.com', 'janesmith', 'Jane Smith', '1992-05-15'),
    ('bob.brown@example.com', 'bobbrown', 'Bob Brown', '1985-11-30')
) AS v(email, login, name, birthday)
ON u.email = v.email
WHEN NOT MATCHED THEN
INSERT (email, login, name, birthday) VALUES (v.email, v.login, v.name, v.birthday);

-- Инициализация таблицы films
MERGE INTO films AS f
USING (VALUES
    ('Inception', 'A thief who steals corporate secrets through the use of dream-sharing technology.', '2010-07-16', 148, 1),
    ('The Matrix', 'A computer hacker learns from mysterious rebels about the true nature of his reality.', '1999-03-31', 136, 2),
    ('Interstellar', 'A team of explorers travel through a wormhole in space in an attempt to ensure humanity’s survival.', '2014-11-07', 148, 1)
) AS v(name, description, release_date, duration, mpa)
ON f.name = v.name AND f.description = v.description AND f.release_date = v.release_date AND f.duration = v.duration AND f.mpa = v.mpa
WHEN NOT MATCHED THEN
INSERT (name, description, release_date, duration, mpa) VALUES (v.name, v.description, v.release_date, v.duration, v.mpa);

-- Инициализация таблицы films_likes
MERGE INTO films_likes AS fl
USING (VALUES
    (1, 1),  -- John Doe likes Inception
    (1, 2),  -- Jane Smith likes Inception
    (2, 1),  -- John Doe likes The Matrix
    (3, 3)   -- Bob Brown likes Interstellar
) AS v(film_id, user_id)
ON fl.film_id = v.film_id AND fl.user_id = v.user_id
WHEN NOT MATCHED THEN
INSERT (film_id, user_id) VALUES (v.film_id, v.user_id);

-- Инициализация таблицы users_friendships
MERGE INTO users_friendships AS uf
USING (VALUES -- я тут понимаю, что идентифицировать дружбу можно было бы и одной единственной записью, но тогда запросы получаются переусложненными.
    (1, 2),
    (2, 3)
) AS v(user_id, friend_id)
ON uf.user_id = v.user_id AND uf.friend_id = v.friend_id
WHEN NOT MATCHED THEN
INSERT (user_id, friend_id) VALUES (v.user_id, v.friend_id);

-- Инициализация таблицы genres
MERGE INTO genres AS g
USING (VALUES
    ('Комедия'),
    ('Драма'),
    ('Мультфильм'),
    ('Триллер'),
    ('Документальный'),
    ('Боевик')
) AS v(name)
ON g.name = v.name
WHEN NOT MATCHED THEN
INSERT (name) VALUES (v.name);

-- Инициализация таблицы films_genres
MERGE INTO films_genres AS fg
USING (VALUES
    (1, 2),  -- Inception is Sci-Fi
    (1, 4),  -- Inception is Thriller
    (2, 2),  -- The Matrix is Sci-Fi
    (3, 2),  -- Interstellar is Sci-Fi
    (3, 3)   -- Interstellar is Drama
) AS v(film_id, genre_id)
ON fg.film_id = v.film_id AND fg.genre_id = v.genre_id
WHEN NOT MATCHED THEN
INSERT (film_id, genre_id) VALUES (v.film_id, v.genre_id);
