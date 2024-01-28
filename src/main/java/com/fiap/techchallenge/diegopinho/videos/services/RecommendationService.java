package com.fiap.techchallenge.diegopinho.videos.services;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fiap.techchallenge.diegopinho.videos.entities.Category;
import com.fiap.techchallenge.diegopinho.videos.entities.Video;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecommendationService {

  private static int NUMBER_OF_RECOMMENDATIONS = 3;

  // @Autowired
  private final VideoService videoService;

  public List<Video> getRecommendationsBasedOnFavorites() {
    List<Video> favoriteVideos = this.videoService.getFavoriteVideos();
    Map<Category, Long> categoryFrequency = this.countCategoryFrequency(favoriteVideos);
    List<Category> sortedCategories = this.sortCategoriesByFrequency(categoryFrequency);
    List<Video> recommendedVideos = this.recommendVideos(sortedCategories, NUMBER_OF_RECOMMENDATIONS);

    return recommendedVideos;
  }

  private Map<Category, Long> countCategoryFrequency(List<Video> favoriteVideos) {
    Map<Category, Long> categoryFrequency = new HashMap<>();

    for (Video video : favoriteVideos) {
      Category category = video.getCategory();
      categoryFrequency.put(category, categoryFrequency.getOrDefault(category, 0L) + 1);
    }

    return categoryFrequency;
  }

  private List<Category> sortCategoriesByFrequency(Map<Category, Long> categoryFrequency) {
    List<Category> sortedCategories = categoryFrequency.entrySet()
        .stream()
        .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
        .map(Map.Entry::getKey)
        .collect(Collectors.toList());

    return sortedCategories;
  }

  private List<Video> recommendVideos(List<Category> sortedCategories, int numberOfVideos) {
    List<Video> recommendedVideos = new ArrayList<>();

    for (Category category : sortedCategories) {
      List<Video> videosInCategory = category.getVideos();
      videosInCategory.sort((v1, v2) -> Integer.compare(v2.getTimes(), v1.getTimes()));
      for (Video video : videosInCategory) {
        if (!recommendedVideos.contains(video)) {
          recommendedVideos.add(video);
        }

        if (recommendedVideos.size() >= numberOfVideos) {
          break;
        }
      }

      if (recommendedVideos.size() >= numberOfVideos) {
        break;
      }
    }

    return recommendedVideos;
  }

}
