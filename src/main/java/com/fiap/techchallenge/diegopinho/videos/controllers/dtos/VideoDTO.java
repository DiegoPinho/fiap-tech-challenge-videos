package com.fiap.techchallenge.diegopinho.videos.controllers.dtos;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fiap.techchallenge.diegopinho.videos.entities.Video;

import jakarta.validation.constraints.NotBlank;

public class VideoDTO {
  @JsonProperty
  @NotBlank(message = "title is required and cannot be blank")
  private String title;

  @JsonProperty
  @NotBlank(message = "description is required and cannot be blank")
  private String description;

  @JsonProperty
  @NotBlank(message = "Link is required and cannot be blank")
  private String link;

  @JsonProperty
  @NotBlank(message = "Publication is required and cannot be blank")
  private String publication;

  public Video toVideo() {
    String pattern = "dd-MM-yyyy";
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
    LocalDateTime localDateTime = LocalDateTime.parse(this.publication, formatter);

    return new Video(this.title, this.description, this.link, localDateTime);
  }
}
