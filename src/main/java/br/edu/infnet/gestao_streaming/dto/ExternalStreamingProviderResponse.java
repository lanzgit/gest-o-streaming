package br.edu.infnet.gestao_streaming.dto;

import br.edu.infnet.gestao_streaming.external.model.ExternalStreamingProvider;

public record ExternalStreamingProviderResponse(
    Integer providerId, String providerName, String logoUrl, Integer displayPriority) {

  public static ExternalStreamingProviderResponse from(ExternalStreamingProvider provider) {
    return new ExternalStreamingProviderResponse(
        provider.providerId(),
        provider.providerName(),
        provider.logoUrl(),
        provider.displayPriority());
  }
}
