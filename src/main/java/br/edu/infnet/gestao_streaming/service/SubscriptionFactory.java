package br.edu.infnet.gestao_streaming.service;

import br.edu.infnet.gestao_streaming.model.Subscription;
import br.edu.infnet.gestao_streaming.model.SubscriptionDraft;
import br.edu.infnet.gestao_streaming.model.SubscriptionStatus;

import org.springframework.stereotype.Component;

@Component
public class SubscriptionFactory {

	public Subscription create(SubscriptionDraft draft) {
		validate(draft);
		return new Subscription(
				null,
				draft.userId(),
				draft.streamingServiceId(),
				draft.amount(),
				draft.billingCycle(),
				draft.billingDate(),
				SubscriptionStatus.ATIVA);
	}

	private void validate(SubscriptionDraft draft) {
		if (draft.userId() == null) {
			throw new IllegalArgumentException("User id is required.");
		}

		if (draft.streamingServiceId() == null) {
			throw new IllegalArgumentException("Streaming service id is required.");
		}

		if (draft.amount() == null || draft.amount().signum() <= 0) {
			throw new IllegalArgumentException("Subscription amount must be positive.");
		}

		if (draft.billingCycle() == null) {
			throw new IllegalArgumentException("Billing cycle is required.");
		}

		if (draft.billingDate() == null) {
			throw new IllegalArgumentException("Billing date is required.");
		}
	}
}
