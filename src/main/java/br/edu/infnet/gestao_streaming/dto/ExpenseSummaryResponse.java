package br.edu.infnet.gestao_streaming.dto;

import br.edu.infnet.gestao_streaming.domain.model.ExpenseSummary;
import java.math.BigDecimal;

public record ExpenseSummaryResponse(Long userId, BigDecimal monthlyTotal, BigDecimal annualTotal) {

  public static ExpenseSummaryResponse from(ExpenseSummary summary) {
    return new ExpenseSummaryResponse(
        summary.userId(), summary.monthlyTotal(), summary.annualTotal());
  }
}
