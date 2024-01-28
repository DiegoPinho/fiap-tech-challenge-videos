package com.fiap.techchallenge.diegopinho.videos.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/health")
public class HealthController {

  @GetMapping
  public Mono<String> status() {
    return Mono.just("It's working!");
  }

}
