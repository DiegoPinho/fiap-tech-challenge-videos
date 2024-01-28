package com.fiap.techchallenge.diegopinho.videos.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fiap.techchallenge.diegopinho.videos.entities.Category;
import com.fiap.techchallenge.diegopinho.videos.entities.Video;
import com.fiap.techchallenge.diegopinho.videos.repositories.VideoRepository;
import com.fiap.techchallenge.diegopinho.videos.utils.CategoryHelper;
import com.fiap.techchallenge.diegopinho.videos.utils.VideoHelper;

public class RecomendationServiceTest {

  private RecommendationService recommendationService;
  private VideoService videoService;

  @Mock
  private VideoRepository videoRepository;

  @Mock
  private CategoryService categoryService;

  AutoCloseable openMocks;

  @BeforeEach
  void setUp() {
    openMocks = MockitoAnnotations.openMocks(this);
    videoService = new VideoService(videoRepository, categoryService);
    recommendationService = new RecommendationService(videoService);
  }

  @AfterEach
  void tearDown() throws Exception {
    openMocks.close();
  }

  @Test
  public void shouldGetRecommendationsBasedOnFavorites() {
    var category = CategoryHelper.generateCategory();

    var video1 = VideoHelper.generateVideo(category);
    video1.setFavorite(true);
    video1.setTimes(10);
    var video2 = VideoHelper.generateVideo(category);
    video2.setFavorite(true);
    video2.setTimes(10);
    var video3 = VideoHelper.generateVideo(category);
    video3.setFavorite(true);
    video3.setTimes(10);

    List<Video> videos = Arrays.asList(video1, video2, video3);
    category.setVideos(videos);

    when(videoService.getFavoriteVideos()).thenReturn(videos);

    List<Video> result = recommendationService.getRecommendationsBasedOnFavorites();
    assertEquals(1, result.size());
  }

  @Test
  public void shouldNotGetRecommendationsBasedOnEmptyFavorites() {
    when(videoService.getFavoriteVideos()).thenReturn(Collections.emptyList());
    List<Video> result = recommendationService.getRecommendationsBasedOnFavorites();
    assertEquals(0, result.size());
  }

}
