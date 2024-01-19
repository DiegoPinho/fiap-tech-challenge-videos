package com.fiap.techchallenge.diegopinho.videos.controllers.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fiap.techchallenge.diegopinho.videos.entities.Category;

import jakarta.validation.constraints.NotBlank;

public class CategoryDTO {

  @JsonProperty
  @NotBlank(message = "name is required and cannot be blank")
  private String name;

  @JsonProperty
  @NotBlank(message = "description is required and cannot be blank")
  private String description;

  public Category toCategory() {
    return new Category(name, description);
  }

  public String getName() {
    return name;
  }

}
