package com.fiap.techchallenge.diegopinho.videos.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.fiap.techchallenge.diegopinho.videos.controllers.criterias.VideoCriteria;
import com.fiap.techchallenge.diegopinho.videos.controllers.dtos.VideoDTO;
import com.fiap.techchallenge.diegopinho.videos.entities.Category;
import com.fiap.techchallenge.diegopinho.videos.entities.Video;
import com.fiap.techchallenge.diegopinho.videos.exceptions.NotFoundException;
import com.fiap.techchallenge.diegopinho.videos.repositories.VideoRepository;

@Service
public class VideoService {

  @Autowired
  private VideoRepository videoRepository;

  @Autowired
  private CategoryService categoryService;

  public List<Video> getAll(VideoCriteria criteria) {
    Specification<Video> specification = criteria.toSpecification();
    return this.videoRepository.findAll(specification);
  }

  public Video getById(Long id) {
    return this.videoRepository
        .findById(id)
        .orElseThrow(() -> new NotFoundException("Video Not Found!"));
  }

  public Video create(VideoDTO videoDTO) {
    Video video = videoDTO.toVideo();
    Category category = this.categoryService.getById(videoDTO.getCategoryId());
    video.setCategory(category);

    return this.videoRepository.save(video);
  }

  public void update(Long id, VideoDTO videoDTO) {
    this.getById(id); // checks if exists
    Category category = this.categoryService.getById(videoDTO.getCategoryId()); // checks if exists

    Video video = videoDTO.toVideo();
    video.setId(id);
    video.setCategory(category);

    this.videoRepository.save(video);
  }

  public void delete(Long id) {
    this.getById(id); // checks if exists
    this.videoRepository.deleteById(id);
  }

  public void favorite(Long id) {
    Video video = this.getById(id);
    video.setFavorite(true);

    this.videoRepository.save(video);
  }

  public void unfavorite(Long id) {
    Video video = this.getById(id);
    video.setFavorite(false);

    this.videoRepository.save(video);
  }

  public Video playVideo(Long id) {
    Video video = this.getById(id);
    video.setTimes(video.getTimes() + 1);
    return this.videoRepository.save(video);
  }

}
