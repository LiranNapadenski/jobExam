CREATE TABLE IF NOT EXISTS task (
                                    description VARCHAR(64) NOT NULL,
    completed   VARCHAR(30) NOT NULL);

CREATE TABLE IF NOT EXISTS movies (
    id SERIAL PRIMARY KEY,
    title VARCHAR(30) NOT NULL,
    genere VARCHAR(30) NOT NULL,
    duration INT NOT NULL,
    rating FLOAT NOT NULL DEFAULT 0,
    release_year INT NOT NULL
);

CREATE TABLE IF NOT EXISTS showtimes (
    id SERIAL PRIMARY KEY,
    theater VARCHAR(50) NOT NULL,
    start_time TIMESTAMPTZ NOT NULL,
    end_time TIMESTAMPTZ NOT NULL,
    price FLOAT NOT NULL,
    movie_id INT REFERENCES movies(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS tickets (
    id SERIAL PRIMARY KEY,
    seat_number INT NOT NULL,
    showtime_id INT REFERENCES showtimes(id) ON DELETE CASCADE,
    user_id VARCHAR(255) NOT NULL,
    movie_id_son INT REFERENCES movies(id) ON DELETE CASCADE,
    CONSTRAINT unique_seat_per_showtime UNIQUE (seat_number, showtime_id)
);
