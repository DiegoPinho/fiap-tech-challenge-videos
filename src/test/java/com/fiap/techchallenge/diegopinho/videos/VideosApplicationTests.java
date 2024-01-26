package com.fiap.techchallenge.diegopinho.videos;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import io.restassured.RestAssured;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class VideosApplicationTests {

	@LocalServerPort
	private int port;

	@BeforeEach
	public void setup() throws Exception {
		RestAssured.port = port;
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
	}

	@Test
	public void shouldStartApplicationCorrectly() {
		RestAssured.given()
				.when()
				.get("/actuator/health")
				.then()
				.statusCode(HttpStatus.OK.value());
	}

}
