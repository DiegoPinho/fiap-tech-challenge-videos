package com.fiap.techchallenge.diegopinho.videos.utils;

import java.time.LocalDate;

import com.fiap.techchallenge.diegopinho.videos.controllers.dtos.VideoDTO;
import com.fiap.techchallenge.diegopinho.videos.entities.Category;
import com.fiap.techchallenge.diegopinho.videos.entities.Video;

public class VideoHelper {

  public static Video generateVideo(Category category) {
    return Video.builder()
        .title("Name")
        .description("Description")
        .link("Link")
        .favorite(false)
        .publication(LocalDate.now())
        .category(category)
        .build();
  }

  public static VideoDTO generateVideoDTO(Long categoryId) {
    VideoDTO dto = new VideoDTO();
    dto.setTitle("Title");
    dto.setDescription("Description");
    dto.setLink("Link");
    dto.setCategoryId(categoryId);
    dto.setPublication(LocalDate.now());

    return dto;
  }

}
