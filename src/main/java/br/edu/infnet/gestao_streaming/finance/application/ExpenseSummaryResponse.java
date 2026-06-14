package br.edu.infnet.gestao_streaming.finance.application;

import java.math.BigDecimal;

public record ExpenseSummaryResponse(Long userId, BigDecimal monthlyTotal, BigDecimal annualTotal) {
}
