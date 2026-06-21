package br.edu.infnet.gestao_streaming.domain.model;

public record UsageSummary(long frequentCount, long rareCount, long notUsedCount) {}
