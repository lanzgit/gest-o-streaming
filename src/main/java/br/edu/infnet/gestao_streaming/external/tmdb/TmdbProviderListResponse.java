package br.edu.infnet.gestao_streaming.external.tmdb;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record TmdbProviderListResponse(List<TmdbProviderResponse> results) {

  public record TmdbProviderResponse(
      @JsonProperty("provider_id") Integer providerId,
      @JsonProperty("provider_name") String providerName,
      @JsonProperty("logo_path") String logoPath,
      @JsonProperty("display_priority") Integer displayPriority) {}
}
