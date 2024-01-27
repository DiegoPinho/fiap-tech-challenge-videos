package com.fiap.techchallenge.diegopinho.videos.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Arrays;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fiap.techchallenge.diegopinho.videos.entities.Category;
import com.fiap.techchallenge.diegopinho.videos.entities.Video;

public class VideoRepositoryTest {

  @Mock
  private VideoRepository videoRepository;

  AutoCloseable openMocks;

  @BeforeEach
  void setUp() {
    openMocks = MockitoAnnotations.openMocks(this);
  }

  @AfterEach
  void tearDown() throws Exception {
    openMocks.close();
  }

  public Video generateVideo(String title, String description, String link, LocalDate publication, boolean favorite,
      Category category) {
    return Video.builder()
        .title(title)
        .description(description)
        .link(link)
        .publication(publication)
        .favorite(favorite)
        .category(category)
        .build();
  }

  @Test
  public void shouldCreateAVideo() {
    Category category = new Category();
    LocalDate now = LocalDate.now();
    Video video = this.generateVideo("Title", "Description", "link", now, false, category);
    when(videoRepository.save(any(Video.class))).thenReturn(video);

    Video savedVideo = this.videoRepository.save(video);
    verify(videoRepository, times(1)).save(video);
    assertThat(savedVideo).isInstanceOf(Video.class).isNotNull().isEqualTo(video);
    assertThat(savedVideo).extracting(Video::getId).isEqualTo(video.getId());
    assertThat(savedVideo).extracting(Video::getTitle).isEqualTo(video.getTitle());
    assertThat(savedVideo).extracting(Video::getDescription).isEqualTo(video.getDescription());
    assertThat(savedVideo).extracting(Video::getLink).isEqualTo(video.getLink());
    assertThat(savedVideo).extracting(Video::getFavorite).isEqualTo(video.getFavorite());
    assertThat(savedVideo).extracting(Video::getPublication).isEqualTo(now);
  }

  @Test
  public void shouldReturnVideos() {
    Category category = new Category();
    LocalDate now = LocalDate.now();
    Video video = this.generateVideo("Title", "Description", "link", now, false, category);
    Video video2 = this.generateVideo("Title", "Description", "link", now, false, category);
    List<Video> videos = Arrays.asList(video, video2);

    when(this.videoRepository.findAll()).thenReturn(videos);

    List<Video> all = this.videoRepository.findAll();

    verify(this.videoRepository, times(1)).findAll();
    assertThat(all).hasSize(2).containsExactlyInAnyOrder(video, video2);
  }

  @Test
  public void shouldDeleteAVideo() {
    long id = new Random().nextLong();
    doNothing().when(this.videoRepository).deleteById(id);
    this.videoRepository.deleteById(id);
    verify(this.videoRepository, times(1)).deleteById(id);
  }

  @Test
  public void shouldReturnACategory() {
    long id = new Random().nextLong();
    Category category = new Category();
    LocalDate now = LocalDate.now();
    Video video = this.generateVideo("Title", "Description", "link", now, false, category);
    video.setId(id);

    when(this.videoRepository.findById(anyLong())).thenReturn(Optional.of(video));

    Optional<Video> byId = this.videoRepository.findById(id);
    verify(videoRepository, times(1)).findById(id);
    assertThat(byId).isPresent().containsSame(video);

    byId.ifPresent(loadedVideo -> {
      assertThat(loadedVideo.getId()).isEqualTo(video.getId());
      assertThat(loadedVideo.getDescription()).isEqualTo(video.getDescription());
      assertThat(loadedVideo.getLink()).isEqualTo(video.getLink());
      assertThat(loadedVideo.getFavorite()).isEqualTo(video.getFavorite());
      assertThat(loadedVideo.getPublication()).isEqualTo(video.getPublication());
    });

  }

}
