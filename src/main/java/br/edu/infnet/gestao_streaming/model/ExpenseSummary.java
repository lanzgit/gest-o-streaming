package br.edu.infnet.gestao_streaming.model;

import java.math.BigDecimal;

public record ExpenseSummary(Long userId, BigDecimal monthlyTotal, BigDecimal annualTotal) {}
