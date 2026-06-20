package br.edu.infnet.gestao_streaming.external.tmdb;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import java.util.List;

public record TmdbMovieListResponse(
    Integer page,
    List<TmdbMovieResponse> results,
    @JsonProperty("total_pages") Integer totalPages,
    @JsonProperty("total_results") Integer totalResults) {

  public record TmdbMovieResponse(
      Integer id,
      String title,
      String overview,
      @JsonProperty("poster_path") String posterPath,
      @JsonProperty("release_date") LocalDate releaseDate,
      @JsonProperty("vote_average") Double voteAverage,
      Double popularity) {}
}
