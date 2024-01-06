# fiap-techchallenge-java-3
Movies Tech Challenge - FIAP

## How to run

- You'll need a Postgres database.
- The script with all the tables needed is inside de `resources/` directory. Please, run it before start the application.
- Check the database configuration inside de `application.properties` file. You probably gonna have to change it.

## How it works
- You can create/delete/update/retrieve a movie or a category.
- You can favorite a movie.
- The system can provide recommendations based on your favorite movies.
- The system can provide statistics about the movies.

## API Documentation

- This project includes a thunder-collection file that can be used with ThunderClient to test this API.

## Health

`GET /health`
Checks if the API is ok.