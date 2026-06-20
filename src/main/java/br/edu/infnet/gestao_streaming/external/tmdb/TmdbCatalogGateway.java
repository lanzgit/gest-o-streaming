package br.edu.infnet.gestao_streaming.external.tmdb;

public interface TmdbCatalogGateway {

  TmdbProviderListResponse listMovieProviders(String region);

  TmdbMovieListResponse discoverMoviesByProvider(Integer providerId, String region);
}
