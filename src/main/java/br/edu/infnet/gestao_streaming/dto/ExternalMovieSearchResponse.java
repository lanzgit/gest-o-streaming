package br.edu.infnet.gestao_streaming.dto;

import br.edu.infnet.gestao_streaming.external.tmdb.TmdbMovieListResponse;
import java.util.List;

public record ExternalMovieSearchResponse(
    Integer page, Integer totalPages, Integer totalResults, List<ExternalMovieResponse> movies) {

  public static ExternalMovieSearchResponse from(TmdbMovieListResponse response) {
    List<ExternalMovieResponse> movies =
        response.results() == null
            ? List.of()
            : response.results().stream().map(ExternalMovieResponse::from).toList();

    return new ExternalMovieSearchResponse(
        response.page(), response.totalPages(), response.totalResults(), movies);
  }
}
