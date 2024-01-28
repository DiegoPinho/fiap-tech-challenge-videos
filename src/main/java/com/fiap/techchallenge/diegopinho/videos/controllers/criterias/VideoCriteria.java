package com.fiap.techchallenge.diegopinho.videos.controllers.criterias;

import java.time.LocalDateTime;
import java.util.Objects;

import org.springframework.data.jpa.domain.Specification;

import com.fiap.techchallenge.diegopinho.videos.entities.Video;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class VideoCriteria {

  private String title;
  private LocalDateTime publication;

  public Specification<Video> toSpecification() {
    return (Root<Video> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
      Predicate predicate = criteriaBuilder.conjunction();

      if (Objects.nonNull(title)) {
        predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("title"), title));
      }

      if (Objects.nonNull(publication)) {
        predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("publication"), publication));
      }

      return predicate;
    };
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

}
