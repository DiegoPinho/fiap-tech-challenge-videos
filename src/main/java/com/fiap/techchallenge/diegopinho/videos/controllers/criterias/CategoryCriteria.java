package com.fiap.techchallenge.diegopinho.videos.controllers.criterias;

import java.util.Objects;

import org.springframework.data.jpa.domain.Specification;

import com.fiap.techchallenge.diegopinho.videos.entities.Category;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class CategoryCriteria {
  private String name;

  public Specification<Category> toSpecification() {
    return (Root<Category> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
      Predicate predicate = criteriaBuilder.conjunction();

      if (Objects.nonNull(name)) {
        predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("name"), name));
      }

      return predicate;
    };
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
