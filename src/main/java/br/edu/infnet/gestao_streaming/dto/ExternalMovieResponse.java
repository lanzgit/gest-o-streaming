package br.edu.infnet.gestao_streaming.dto;

import br.edu.infnet.gestao_streaming.external.model.ExternalMovie;
import java.time.LocalDate;

public record ExternalMovieResponse(
    Integer id,
    String title,
    String overview,
    String posterUrl,
    LocalDate releaseDate,
    Double voteAverage,
    Double popularity) {

  public static ExternalMovieResponse from(ExternalMovie movie) {
    return new ExternalMovieResponse(
        movie.id(),
        movie.title(),
        movie.overview(),
        movie.posterUrl(),
        movie.releaseDate(),
        movie.voteAverage(),
        movie.popularity());
  }
}
