package com.fiap.techchallenge.diegopinho.videos.controllers;

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
import org.springframework.web.bind.annotation.RestController;

import com.fiap.techchallenge.diegopinho.videos.controllers.criterias.CategoryCriteria;
import com.fiap.techchallenge.diegopinho.videos.controllers.dtos.CategoryDTO;
import com.fiap.techchallenge.diegopinho.videos.entities.Category;
import com.fiap.techchallenge.diegopinho.videos.exceptions.ConflictException;
import com.fiap.techchallenge.diegopinho.videos.exceptions.NotFoundException;
import com.fiap.techchallenge.diegopinho.videos.services.CategoryService;
import com.fiap.techchallenge.diegopinho.videos.utils.DTOValidator;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

  // @Autowired
  private final CategoryService categoryService;

  // @Autowired
  private final DTOValidator validator;

  @GetMapping
  public ResponseEntity<?> getAll(CategoryCriteria criteria) {
    List<Category> categories = this.categoryService.getAll(criteria);
    return ResponseEntity.ok().body(categories);
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> getById(@PathVariable("id") Long id) {
    try {
      Category category = this.categoryService.getById(id);
      return ResponseEntity.ok().body(category);
    } catch (NotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
  }

  @PostMapping
  public ResponseEntity<?> create(@RequestBody CategoryDTO categoryDTO) {
    Map<Object, Object> violations = validator.check(categoryDTO);
    if (!violations.isEmpty()) {
      return ResponseEntity.badRequest().body(violations);
    }

    try {
      Category category = this.categoryService.create(categoryDTO);
      return ResponseEntity.status(HttpStatus.CREATED).body(category);
    } catch (ConflictException e) {
      return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
  }

  @PutMapping("/{id}")
  public ResponseEntity<?> update(@RequestBody CategoryDTO categoryDTO, @PathVariable("id") Long id) {
    try {
      Category category = this.categoryService.update(id, categoryDTO);
      return ResponseEntity.ok().body(category);
    } catch (NotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> delete(@PathVariable("id") Long id) {
    try {
      this.categoryService.delete(id);
      return ResponseEntity.ok().build();
    } catch (NotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cannot remove category in use.");
    }
  }

}
