package com.fiap.techchallenge.diegopinho.videos.controllers.dtos;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fiap.techchallenge.diegopinho.videos.entities.Video;

import jakarta.validation.constraints.NotBlank;

public class VideoDTO {

  private static final String VIDEO_DATE_PATTERN = "dd-MM-yyyy";

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
  @JsonFormat(pattern = "dd-MM-yyyy")
  private LocalDate publication;

  public Video toVideo() {
    // DateTimeFormatter formatter =
    // DateTimeFormatter.ofPattern(VIDEO_DATE_PATTERN);
    // LocalDateTime localDateTime = LocalDateTime.parse(this.publication,
    // formatter);

    return new Video(this.title, this.description, this.link, this.publication);
  }
}
