package br.edu.infnet.gestao_streaming.dto;

import br.edu.infnet.gestao_streaming.model.UsageSummary;

public record UsageSummaryResponse(long frequentCount, long rareCount, long notUsedCount) {

  public static UsageSummaryResponse from(UsageSummary summary) {
    return new UsageSummaryResponse(
        summary.frequentCount(), summary.rareCount(), summary.notUsedCount());
  }
}
