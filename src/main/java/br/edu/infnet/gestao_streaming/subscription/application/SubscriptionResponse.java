package br.edu.infnet.gestao_streaming.subscription.application;

import br.edu.infnet.gestao_streaming.subscription.domain.BillingCycle;
import br.edu.infnet.gestao_streaming.subscription.domain.Subscription;
import br.edu.infnet.gestao_streaming.subscription.domain.SubscriptionStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

public record SubscriptionResponse(
		Long id,
		Long userId,
		Long streamingServiceId,
		BigDecimal amount,
		BillingCycle billingCycle,
		LocalDate billingDate,
		SubscriptionStatus status) {

	static SubscriptionResponse from(Subscription subscription) {
		return new SubscriptionResponse(
				subscription.id(),
				subscription.userId(),
				subscription.streamingServiceId(),
				subscription.amount(),
				subscription.billingCycle(),
				subscription.billingDate(),
				subscription.status());
	}
}
