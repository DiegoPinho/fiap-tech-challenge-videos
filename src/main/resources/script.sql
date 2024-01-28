CREATE DATABASE fiap_videos;

CREATE TABLE categories (
  id SERIAL PRIMARY KEY,
  name VARCHAR(255) UNIQUE,
  description VARCHAR(255) NOT NULL
);

CREATE TABLE videos (
  id SERIAL PRIMARY KEY,
  title VARCHAR(255) NOT NULL,
  description TEXT,
  link VARCHAR(255),
  publication TIMESTAMP,
  favorite BOOLEAN DEFAULT FALSE,
  category_id INTEGER,
  times INTEGER,
  FOREIGN KEY (category_id) REFERENCES categories(id)
);