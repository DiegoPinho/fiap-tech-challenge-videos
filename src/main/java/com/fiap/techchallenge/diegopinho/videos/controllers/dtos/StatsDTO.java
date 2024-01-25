package com.fiap.techchallenge.diegopinho.videos.controllers.dtos;

public class StatsDTO {
  private long totalVideos;
  private int totalFavoriteVideos;
  private Double averageTimes;

  public StatsDTO(long totalVideos, int totalFavoriteVideos, Double averageTimes) {
    this.totalVideos = totalVideos;
    this.totalFavoriteVideos = totalFavoriteVideos;
    this.averageTimes = averageTimes;
  }

  public long getTotalVideos() {
    return totalVideos;
  }

  public void setTotalVideos(long totalVideos) {
    this.totalVideos = totalVideos;
  }

  public int getTotalFavoriteVideos() {
    return totalFavoriteVideos;
  }

  public void setTotalFavoriteVideos(int totalFavoriteVideos) {
    this.totalFavoriteVideos = totalFavoriteVideos;
  }

  public Double getAverageTimes() {
    return averageTimes;
  }

  public void setAverageTimes(Double averageTimes) {
    this.averageTimes = averageTimes;
  }

}
