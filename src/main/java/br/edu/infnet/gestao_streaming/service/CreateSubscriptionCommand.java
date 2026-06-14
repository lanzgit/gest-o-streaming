package br.edu.infnet.gestao_streaming.service;

import br.edu.infnet.gestao_streaming.model.BillingCycle;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateSubscriptionCommand(
		Long userId,
		Long streamingServiceId,
		BigDecimal amount,
		BillingCycle billingCycle,
		LocalDate billingDate) {
}
