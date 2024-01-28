package com.fiap.techchallenge.diegopinho.videos.controllers;

import static org.hamcrest.Matchers.equalTo;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.hasSize;
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

import com.fiap.techchallenge.diegopinho.videos.utils.CategoryHelper;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = { "/clean.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@ActiveProfiles("test")
public class CategoryControllerTest {

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
  public void shouldCreateACategory() {
    var dto = CategoryHelper.generateCategoryDTO();
    given()
        .filter(new AllureRestAssured())
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .body(dto)
        .when()
        .post("/categories")
        .then()
        .statusCode(HttpStatus.CREATED.value())
        .body("$", hasKey("name"))
        .body("$", hasKey("description"))
        .body("name", equalTo(dto.getName()))
        .body("description", equalTo(dto.getDescription()));
  }

  @Test
  @Sql(scripts = { "/clean.sql", "/data.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
  public void shouldNotCreateDuplicatedCategory() {
    var dto = CategoryHelper.generateCategoryDTO();
    dto.setName("name");

    given()
        .filter(new AllureRestAssured())
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .body(dto)
        .when()
        .post("/categories")
        .then()
        .statusCode(HttpStatus.CONFLICT.value());
  }

  @Test
  public void shouldGiveAnErrorWhenBodyIsEmpty() {
    var dto = CategoryHelper.generateCategoryDTO();
    dto.setName(null);
    dto.setDescription(null);

    given()
        .filter(new AllureRestAssured())
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .body(dto)
        .when()
        .post("/categories")
        .then()
        .statusCode(HttpStatus.BAD_REQUEST.value());
  }

  @Test
  @Sql(scripts = { "/clean.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
  public void shouldGivenAnErrorWhenNotFound() {
    Long id = new Random().nextLong();
    given()
        .filter(new AllureRestAssured())
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .when()
        .get("/categories/{id}", id)
        .then()
        .statusCode(HttpStatus.NOT_FOUND.value())
        .body(equalTo("Category Not Found!"));
  }

  @Test
  @Sql(scripts = { "/clean.sql", "/data.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
  public void shouldReturnAllCategories() {
    given()
        .filter(new AllureRestAssured())
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .when()
        .get("/categories")
        .then()
        .statusCode(HttpStatus.OK.value())
        .body("$", hasSize(equalTo(1)));
  }

  @Test
  @Sql(scripts = { "/clean.sql", "/data.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
  public void shouldReturnACategory() {
    given()
        .filter(new AllureRestAssured())
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .when()
        .get("/categories/{id}", 1)
        .then()
        .statusCode(HttpStatus.OK.value())
        .body(matchesJsonSchemaInClasspath("./schemas/CategorySchema.json"));
  }

  @Test
  public void shouldUseFilter() {
    given()
        .filter(new AllureRestAssured())
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .when()
        .get("/categories?name=name")
        .then()
        .statusCode(HttpStatus.OK.value());
  }

  @Test
  @Sql(scripts = { "/clean.sql", "/data.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
  public void shouldAlterACategory() {
    var categoryDTO = CategoryHelper.generateCategoryDTO();
    given()
        .filter(new AllureRestAssured())
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .body(categoryDTO)
        .when()
        .put("/categories/{id}", 1)
        .then()
        .statusCode(HttpStatus.OK.value())
        .body("name", equalTo(categoryDTO.getName()))
        .body("description", equalTo(categoryDTO.getDescription()));
  }

  @Test
  @Sql(scripts = { "/clean.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
  public void shouldGiveAnErrorWhenIdNotFoundForUpdate() {
    var categoryDTO = CategoryHelper.generateCategoryDTO();
    given()
        .filter(new AllureRestAssured())
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .body(categoryDTO)
        .when()
        .put("/categories/{id}", 1)
        .then()
        .statusCode(HttpStatus.NOT_FOUND.value())
        .body(equalTo("Category Not Found!"));
  }

  @Test
  @Sql(scripts = { "/clean.sql", "/data.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
  public void shouldDeleteACategory() {
    given()
        .filter(new AllureRestAssured())
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .when()
        .delete("/categories/{id}", 1)
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
        .delete("/categories/{id}", 1)
        .then()
        .statusCode(HttpStatus.NOT_FOUND.value())
        .body(equalTo("Category Not Found!"));
  }

}
