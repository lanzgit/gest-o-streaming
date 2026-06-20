package br.edu.infnet.gestao_streaming.dto;

import br.edu.infnet.gestao_streaming.external.tmdb.TmdbMovieListResponse.TmdbMovieResponse;
import java.time.LocalDate;

public record ExternalMovieResponse(
    Integer id,
    String title,
    String overview,
    String posterUrl,
    LocalDate releaseDate,
    Double voteAverage,
    Double popularity) {

  private static final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500";

  public static ExternalMovieResponse from(TmdbMovieResponse movie) {
    return new ExternalMovieResponse(
        movie.id(),
        movie.title(),
        movie.overview(),
        buildPosterUrl(movie.posterPath()),
        movie.releaseDate(),
        movie.voteAverage(),
        movie.popularity());
  }

  private static String buildPosterUrl(String posterPath) {
    if (posterPath == null || posterPath.isBlank()) {
      return null;
    }
    return IMAGE_BASE_URL + posterPath;
  }
}
