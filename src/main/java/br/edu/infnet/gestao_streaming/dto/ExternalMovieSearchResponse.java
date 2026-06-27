package br.edu.infnet.gestao_streaming.dto;

import br.edu.infnet.gestao_streaming.external.model.ExternalMovieCatalog;
import java.util.List;

public record ExternalMovieSearchResponse(
    Integer page, Integer totalPages, Integer totalResults, List<ExternalMovieResponse> movies) {

  public static ExternalMovieSearchResponse from(ExternalMovieCatalog response) {
    return new ExternalMovieSearchResponse(
        response.page(),
        response.totalPages(),
        response.totalResults(),
        response.movies().stream().map(ExternalMovieResponse::from).toList());
  }
}
