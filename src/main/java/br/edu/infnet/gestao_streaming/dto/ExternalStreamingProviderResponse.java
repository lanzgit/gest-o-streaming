package br.edu.infnet.gestao_streaming.dto;

import br.edu.infnet.gestao_streaming.external.tmdb.TmdbProviderListResponse.TmdbProviderResponse;

public record ExternalStreamingProviderResponse(
    Integer providerId, String providerName, String logoUrl, Integer displayPriority) {

  private static final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500";

  public static ExternalStreamingProviderResponse from(TmdbProviderResponse provider) {
    return new ExternalStreamingProviderResponse(
        provider.providerId(),
        provider.providerName(),
        buildLogoUrl(provider.logoPath()),
        provider.displayPriority());
  }

  private static String buildLogoUrl(String logoPath) {
    if (logoPath == null || logoPath.isBlank()) {
      return null;
    }
    return IMAGE_BASE_URL + logoPath;
  }
}
