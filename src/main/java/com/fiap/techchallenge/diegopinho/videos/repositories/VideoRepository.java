package com.fiap.techchallenge.diegopinho.videos.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.fiap.techchallenge.diegopinho.videos.entities.Video;

public interface VideoRepository extends JpaRepository<Video, Long>, JpaSpecificationExecutor<Video> {

  List<Video> findByFavoriteTrue();

  @Query("SELECT AVG(v.times) FROM videos v")
  Double getAverageTime();

}
