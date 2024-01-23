package com.fiap.techchallenge.diegopinho.videos.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.fiap.techchallenge.diegopinho.videos.controllers.criterias.VideoCriteria;
import com.fiap.techchallenge.diegopinho.videos.controllers.dtos.VideoDTO;
import com.fiap.techchallenge.diegopinho.videos.entities.Video;
import com.fiap.techchallenge.diegopinho.videos.exceptions.NotFoundException;
import com.fiap.techchallenge.diegopinho.videos.services.RecommendationService;
import com.fiap.techchallenge.diegopinho.videos.services.VideoService;
import com.fiap.techchallenge.diegopinho.videos.utils.DTOValidator;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/videos")

public class VideoController {

  @Autowired
  private VideoService videoService;

  @Autowired
  private RecommendationService recommendationService;

  @Autowired
  private DTOValidator validator;

  @GetMapping
  public ResponseEntity<?> getAll(VideoCriteria criteria) {
    List<Video> videos = this.videoService.getAll(criteria);
    return ResponseEntity.ok().body(videos);
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> getById(@PathVariable("id") Long id) {
    try {
      Video video = this.videoService.getById(id);
      System.out.println(video);
      return ResponseEntity.ok().body(video);
    } catch (NotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
  }

  @PostMapping
  public ResponseEntity<?> create(@RequestBody VideoDTO videoDTO) {
    Map<Object, Object> violations = validator.check(videoDTO);
    if (!violations.isEmpty()) {
      return ResponseEntity.badRequest().body(violations);
    }

    // System.out.println(videoDTO);

    Video video = this.videoService.create(videoDTO);
    return ResponseEntity.status(HttpStatus.CREATED).body(video);
  }

  @PutMapping("/{id}")
  public ResponseEntity<?> update(@RequestBody VideoDTO videoDTO, @PathVariable("id") Long id) {
    try {
      this.videoService.update(id, videoDTO);
      return ResponseEntity.ok().build();
    } catch (NotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> delete(@PathVariable("id") Long id) {
    try {
      this.videoService.delete(id);
      return ResponseEntity.ok().build();
    } catch (NotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cannot remove resource in use.");
    }
  }

  @GetMapping("/recommend")
  public ResponseEntity<?> recommendVideos() {
    List<Video> videos = recommendationService.getRecommendationsBasedOnFavorites();
    return ResponseEntity.ok().body(videos);
  }

  @GetMapping("/play/{id}")
  public ResponseEntity<?> playVideo(@PathVariable("id") Long id) {
    try {
      Video video = this.videoService.playVideo(id);
      return ResponseEntity.ok().body("You're now watching... " + video.getTitle());
    } catch (NotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
  }

  @PostMapping("/favorite/{id}")
  public ResponseEntity<?> favorite(@PathVariable("id") Long id) {
    try {
      this.videoService.favorite(id);
      return ResponseEntity.ok().build();
    } catch (NotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
  }

  @PostMapping("/unfavorite/{id}")
  public ResponseEntity<?> unfavorite(@PathVariable("id") Long id) {
    try {
      this.videoService.unfavorite(id);
      return ResponseEntity.ok().build();
    } catch (NotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
  }

}
