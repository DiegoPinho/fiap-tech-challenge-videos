package com.fiap.techchallenge.diegopinho.videos.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.fiap.techchallenge.diegopinho.videos.controllers.criterias.CategoryCriteria;
import com.fiap.techchallenge.diegopinho.videos.controllers.dtos.CategoryDTO;
import com.fiap.techchallenge.diegopinho.videos.entities.Category;
import com.fiap.techchallenge.diegopinho.videos.services.CategoryService;
import com.fiap.techchallenge.diegopinho.videos.utils.DTOValidator;

public class CategoryController {

  @Autowired
  private CategoryService categoryService;

  @Autowired
  private DTOValidator validator;

  @GetMapping
  public ResponseEntity<?> getAll(CategoryCriteria criteria) {
    List<Category> categories = this.categoryService.getAll(criteria);
    return ResponseEntity.ok().body(categories);
  }

  @PostMapping
  public ResponseEntity<?> create(@RequestBody CategoryDTO categoryDTO) {
    Map<Object, Object> violations = validator.check(categoryDTO);
    if (!violations.isEmpty()) {
      return ResponseEntity.badRequest().body(violations);
    }

    Category category = this.categoryService.create(categoryDTO);
    return ResponseEntity.status(HttpStatus.CREATED).body(category);
  }

}
