package com.fiap.techchallenge.diegopinho.videos.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.fiap.techchallenge.diegopinho.videos.controllers.criterias.CategoryCriteria;
import com.fiap.techchallenge.diegopinho.videos.controllers.dtos.CategoryDTO;
import com.fiap.techchallenge.diegopinho.videos.entities.Category;
import com.fiap.techchallenge.diegopinho.videos.exceptions.ConflictException;
import com.fiap.techchallenge.diegopinho.videos.exceptions.NotFoundException;
import com.fiap.techchallenge.diegopinho.videos.repositories.CategoryRepository;

@Service
public class CategoryService {

  @Autowired
  private CategoryRepository categoryRepository;

  public List<Category> getAll(CategoryCriteria criteria) {
    Specification<Category> specification = criteria.toSpecification();
    return this.categoryRepository.findAll(specification);
  }

  public Category getById(Long id) {
    return this.categoryRepository
        .findById(id)
        .orElseThrow(() -> new NotFoundException("Category Not Found!"));
  }

  public Category create(CategoryDTO categoryDTO) {
    Optional<Category> existingCategory = this.categoryRepository.findByName(categoryDTO.getName());

    if (existingCategory.isPresent()) {
      throw new ConflictException("Category already exist.");
    }

    Category category = categoryDTO.toCategory();
    return this.categoryRepository.save(category);
  }

}
