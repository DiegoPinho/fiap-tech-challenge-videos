CREATE DATABASE fiap_techchallenge_3;

CREATE TABLE movies (
  id SERIAL PRIMARY KEY,
  title VARCHAR(255) NOT NULL,
  description TEXT,
  link VARCHAR(255),
  publication TIMESTAMP
);