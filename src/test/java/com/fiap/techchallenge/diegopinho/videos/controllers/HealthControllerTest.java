package com.fiap.techchallenge.diegopinho.videos.controllers;

import static io.restassured.RestAssured.given;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class HealthControllerTest {

  @LocalServerPort
  private int port;

  @BeforeEach
  public void setup() {
    RestAssured.port = port;
    RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
  }

  @Test
  public void shoudlGetHealth() {
    given()
        .filter(new AllureRestAssured())
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .when()
        .get("/health")
        .then()
        .statusCode(HttpStatus.OK.value());
  }

}
