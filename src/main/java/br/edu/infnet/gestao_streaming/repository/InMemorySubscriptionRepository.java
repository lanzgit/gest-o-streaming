package br.edu.infnet.gestao_streaming.repository;

import br.edu.infnet.gestao_streaming.model.Subscription;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
@Profile("test")
class InMemorySubscriptionRepository implements SubscriptionRepository {

	private final AtomicLong sequence = new AtomicLong();
	private final ConcurrentHashMap<Long, Subscription> subscriptions = new ConcurrentHashMap<>();

	@Override
	public Subscription save(Subscription subscription) {
		Long id = nextId(subscription);
		Subscription savedSubscription = new Subscription(
				id,
				subscription.userId(),
				subscription.streamingServiceId(),
				subscription.amount(),
				subscription.billingCycle(),
				subscription.billingDate(),
				subscription.status());

		subscriptions.put(id, savedSubscription);
		return savedSubscription;
	}

	@Override
	public List<Subscription> findByUserId(Long userId) {
		return subscriptions.values().stream()
				.filter(subscription -> subscription.userId().equals(userId))
				.sorted(Comparator.comparing(Subscription::id))
				.toList();
	}

	private Long nextId(Subscription subscription) {
		if (subscription.id() != null) {
			return subscription.id();
		}

		return sequence.incrementAndGet();
	}
}
