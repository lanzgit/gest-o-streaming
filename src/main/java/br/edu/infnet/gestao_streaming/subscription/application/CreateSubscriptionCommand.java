package br.edu.infnet.gestao_streaming.subscription.application;

import br.edu.infnet.gestao_streaming.subscription.domain.BillingCycle;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateSubscriptionCommand(
		Long userId,
		Long streamingServiceId,
		BigDecimal amount,
		BillingCycle billingCycle,
		LocalDate billingDate) {
}
