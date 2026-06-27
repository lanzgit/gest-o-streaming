package br.edu.infnet.gestao_streaming.external.tmdb;

public interface TmdbApiClient {

  TmdbProviderListResponse listMovieProviders(String region);

  TmdbMovieListResponse discoverMoviesByProvider(Integer providerId, String region);
}
