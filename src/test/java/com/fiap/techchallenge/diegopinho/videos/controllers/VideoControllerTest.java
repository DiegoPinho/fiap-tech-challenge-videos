package com.fiap.techchallenge.diegopinho.videos.controllers;

import static org.hamcrest.Matchers.equalTo;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasKey;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

import java.util.Random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import com.fiap.techchallenge.diegopinho.videos.entities.Category;
import com.fiap.techchallenge.diegopinho.videos.utils.CategoryHelper;
import com.fiap.techchallenge.diegopinho.videos.utils.VideoHelper;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = { "/clean.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@ActiveProfiles("test")
public class VideoControllerTest {

  @LocalServerPort
  private int port;

  @BeforeEach
  public void setup() {
    RestAssured.port = port;
    RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    // RestAssured.filters(new AllureRestAssured()); // desta forma como estamos
    // utilizando nested class gera informação duplicada
  }

  @Test
  @Sql(scripts = { "/clean.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
  public void shouldNotCreateAVideoWhenCategoryNotFound() {
    var category = new Category();
    category.setId(1L);

    var dto = VideoHelper.generateVideoDTO(category.getId());
    given()
        .filter(new AllureRestAssured())
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .body(dto)
        .when()
        .post("/videos")
        .then()
        .statusCode(HttpStatus.NOT_FOUND.value())
        .body(equalTo("Category Not Found!"));
  }

  @Test
  @Sql(scripts = { "/clean.sql", "/data.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
  public void shouldCreateAVideo() {
    var category = new Category();
    category.setId(1L);

    var dto = VideoHelper.generateVideoDTO(category.getId());
    given()
        .filter(new AllureRestAssured())
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .body(dto)
        .when()
        .post("/videos")
        .then()
        .body("$", hasKey("title"))
        .body("$", hasKey("description"))
        .body("$", hasKey("link"))
        .body("title", equalTo(dto.getTitle()))
        .body("description", equalTo(dto.getDescription()))
        .body("link", equalTo(dto.getLink()));
  }

  @Test
  public void shouldGiveAnErrorWhenBodyIsEmpty() {
    given()
        .filter(new AllureRestAssured())
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .when()
        .post("/videos")
        .then()
        .statusCode(HttpStatus.BAD_REQUEST.value());
  }

  @Test
  @Sql(scripts = { "/clean.sql", "/data.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
  public void shouldReturnAVideo() {
    given()
        .filter(new AllureRestAssured())
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .when()
        .get("/videos/{id}", 10)
        .then()
        .statusCode(HttpStatus.OK.value())
        .body(matchesJsonSchemaInClasspath("./schemas/VideoSchema.json"));
  }

  @Test
  @Sql(scripts = { "/clean.sql", "/data.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
  public void shouldAlterAVideo() {
    var videoDTO = VideoHelper.generateVideoDTO(1L);
    given()
        .filter(new AllureRestAssured())
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .body(videoDTO)
        .when()
        .put("/videos/{id}", 10)
        .then()
        .statusCode(HttpStatus.OK.value())
        .body("title", equalTo(videoDTO.getTitle()))
        .body("description", equalTo(videoDTO.getDescription()));
  }

  @Test
  @Sql(scripts = { "/clean.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
  public void shouldGiveAnErrorWhenIdNotFoundForUpdate() {
    var videoDTO = VideoHelper.generateVideoDTO(1L);
    given()
        .filter(new AllureRestAssured())
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .body(videoDTO)
        .when()
        .put("/videos/{id}", 1)
        .then()
        .statusCode(HttpStatus.NOT_FOUND.value())
        .body(equalTo("Video Not Found!"));
  }

  @Test
  @Sql(scripts = { "/clean.sql", "/data.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
  public void shouldDeleteAVideo() {
    given()
        .filter(new AllureRestAssured())
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .when()
        .delete("/videos/{id}", 10)
        .then()
        .statusCode(HttpStatus.OK.value());
  }

  @Test
  @Sql(scripts = { "/clean.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
  public void shouldGiveAnErrorWhenIdNotFoundForDelete() {
    given()
        .filter(new AllureRestAssured())
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .when()
        .delete("/videos/{id}", 1)
        .then()
        .statusCode(HttpStatus.NOT_FOUND.value())
        .body(equalTo("Video Not Found!"));
  }

}
