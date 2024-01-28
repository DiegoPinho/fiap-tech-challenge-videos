package com.fiap.techchallenge.diegopinho.videos.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fiap.techchallenge.diegopinho.videos.controllers.dtos.StatsDTO;
import com.fiap.techchallenge.diegopinho.videos.services.VideoService;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/stats")
public class StatsController {

  @Autowired
  private VideoService videoService;

  @GetMapping
  public Mono<ResponseEntity<StatsDTO>> getStatistics() {
    return Mono.fromSupplier(() -> ResponseEntity.ok().body(videoService.getStatisticsFromVideos()));
  }

}
