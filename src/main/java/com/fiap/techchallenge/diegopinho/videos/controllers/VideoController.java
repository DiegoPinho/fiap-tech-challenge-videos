package com.fiap.techchallenge.diegopinho.videos.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.fiap.techchallenge.diegopinho.videos.controllers.criterias.VideoCriteria;
import com.fiap.techchallenge.diegopinho.videos.controllers.dtos.VideoDTO;
import com.fiap.techchallenge.diegopinho.videos.entities.Video;
import com.fiap.techchallenge.diegopinho.videos.exceptions.NotFoundException;
import com.fiap.techchallenge.diegopinho.videos.services.RecommendationService;
import com.fiap.techchallenge.diegopinho.videos.services.VideoService;
import com.fiap.techchallenge.diegopinho.videos.utils.DTOValidator;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

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
@RequiredArgsConstructor
public class VideoController {

  private final VideoService videoService;
  private final RecommendationService recommendationService;
  private final DTOValidator validator;

  @GetMapping
  public Mono<ResponseEntity<?>> getAll(VideoCriteria criteria) {
    return Mono.fromSupplier(() -> {
      List<Video> videos = this.videoService.getAll(criteria);
      return ResponseEntity.ok().body(videos);
    });
  }

  @GetMapping("/{id}")
  public Mono<ResponseEntity<?>> getById(@PathVariable("id") Long id) {
    return Mono.fromSupplier(() -> {
      try {
        Video video = this.videoService.getById(id);
        return ResponseEntity.ok().body(video);
      } catch (NotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
      }
    });
  }

  @PostMapping
  public Mono<ResponseEntity<?>> create(@RequestBody VideoDTO videoDTO) {
    return Mono.fromSupplier(() -> {
      Map<Object, Object> violations = validator.check(videoDTO);
      if (!violations.isEmpty()) {
        return ResponseEntity.badRequest().body(violations);
      }

      try {
        Video video = this.videoService.create(videoDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(video);
      } catch (NotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
      }
    });
  }

  @PutMapping("/{id}")
  public Mono<ResponseEntity<?>> update(@RequestBody VideoDTO videoDTO, @PathVariable("id") Long id) {
    return Mono.fromSupplier(() -> {
      try {
        Video video = this.videoService.update(id, videoDTO);
        return ResponseEntity.ok().body(video);
      } catch (NotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
      }
    });
  }

  @DeleteMapping("/{id}")
  public Mono<ResponseEntity<?>> delete(@PathVariable("id") Long id) {
    return Mono.fromSupplier(() -> {
      try {
        this.videoService.delete(id);
        return ResponseEntity.ok().build();
      } catch (NotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
      }
    });
  }

  @GetMapping("/recommend")
  public Mono<ResponseEntity<?>> recommendVideos() {
    return Mono.fromSupplier(() -> {
      List<Video> videos = recommendationService.getRecommendationsBasedOnFavorites();
      return ResponseEntity.ok().body(videos);
    });
  }

  @GetMapping("/play/{id}")
  public Mono<ResponseEntity<?>> playVideo(@PathVariable("id") Long id) {
    return Mono.fromSupplier(() -> {
      try {
        Video video = this.videoService.playVideo(id);
        return ResponseEntity.ok().body("You're now watching... " + video.getTitle());
      } catch (NotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
      }
    });
  }

  @PostMapping("/favorite/{id}")
  public Mono<ResponseEntity<?>> favorite(@PathVariable("id") Long id) {
    return Mono.fromSupplier(() -> {
      try {
        this.videoService.favorite(id);
        return ResponseEntity.ok().build();
      } catch (NotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
      }
    });
  }

  @PostMapping("/unfavorite/{id}")
  public Mono<ResponseEntity<?>> unfavorite(@PathVariable("id") Long id) {
    return Mono.fromSupplier(() -> {
      try {
        this.videoService.unfavorite(id);
        return ResponseEntity.ok().build();
      } catch (NotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
      }
    });
  }
}