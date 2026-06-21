package br.edu.infnet.gestao_streaming.repository;

import br.edu.infnet.gestao_streaming.model.Usage;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

@Repository
@Profile("test")
class InMemoryUsageRepository implements UsageRepository {

  private final AtomicLong sequence = new AtomicLong();
  private final ConcurrentHashMap<Long, Usage> usageEntries = new ConcurrentHashMap<>();

  @Override
  public Usage save(Usage usage) {
    Long id = nextId(usage);
    Usage savedUsage =
        new Usage(id, usage.userId(), usage.subscriptionId(), usage.level(), usage.updatedAt());
    usageEntries.put(id, savedUsage);
    return savedUsage;
  }

  @Override
  public List<Usage> findByUserId(Long userId) {
    return usageEntries.values().stream()
        .filter(usage -> usage.userId().equals(userId))
        .sorted(Comparator.comparing(Usage::subscriptionId))
        .toList();
  }

  @Override
  public Optional<Usage> findByUserIdAndSubscriptionId(Long userId, Long subscriptionId) {
    return usageEntries.values().stream()
        .filter(usage -> usage.userId().equals(userId))
        .filter(usage -> usage.subscriptionId().equals(subscriptionId))
        .findFirst();
  }

  private Long nextId(Usage usage) {
    if (usage.id() != null) {
      return usage.id();
    }

    return sequence.incrementAndGet();
  }
}
