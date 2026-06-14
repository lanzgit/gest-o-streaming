package br.edu.infnet.gestao_streaming.subscription.web;

import br.edu.infnet.gestao_streaming.subscription.application.CreateSubscriptionCommand;
import br.edu.infnet.gestao_streaming.subscription.domain.BillingCycle;

import java.math.BigDecimal;
import java.time.LocalDate;

record CreateSubscriptionRequest(
		Long streamingServiceId,
		BigDecimal amount,
		BillingCycle billingCycle,
		LocalDate billingDate) {

	CreateSubscriptionCommand toCommand(Long userId) {
		return new CreateSubscriptionCommand(
				userId,
				streamingServiceId,
				amount,
				billingCycle,
				billingDate);
	}
}
