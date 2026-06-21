package br.edu.infnet.gestao_streaming.service;

import br.edu.infnet.gestao_streaming.domain.command.UpdateUsageCommand;
import br.edu.infnet.gestao_streaming.domain.model.Subscription;
import br.edu.infnet.gestao_streaming.domain.model.Usage;
import br.edu.infnet.gestao_streaming.domain.model.UsageLevel;
import br.edu.infnet.gestao_streaming.domain.model.UsageSummary;
import br.edu.infnet.gestao_streaming.repository.SubscriptionRepository;
import br.edu.infnet.gestao_streaming.repository.UsageRepository;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsageService {

  private final UsageRepository usageRepository;
  private final SubscriptionRepository subscriptions;
  private final Clock clock;

  public Usage update(UpdateUsageCommand command) {
    validate(command);
    Subscription subscription = findUserSubscription(command.userId(), command.subscriptionId());

    Usage usage =
        usageRepository
            .findByUserIdAndSubscriptionId(command.userId(), subscription.id())
            .map(existing -> replace(existing, command.level()))
            .orElseGet(() -> create(command.userId(), subscription.id(), command.level()));

    return usageRepository.save(usage);
  }

  public List<Usage> listByUser(Long userId) {
    return usageRepository.findByUserId(userId);
  }

  public List<Usage> listLowUsage(Long userId) {
    return usageRepository.findByUserId(userId).stream()
        .filter(usage -> usage.level() == UsageLevel.RARO || usage.level() == UsageLevel.NAO_USADO)
        .toList();
  }

  public UsageSummary summarize(Long userId) {
    List<Usage> entries = usageRepository.findByUserId(userId);
    return new UsageSummary(
        count(entries, UsageLevel.FREQUENTE),
        count(entries, UsageLevel.RARO),
        count(entries, UsageLevel.NAO_USADO));
  }

  private Usage create(Long userId, Long subscriptionId, UsageLevel level) {
    return new Usage(null, userId, subscriptionId, level, LocalDateTime.now(clock));
  }

  private Usage replace(Usage usage, UsageLevel level) {
    return new Usage(
        usage.id(), usage.userId(), usage.subscriptionId(), level, LocalDateTime.now(clock));
  }

  private long count(List<Usage> entries, UsageLevel level) {
    return entries.stream().filter(usage -> usage.level() == level).count();
  }

  private void validate(UpdateUsageCommand command) {
    if (command.userId() == null) {
      throw new IllegalArgumentException("User id is required.");
    }

    if (command.subscriptionId() == null) {
      throw new IllegalArgumentException("Subscription id is required.");
    }

    if (command.level() == null) {
      throw new IllegalArgumentException("Usage level is required.");
    }
  }

  private Subscription findUserSubscription(Long userId, Long subscriptionId) {
    Subscription subscription =
        subscriptions
            .findById(subscriptionId)
            .orElseThrow(() -> new IllegalArgumentException("Subscription not found."));

    if (!subscription.userId().equals(userId)) {
      throw new IllegalArgumentException("Subscription not found for user.");
    }

    return subscription;
  }
}
