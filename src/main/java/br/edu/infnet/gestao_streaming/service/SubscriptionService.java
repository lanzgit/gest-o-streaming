package br.edu.infnet.gestao_streaming.service;

import br.edu.infnet.gestao_streaming.domain.command.CreateSubscriptionCommand;
import br.edu.infnet.gestao_streaming.domain.factory.SubscriptionCreator;
import br.edu.infnet.gestao_streaming.domain.model.Subscription;
import br.edu.infnet.gestao_streaming.domain.model.SubscriptionDraft;
import br.edu.infnet.gestao_streaming.repository.StreamingServiceRepository;
import br.edu.infnet.gestao_streaming.repository.SubscriptionRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

  private final StreamingServiceRepository streamingServices;
  private final SubscriptionCreator factory;
  private final SubscriptionRepository subscriptions;

  public Subscription create(CreateSubscriptionCommand command) {
    SubscriptionDraft draft = toDraft(command);
    Subscription subscription = factory.create(draft);
    ensureStreamingServiceExists(subscription.streamingServiceId());
    return subscriptions.save(subscription);
  }

  public List<Subscription> listByUser(Long userId) {
    return subscriptions.findByUserIdOrderById(userId);
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
