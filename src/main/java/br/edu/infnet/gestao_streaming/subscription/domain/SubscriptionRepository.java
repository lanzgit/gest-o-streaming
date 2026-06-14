package br.edu.infnet.gestao_streaming.subscription.domain;

import java.util.List;

public interface SubscriptionRepository {

	Subscription save(Subscription subscription);

	List<Subscription> findByUserId(Long userId);
}
