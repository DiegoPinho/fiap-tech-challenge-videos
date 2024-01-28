package com.fiap.techchallenge.diegopinho.videos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.config.EnableWebFlux;

@SpringBootApplication
@EnableWebFlux
public class VideosApplication {

	public static void main(String[] args) {
		SpringApplication.run(VideosApplication.class, args);
	}

}
