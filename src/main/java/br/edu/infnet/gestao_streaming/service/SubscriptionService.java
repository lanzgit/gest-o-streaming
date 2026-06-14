package br.edu.infnet.gestao_streaming.service;

import br.edu.infnet.gestao_streaming.model.Subscription;
import br.edu.infnet.gestao_streaming.model.SubscriptionDraft;
import br.edu.infnet.gestao_streaming.repository.StreamingServiceRepository;
import br.edu.infnet.gestao_streaming.repository.SubscriptionRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class SubscriptionService {

  private final StreamingServiceRepository streamingServices;
  private final SubscriptionFactory factory;
  private final SubscriptionRepository subscriptions;

  public SubscriptionService(
      StreamingServiceRepository streamingServices,
      SubscriptionFactory factory,
      SubscriptionRepository subscriptions) {
    this.streamingServices = streamingServices;
    this.factory = factory;
    this.subscriptions = subscriptions;
  }

  public Subscription create(CreateSubscriptionCommand command) {
    SubscriptionDraft draft = toDraft(command);
    Subscription subscription = factory.create(draft);
    ensureStreamingServiceExists(subscription.streamingServiceId());
    return subscriptions.save(subscription);
  }

  public List<Subscription> listByUser(Long userId) {
    return subscriptions.findByUserId(userId);
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
    streamingServices
        .findById(streamingServiceId)
        .orElseThrow(() -> new IllegalArgumentException("Streaming service not found."));
  }
}
