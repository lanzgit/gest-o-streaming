package br.edu.infnet.gestao_streaming.external.tmdb;

import br.edu.infnet.gestao_streaming.config.TmdbProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClient;

@Component
public class TmdbClient implements TmdbApiClient {

  private final TmdbProperties properties;
  private final RestClient restClient;

  public TmdbClient(TmdbProperties properties) {
    this.properties = properties;
    this.restClient =
        RestClient.builder()
            .baseUrl(properties.getBaseUrl())
            .defaultHeader(HttpHeaders.ACCEPT, "application/json")
            .build();
  }

  @Override
  public TmdbProviderListResponse listMovieProviders(String region) {
    ensureConfigured();
    return restClient
        .get()
        .uri(
            uriBuilder ->
                uriBuilder
                    .path("/watch/providers/movie")
                    .queryParam("watch_region", region)
                    .queryParam("language", properties.getDefaultLanguage())
                    .build())
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + properties.getApiToken())
        .retrieve()
        .body(TmdbProviderListResponse.class);
  }

  @Override
  public TmdbMovieListResponse discoverMoviesByProvider(Integer providerId, String region) {
    ensureConfigured();
    return restClient
        .get()
        .uri(
            uriBuilder ->
                uriBuilder
                    .path("/discover/movie")
                    .queryParam("watch_region", region)
                    .queryParam("with_watch_providers", providerId)
                    .queryParam("with_watch_monetization_types", "flatrate")
                    .queryParam("language", properties.getDefaultLanguage())
                    .queryParam("sort_by", "popularity.desc")
                    .build())
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + properties.getApiToken())
        .retrieve()
        .body(TmdbMovieListResponse.class);
  }

  private void ensureConfigured() {
    if (!StringUtils.hasText(properties.getApiToken())) {
      throw new IllegalStateException("TMDB_API_TOKEN nao configurado.");
    }
  }
}
