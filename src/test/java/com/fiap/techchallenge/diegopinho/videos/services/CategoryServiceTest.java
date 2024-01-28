package com.fiap.techchallenge.diegopinho.videos.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.jpa.domain.Specification;

import com.fiap.techchallenge.diegopinho.videos.controllers.criterias.CategoryCriteria;
import com.fiap.techchallenge.diegopinho.videos.controllers.dtos.CategoryDTO;
import com.fiap.techchallenge.diegopinho.videos.entities.Category;
import com.fiap.techchallenge.diegopinho.videos.exceptions.ConflictException;
import com.fiap.techchallenge.diegopinho.videos.exceptions.NotFoundException;
import com.fiap.techchallenge.diegopinho.videos.repositories.CategoryRepository;
import com.fiap.techchallenge.diegopinho.videos.utils.CategoryHelper;

public class CategoryServiceTest {

  private CategoryService categoryService;

  @Mock
  private CategoryRepository categoryRepository;

  AutoCloseable openMocks;

  @BeforeEach
  void setUp() {
    openMocks = MockitoAnnotations.openMocks(this);
    categoryService = new CategoryService(categoryRepository);
  }

  @AfterEach
  void tearDown() throws Exception {
    openMocks.close();
  }

  @Nested
  public class CreateCategory {

    @Test
    public void shouldCreateACategory() {
      CategoryDTO categoryDTO = CategoryHelper.generateCategoryDTO();
      when(categoryRepository.save(any(Category.class))).thenAnswer(i -> i.getArgument(0));

      Category savedCategory = categoryService.create(categoryDTO);

      assertThat(savedCategory).isInstanceOf(Category.class).isNotNull();
      assertThat(savedCategory.getName()).isEqualTo(categoryDTO.getName());
      assertThat(savedCategory.getDescription()).isEqualTo(categoryDTO.getDescription());
      verify(categoryRepository, times(1)).save(categoryDTO.toCategory());
    }

    @Test
    public void shouldNotCreateDuplicatedCategory() {
      Category category = CategoryHelper.generateCategory();
      CategoryDTO categoryDTO = CategoryHelper.generateCategoryDTO();
      categoryDTO.setName(category.getName());
      when(categoryRepository.findByName(any(String.class))).thenReturn(Optional.of(category));

      assertThatThrownBy(() -> categoryService.create(categoryDTO))
          .isInstanceOf(ConflictException.class)
          .hasMessage("Category already exist.");
      verify(categoryRepository, never()).save(any(Category.class));
    }
  }

  @Nested
  public class SearchCategory {

    @Test
    public void shouldReturnACategory() {
      Long id = new Random().nextLong();
      Category category = CategoryHelper.generateCategory();
      when(categoryRepository.findById(any(Long.class))).thenReturn(Optional.of(category));

      Category categoryFromDb = categoryService.getById(id);
      verify(categoryRepository, times(1)).findById(id);
      assertThat(categoryFromDb).isEqualTo(category);
      assertThat(categoryFromDb.getId()).isEqualTo(category.getId());
      assertThat(categoryFromDb.getName()).isEqualTo(category.getName());
      assertThat(categoryFromDb.getDescription()).isEqualTo(category.getDescription());
    }

    @Test
    public void shouldReturnErrorIfNotFound() {
      Long id = new Random().nextLong();
      when(categoryRepository.findById(any(Long.class)))
          .thenReturn(Optional.empty());
      assertThatThrownBy(() -> categoryService.getById(id))
          .isInstanceOf(NotFoundException.class)
          .hasMessage("Category Not Found!");
      verify(categoryRepository, times(1)).findById(id);
    }

    @Test
    public void shouldReturnAllCategories() {
      Category category = CategoryHelper.generateCategory();
      Category category2 = CategoryHelper.generateCategory();

      List<Category> categories = Arrays.asList(category, category2);
      when(categoryRepository.findAll(any(Specification.class))).thenReturn(categories);

      List<Category> all = categoryService.getAll(new CategoryCriteria());
      assertThat(all).hasSize(2);
      assertThat(all).asList().allSatisfy(c -> {
        assertThat(c).isNotNull();
        assertThat(c).isInstanceOf(Category.class);
      });

      verify(categoryRepository, times(1)).findAll(any(Specification.class));
    }

  }

  @Nested
  public class AlterCategory {

    @Test
    public void shouldAlterACategory() {
      Long id = new Random().nextLong();

      Category oldCategory = new Category();
      oldCategory.setId(id);
      oldCategory.setName("Name");
      oldCategory.setDescription("Description");

      oldCategory.toString();

      when(categoryRepository.findById(any(Long.class))).thenReturn(Optional.of(oldCategory));
      when(categoryRepository.save(any(Category.class))).thenAnswer(i -> i.getArgument(0));

      CategoryDTO categoryDTO = new CategoryDTO();
      categoryDTO.setName("Name 2");
      categoryDTO.setDescription("Description 2");

      Category updatedCategory = categoryService.update(id, categoryDTO);

      assertThat(updatedCategory).isInstanceOf(Category.class).isNotNull();
      assertThat(updatedCategory.getId()).isEqualTo(id);
      assertThat(updatedCategory.getName()).isEqualTo(categoryDTO.getName());
      assertThat(updatedCategory.getDescription()).isEqualTo(categoryDTO.getDescription());
      verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    public void shouldReturnErrorIfIdDoesNotExist() {
      Long id = new Random().nextLong();

      var oldCategory = CategoryHelper.generateCategory();
      oldCategory.setId(id);

      var categoryDTO = new CategoryDTO();
      categoryDTO.setName("Name 2");
      categoryDTO.setDescription("Description 2");

      when(categoryRepository.findById(any(Long.class))).thenReturn(Optional.empty());

      assertThatThrownBy(() -> categoryService.update(id, categoryDTO))
          .isInstanceOf(NotFoundException.class)
          .hasMessage("Category Not Found!");
      verify(categoryRepository, never()).save(any(Category.class));
    }
  }

  @Nested
  public class RemoveCategory {

    @Test
    void shouldRemoveCategory() {
      Long id = new Random().nextLong();

      var category = CategoryHelper.generateCategory();
      category.setId(id);
      when(categoryRepository.findById(id)).thenReturn(Optional.of(category));
      doNothing().when(categoryRepository).deleteById(id);

      categoryService.delete(id);

      verify(categoryRepository, times(1)).findById(id);
      verify(categoryRepository, times(1)).deleteById(id);
    }

    @Test
    public void shouldReturnErrorIfIdDoesNotExist() {
      Long id = new Random().nextLong();

      var oldCategory = CategoryHelper.generateCategory();
      oldCategory.setId(id);

      when(categoryRepository.findById(any(Long.class))).thenReturn(Optional.empty());

      assertThatThrownBy(() -> categoryService.delete(id))
          .isInstanceOf(NotFoundException.class)
          .hasMessage("Category Not Found!");
      verify(categoryRepository, never()).delete(any(Category.class));
    }
  }

}
