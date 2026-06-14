package br.edu.infnet.gestao_streaming.subscription.domain;

import java.math.BigDecimal;
import java.time.LocalDate;

public record Subscription(
		Long id,
		Long userId,
		Long streamingServiceId,
		BigDecimal amount,
		BillingCycle billingCycle,
		LocalDate billingDate,
		SubscriptionStatus status) {

	public boolean isActive() {
		return status == SubscriptionStatus.ATIVA;
	}
}
