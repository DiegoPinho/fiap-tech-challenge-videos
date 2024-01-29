# Videos Tech Challenge - FIAP

![Java](https://img.shields.io/badge/Java-ED8B00?style=flat&logo=openjdk&logoColor=white) 
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=flat&logo=spring&logoColor=white) 
![Hibernate](https://img.shields.io/badge/Hibernate-59666C?style=flat&logo=Hibernate&logoColor=white)
![Docker](https://img.shields.io/badge/docker-%230db7ed.svg?style=flat&logo=docker&logoColor=white)
![Postgres](https://img.shields.io/badge/postgres-%23316192.svg?style=flat&logo=postgresql&logoColor=white)
![Markdown](https://img.shields.io/badge/Markdown-000000?style=flat&logo=markdown&logoColor=white)

## How it works
* In this application, you can administrate a Video Streaming (back-end).
  - You can create/delete/update/retrieve (CRUD) a video or/and a category.
  - Each video must have a category.
  - You can favorite/unfavorite a video.
  - The system can provide recommendations based on your favorite videos.
  - The system can provide statistics about the videos.

## How to run
- You can use Docker to run the project, just run `docker compose up -d` on the terminal.
- Otherwise, follow the steps below:
  - You'll need a Postgres database.
  - The script with all the tables needed is inside de `src/main/resources/` folder. Please, run it before start the application.
  - Check the database configuration on the `application.properties` file inside the same folder. You probably gonna have to change it.

## Unit and integration tests
- This project support both kind of tests.
- To run it, just use the `mvn test` command.
- The integration tests use a in memory database (h2). The details about its configution are inside `src/test/resources/application-test.proprties` file.
- All the tests generate a coverage report that can be found in the `target/site` folder.
- Reports about each test itself can be found in the `surefire-reports` folder.

## API Documentation
- This project includes a `thunder-collection.json` file that can be used with ThunderClient to test this API.
- You can check more details about the endpoints below.

### Health
* `GET /health`
  - Checks if the API is ok.

### Categories

* `POST /categories`
  - Create a new category.
  - Expected body:
  ``` js
  {
    "name": string, // unique
    "description": string
  }
  ```
  - Status 422 if body information/format is not valid.

* `GET /categories`
  - List all the categories and its videos.
  - You can filter it by name using query params (ex: ?name="value").

* `GET /categories/:id`
  - Return a specific category.
  - Status 404 if not found.

* `PUT /categories/:id`
  - Changes a specific category.
  - Expected body:
    ``` js
      {
        "name": string, // unique
        "description": string
      }
    ```
  - Status 404 if not found.
  - Status 409 if name conflicts.
  - Status 422 if body information/format is not valid.

* `DELETE /categories/:id`
  - Remove a category and its videos.
  - Status 404 if category not found.


### Videos

* `POST /videos`
  - Create a new video.
  - Expected body:
  ``` js
  {
    "title": string,
    "description": string,
    "link": string,
    "publication": string, // dd-MM-YYYY
    "categoryId": number
  }
  ```
  - Status 404 if category not found.
  - Status 422 if body information/format is not valid.

* `GET /videos`
  - List all the videos.
  - You can filter it by title and publication using query params (ex: ?name="value").

* `GET /videos/:id`
  - Return a specific video.
  - Status 404 if not found.

* `PUT /videos/:id`
  - Changes a specific video.
  - Expected body:
    ``` js
    {
      "title": string,
      "description": string,
      "link": string,
      "publication": string, // dd-MM-YYYY
      "categoryId": number
    }
    ```
  - Status 404 if category not found.
  - Status 422 if body information/format is not valid.

* `DELETE /videos/:id`
  - Remove a category and its videos.
  - Status 404 if video not found.

* `POST /videos/favorite/:id`
  - Favorite a video.
  - Status 404 if not found.

* `POST /videos/favorite/:id`
  - Unfavorite a video.
  - Status 404 if not found.

* `GET /videos/play/:id`
  - "Plays" a video.
  - Status 404 if not found.

* `GET /videos/recommend`
  - Get recommendations based on the last videos watched.
  - Returns an empty array if user saw nothing yet.

### Statistics

* `GET /stats`
  - Return statistics about the videos.
  - The stats are:
    - Number of videos (total);
    - Number of favorite videos;
    - Average time spent watching videos.
