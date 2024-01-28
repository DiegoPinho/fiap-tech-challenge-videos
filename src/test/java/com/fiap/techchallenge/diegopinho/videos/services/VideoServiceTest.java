package com.fiap.techchallenge.diegopinho.videos.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
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

import com.fiap.techchallenge.diegopinho.videos.controllers.criterias.VideoCriteria;
import com.fiap.techchallenge.diegopinho.videos.controllers.dtos.StatsDTO;
import com.fiap.techchallenge.diegopinho.videos.controllers.dtos.VideoDTO;
import com.fiap.techchallenge.diegopinho.videos.entities.Category;
import com.fiap.techchallenge.diegopinho.videos.entities.Video;
import com.fiap.techchallenge.diegopinho.videos.exceptions.NotFoundException;
import com.fiap.techchallenge.diegopinho.videos.repositories.VideoRepository;
import com.fiap.techchallenge.diegopinho.videos.utils.CategoryHelper;
import com.fiap.techchallenge.diegopinho.videos.utils.VideoHelper;

public class VideoServiceTest {

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
  }

  @AfterEach
  void tearDown() throws Exception {
    openMocks.close();
  }

  @Nested
  public class CreateVideo {

    @Test
    public void shouldCreateAVideo() {
      VideoDTO videoDTO = VideoHelper.generateVideoDTO(1L);
      when(videoRepository.save(any(Video.class))).thenAnswer(i -> i.getArgument(0));
      when(categoryService.getById(any(Long.class))).thenReturn(new Category());

      Video savedVideo = videoService.create(videoDTO);

      assertThat(savedVideo).isInstanceOf(Video.class).isNotNull();
      assertThat(savedVideo.getTitle()).isEqualTo(videoDTO.getTitle());
      assertThat(savedVideo.getDescription()).isEqualTo(videoDTO.getDescription());
      assertThat(savedVideo.getLink()).isEqualTo(videoDTO.getLink());
      assertThat(savedVideo.getPublication()).isEqualTo(videoDTO.getPublication());

      verify(videoRepository, times(1)).save(videoDTO.toVideo());
    }

  }

  @Nested
  public class SearchVideo {

    @Test
    public void shouldReturnVideo() {
      Long id = new Random().nextLong();
      Video video = new Video();
      video.setId(id);
      video.setTitle("Title");
      video.setDescription("Description");
      video.setLink("Link");
      video.setPublication(LocalDate.now());
      video.toString();
      when(videoRepository.findById(id)).thenReturn(Optional.of(video));

      Video videoFromDb = videoService.getById(id);
      verify(videoRepository, times(1)).findById(id);
      assertThat(videoFromDb.getId()).isEqualTo(video.getId());
      assertThat(videoFromDb.getTitle()).isEqualTo(video.getTitle());
      assertThat(videoFromDb.getDescription()).isEqualTo(video.getDescription());
      assertThat(videoFromDb.getLink()).isEqualTo(video.getLink());
      assertThat(videoFromDb.getPublication()).isEqualTo(video.getPublication());
    }

    @Test
    public void shouldReturnErrorIfNotFound() {
      Long id = new Random().nextLong();
      when(videoRepository.findById(any(Long.class))).thenReturn(Optional.empty());
      assertThatThrownBy(() -> videoService.getById(id))
          .isInstanceOf(NotFoundException.class)
          .hasMessage("Video Not Found!");
      verify(videoRepository, times(1)).findById(id);
    }

    @Test
    public void shouldReturnAllVideos() {
      var category = new Category();
      Video video = VideoHelper.generateVideo(category);
      Video video2 = VideoHelper.generateVideo(category);

      List<Video> videos = Arrays.asList(video, video2);
      when(videoRepository.findAll(any(Specification.class))).thenReturn(videos);

      List<Video> all = videoService.getAll(new VideoCriteria());
      assertThat(all).hasSize(2);
      assertThat(all).asList().allSatisfy(c -> {
        assertThat(c).isNotNull();
        assertThat(c).isInstanceOf(Video.class);
      });

      verify(videoRepository, times(1)).findAll(any(Specification.class));
    }

  }

  @Nested
  public class AlterVideo {

    @Test
    public void shouldAlterAVideo() {
      Long id = new Random().nextLong();

      Category category = CategoryHelper.generateCategory();
      Video oldVideo = VideoHelper.generateVideo(category);
      oldVideo.setId(id);

      when(videoRepository.findById(any(Long.class))).thenReturn(Optional.of(oldVideo));
      when(videoRepository.save(any(Video.class))).thenAnswer(i -> i.getArgument(0));

      VideoDTO videoDTO = new VideoDTO();
      videoDTO.setTitle("Name 2");
      videoDTO.setDescription("Description 2");

      Video updatedVideo = videoService.update(id, videoDTO);

      assertThat(updatedVideo).isInstanceOf(Video.class).isNotNull();
      assertThat(updatedVideo.getId()).isEqualTo(id);
      assertThat(updatedVideo.getTitle()).isEqualTo(videoDTO.getTitle());
      assertThat(updatedVideo.getDescription()).isEqualTo(videoDTO.getDescription());
      verify(videoRepository, times(1)).save(any(Video.class));
    }

    @Test
    public void shouldReturnErrorIfIdDoesNotExist() {
      Long id = new Random().nextLong();

      Category category = CategoryHelper.generateCategory();
      Video oldVideo = VideoHelper.generateVideo(category);
      oldVideo.setId(id);

      VideoDTO videoDTO = new VideoDTO();
      videoDTO.setTitle("Name 2");
      videoDTO.setDescription("Description 2");

      when(videoRepository.findById(any(Long.class))).thenReturn(Optional.empty());

      assertThatThrownBy(() -> videoService.update(id, videoDTO))
          .isInstanceOf(NotFoundException.class)
          .hasMessage("Video Not Found!");

      verify(videoRepository, never()).save(any(Video.class));
    }

  }

  @Nested
  public class RemoveVideo {

    @Test
    public void shouldDeleteAVideo() {
      Long id = new Random().nextLong();

      var category = CategoryHelper.generateCategory();
      var video = VideoHelper.generateVideo(category);
      video.setId(id);

      when(videoRepository.findById(id)).thenReturn(Optional.of(video));
      doNothing().when(videoRepository).deleteById(id);

      videoService.delete(id);

      verify(videoRepository, times(1)).findById(id);
      verify(videoRepository, times(1)).deleteById(id);
    }

    @Test
    public void shouldReturnErrorIfIdDoesNotExist() {
      Long id = new Random().nextLong();

      Category category = CategoryHelper.generateCategory();
      Video oldVideo = VideoHelper.generateVideo(category);
      oldVideo.setId(id);

      when(videoRepository.findById(any(Long.class))).thenReturn(Optional.empty());

      assertThatThrownBy(() -> videoService.delete(id))
          .isInstanceOf(NotFoundException.class)
          .hasMessage("Video Not Found!");

      verify(videoRepository, never()).delete(any(Video.class));
    }
  }

  @Nested
  public class FavoriteVideo {

    @Test
    public void shouldFavoriteAVideo() {
      Long id = new Random().nextLong();

      Category category = CategoryHelper.generateCategory();
      Video video = VideoHelper.generateVideo(category);
      video.setId(id);

      when(videoRepository.findById(any(Long.class))).thenReturn(Optional.of(video));
      when(videoRepository.save(any(Video.class))).thenAnswer(i -> i.getArgument(0));

      Video updatedVideo = videoService.favorite(id);
      assertThat(updatedVideo).isInstanceOf(Video.class).isNotNull();
      assertThat(updatedVideo.getId()).isEqualTo(id);
      assertThat(updatedVideo.getFavorite()).isEqualTo(true);
    }

    @Test
    public void shouldUnfavoriteAVideo() {
      Long id = new Random().nextLong();

      Category category = CategoryHelper.generateCategory();
      Video video = VideoHelper.generateVideo(category);
      video.setId(id);
      video.setFavorite(true);

      when(videoRepository.findById(any(Long.class))).thenReturn(Optional.of(video));
      when(videoRepository.save(any(Video.class))).thenAnswer(i -> i.getArgument(0));

      Video updatedVideo = videoService.unfavorite(id);
      assertThat(updatedVideo).isInstanceOf(Video.class).isNotNull();
      assertThat(updatedVideo.getId()).isEqualTo(id);
      assertThat(updatedVideo.getFavorite()).isEqualTo(false);
    }

  }

  @Nested
  public class PlayVideo {

    @Test
    public void shouldPlayVideo() {
      Long id = new Random().nextLong();

      Category category = CategoryHelper.generateCategory();
      Video video = VideoHelper.generateVideo(category);
      video.setId(id);
      video.setTimes(0);

      when(videoRepository.findById(any(Long.class))).thenReturn(Optional.of(video));
      when(videoRepository.save(any(Video.class))).thenAnswer(i -> i.getArgument(0));

      Video playedVideo = videoService.playVideo(id);
      assertThat(playedVideo).isInstanceOf(Video.class).isNotNull();
      assertThat(playedVideo.getId()).isEqualTo(id);
      assertThat(playedVideo.getTimes()).isEqualTo(1);
    }

  }

  @Nested
  public class VideoStats {

    @Test
    public void shouldGetStatusFromVideos() {
      Long id = new Random().nextLong();

      Category category = CategoryHelper.generateCategory();

      Video video = VideoHelper.generateVideo(category);
      video.setId(id);
      video.setTimes(2);
      video.setFavorite(true);

      Video video2 = VideoHelper.generateVideo(category);
      video.setId(id);
      video.setTimes(4);
      video2.setFavorite(true);

      List<Video> videos = Arrays.asList(video, video2);
      double avg = (double) (video.getTimes() + video.getTimes()) / 2;

      when(videoRepository.count()).thenReturn(2L);
      when(videoRepository.findByFavoriteTrue()).thenReturn(videos);
      when(videoRepository.getAverageTime()).thenReturn(avg);

      StatsDTO stats = videoService.getStatisticsFromVideos();
      assertThat(stats).isInstanceOf(StatsDTO.class).isNotNull();
      assertThat(stats.getTotalVideos()).isEqualTo(videos.size());
      assertThat(stats.getAverageTimes()).isEqualTo(avg);
      assertThat(stats.getTotalFavoriteVideos()).isEqualTo(videos.size());
    }

  }

}
