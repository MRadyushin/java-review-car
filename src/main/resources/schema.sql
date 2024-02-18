DROP TABLE IF EXISTS types CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS likes CASCADE;
DROP TABLE IF EXISTS car_types CASCADE;
DROP TABLE IF EXISTS cars CASCADE;
DROP TABLE IF EXISTS klass CASCADE;
DROP TABLE IF EXISTS cars_directors CASCADE;
DROP TABLE IF EXISTS directors CASCADE;
DROP TABLE IF EXISTS feed CASCADE;
DROP TABLE IF EXISTS reviews CASCADE;
DROP TABLE IF EXISTS review_useful CASCADE;

CREATE TABLE IF NOT EXISTS directors
(
    id   identity PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS types
(
    type_id   identity PRIMARY KEY,
    type_name VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS klass
(
    klass_id   identity PRIMARY KEY,
    klass_name VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS users
(
    user_id  identity PRIMARY KEY,
    email    VARCHAR(255) NOT NULL,
    login    VARCHAR(255) NOT NULL,
    name     VARCHAR(255) NOT NULL,
    birthday DATE         NOT NULL,
    UNIQUE(login),
    UNIQUE(email)


);

CREATE TABLE IF NOT EXISTS cars
(
    car_id     identity PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    rate        INTEGER,
    releasedate DATE         NOT NULL,
    price    INTEGER      NOT NULL,
    klass_id      INTEGER,
    FOREIGN KEY (klass_id) REFERENCES klass (klass_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS likes
(
    car_id INTEGER NOT NULL,
    user_id INTEGER NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE,
    FOREIGN KEY (car_id) REFERENCES cars (car_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS car_types
(
    car_id  INTEGER,
    type_id INTEGER,
    FOREIGN KEY (car_id) REFERENCES cars (car_id) ON DELETE CASCADE,
    FOREIGN KEY (type_id) REFERENCES types (type_id) ON DELETE CASCADE
);


CREATE TABLE IF NOT EXISTS cars_directors
(
    car_id     INTEGER,
    director_id INTEGER,
    FOREIGN KEY (car_id) REFERENCES cars (car_id) ON DELETE CASCADE,
    FOREIGN KEY (director_id) REFERENCES directors (id) ON DELETE CASCADE,
    PRIMARY KEY (car_id, director_id)
);

CREATE TABLE IF NOT EXISTS reviews (
  review_id IDENTITY PRIMARY KEY,
  content VARCHAR(255) NOT NULL,
  is_positive BOOLEAN NOT NULL,
  user_id INTEGER NOT NULL,
  car_id INTEGER NOT NULL,
  FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE,
  FOREIGN KEY (car_id) REFERENCES cars (car_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS feed (
    event_id INT AUTO_INCREMENT,
    user_id INT NOT NULL,
    timestamp long NOT NULL,
    event_type VARCHAR(255) NOT NULL,
    operation VARCHAR(255) NOT NULL,
    entity_id INT NOT NULL
);

CREATE TABLE IF NOT EXISTS review_useful (
    review_id INTEGER NOT NULL,
    is_positive BOOLEAN NOT NULL,
    user_id INTEGER NOT NULL,
    UNIQUE(user_id, review_id),
    FOREIGN KEY (review_id) REFERENCES reviews (review_id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE
);
