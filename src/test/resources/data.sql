CREATE TABLE IF NOT EXISTS categories (
  id SERIAL PRIMARY KEY,
  name VARCHAR(255) UNIQUE,
  description VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS videos (
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

--ALTER SEQUENCE categories_id_seq RESTART WITH 1;

INSERT INTO categories (id, name, description) VALUES (1, 'name', 'description');
INSERT INTO videos (id,title, description, link, publication, favorite, category_id, times) VALUES (10, 'title', 'description', 'link', now(), false, 1, 0);