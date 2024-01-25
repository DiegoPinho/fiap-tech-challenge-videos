package com.fiap.techchallenge.diegopinho.videos.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fiap.techchallenge.diegopinho.videos.controllers.dtos.StatsDTO;
import com.fiap.techchallenge.diegopinho.videos.services.VideoService;

@RestController
@RequestMapping("/stats")
public class StatsController {

  @Autowired
  private VideoService videoService;

  @GetMapping
  public ResponseEntity<StatsDTO> getStatistics() {
    StatsDTO statisticsFromVideos = this.videoService.getStatisticsFromVideos();
    return ResponseEntity.ok().body(statisticsFromVideos);
  }

}
