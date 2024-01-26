package com.fiap.techchallenge.diegopinho.videos.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fiap.techchallenge.diegopinho.videos.entities.Category;

public class CategoryRepositoryTest {

  @Mock
  private CategoryRepository categoryRepository;

  AutoCloseable openMocks;

  @BeforeEach
  void setUp() {
    openMocks = MockitoAnnotations.openMocks(this);
  }

  @AfterEach
  void tearDown() throws Exception {
    openMocks.close();
  }

  public Category generateCategory(String name, String description) {
    return Category.builder().name(name).description(description).build();
  }

  @Test
  public void shouldCreateACategory() {
    Category category = this.generateCategory("Action", "Action Videos!");
    when(categoryRepository.save(any(Category.class))).thenReturn(category);

    Category savedCategory = this.categoryRepository.save(category);

    verify(categoryRepository, times(1)).save(category);
    assertThat(savedCategory).isInstanceOf(Category.class).isNotNull().isEqualTo(category);
    assertThat(savedCategory).extracting(Category::getId).isEqualTo(category.getId());
    assertThat(savedCategory).extracting(Category::getName).isEqualTo(category.getName());
    assertThat(savedCategory).extracting(Category::getDescription).isEqualTo(category.getDescription());
  }

  @Test
  public void shouldReturnCategories() {
    Category category = this.generateCategory("Action", "Action Videos!");
    Category category2 = this.generateCategory("Adventure", "Adventure Videos!");
    List<Category> categories = Arrays.asList(category, category2);

    when(this.categoryRepository.findAll()).thenReturn(categories);

    List<Category> all = this.categoryRepository.findAll();

    verify(this.categoryRepository, times(1)).findAll();
    assertThat(all).hasSize(2).containsExactlyInAnyOrder(category, category2);
  }

  @Test
  public void shouldDeleteACategory() {
    long id = new Random().nextLong();
    doNothing().when(this.categoryRepository).deleteById(id);
    this.categoryRepository.deleteById(id);
    verify(this.categoryRepository, times(1)).deleteById(id);
  }

  @Test
  public void shouldReturnACategory() {
    long id = new Random().nextLong();
    Category category = this.generateCategory("Action", "Action Videos!");
    category.setId(id);

    when(this.categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));

    Optional<Category> byId = this.categoryRepository.findById(id);
    verify(categoryRepository, times(1)).findById(id);
    assertThat(byId).isPresent().containsSame(category);

    byId.ifPresent(loadedCategory -> {
      assertThat(loadedCategory.getId()).isEqualTo(category.getId());
      assertThat(loadedCategory.getName()).isEqualTo(category.getName());
      assertThat(loadedCategory.getDescription()).isEqualTo(category.getDescription());
    });
  }

}
