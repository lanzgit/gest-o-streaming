package br.edu.infnet.gestao_streaming.subscription.application;

import br.edu.infnet.gestao_streaming.catalog.domain.StreamingServiceRepository;
import br.edu.infnet.gestao_streaming.subscription.domain.Subscription;
import br.edu.infnet.gestao_streaming.subscription.domain.SubscriptionDraft;
import br.edu.infnet.gestao_streaming.subscription.domain.SubscriptionFactory;
import br.edu.infnet.gestao_streaming.subscription.domain.SubscriptionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubscriptionFacade {

	private final StreamingServiceRepository streamingServices;
	private final SubscriptionFactory factory;
	private final SubscriptionRepository subscriptions;

	public SubscriptionFacade(
			StreamingServiceRepository streamingServices,
			SubscriptionFactory factory,
			SubscriptionRepository subscriptions) {
		this.streamingServices = streamingServices;
		this.factory = factory;
		this.subscriptions = subscriptions;
	}

	public SubscriptionResponse create(CreateSubscriptionCommand command) {
		SubscriptionDraft draft = toDraft(command);
		Subscription subscription = factory.create(draft);
		ensureStreamingServiceExists(subscription.streamingServiceId());
		return SubscriptionResponse.from(subscriptions.save(subscription));
	}

	public List<SubscriptionResponse> listByUser(Long userId) {
		return subscriptions.findByUserId(userId).stream()
				.map(SubscriptionResponse::from)
				.toList();
	}

	private SubscriptionDraft toDraft(CreateSubscriptionCommand command) {
		return new SubscriptionDraft(
				command.userId(),
				command.streamingServiceId(),
				command.amount(),
				command.billingCycle(),
				command.billingDate());
	}

	private void ensureStreamingServiceExists(Long streamingServiceId) {
		streamingServices.findById(streamingServiceId)
				.orElseThrow(() -> new IllegalArgumentException("Streaming service not found."));
	}
}
