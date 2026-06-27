package br.edu.infnet.gestao_streaming.service;

import br.edu.infnet.gestao_streaming.config.TmdbProperties;
import br.edu.infnet.gestao_streaming.dto.ExternalMovieSearchResponse;
import br.edu.infnet.gestao_streaming.dto.ExternalStreamingProviderResponse;
import br.edu.infnet.gestao_streaming.external.CatalogProviderGateway;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class ExternalCatalogService {

  private final CatalogProviderGateway catalogProvider;
  private final TmdbProperties properties;

  public List<ExternalStreamingProviderResponse> listMovieProviders(String region) {
    String resolvedRegion = resolveRegion(region);
    return catalogProvider.listMovieProviders(resolvedRegion).stream()
        .map(ExternalStreamingProviderResponse::from)
        .toList();
  }

  public ExternalMovieSearchResponse discoverMoviesByProvider(Integer providerId, String region) {
    if (providerId == null || providerId <= 0) {
      throw new IllegalArgumentException("providerId deve ser positivo.");
    }

    String resolvedRegion = resolveRegion(region);
    return ExternalMovieSearchResponse.from(
        catalogProvider.discoverMoviesByProvider(providerId, resolvedRegion));
  }

  private String resolveRegion(String region) {
    if (StringUtils.hasText(region)) {
      return region.trim().toUpperCase();
    }
    return properties.getDefaultRegion();
  }
}
