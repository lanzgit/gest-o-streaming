package br.edu.infnet.gestao_streaming.external.model;

public record ExternalStreamingProvider(
    Integer providerId, String providerName, String logoUrl, Integer displayPriority) {}
