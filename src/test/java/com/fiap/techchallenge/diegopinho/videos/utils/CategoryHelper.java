package com.fiap.techchallenge.diegopinho.videos.utils;

import com.fiap.techchallenge.diegopinho.videos.controllers.dtos.CategoryDTO;
import com.fiap.techchallenge.diegopinho.videos.entities.Category;

public class CategoryHelper {

  public static Category generateCategory() {
    return Category.builder()
        .name("Name")
        .description("Description")
        .build();
  }

  public static CategoryDTO generateCategoryDTO() {
    CategoryDTO categoryDTO = new CategoryDTO();
    categoryDTO.setName("Name");
    categoryDTO.setDescription("Description");

    return categoryDTO;
  }

}
