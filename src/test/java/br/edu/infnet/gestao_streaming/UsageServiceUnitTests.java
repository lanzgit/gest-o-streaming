package br.edu.infnet.gestao_streaming;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import br.edu.infnet.gestao_streaming.model.BillingCycle;
import br.edu.infnet.gestao_streaming.model.Subscription;
import br.edu.infnet.gestao_streaming.model.SubscriptionStatus;
import br.edu.infnet.gestao_streaming.model.Usage;
import br.edu.infnet.gestao_streaming.model.UsageLevel;
import br.edu.infnet.gestao_streaming.repository.SubscriptionRepository;
import br.edu.infnet.gestao_streaming.repository.UsageRepository;
import br.edu.infnet.gestao_streaming.service.UpdateUsageCommand;
import br.edu.infnet.gestao_streaming.service.UsageService;
import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class UsageServiceUnitTests {

  private final FakeUsageRepository usageRepository = new FakeUsageRepository();
  private final FakeSubscriptionRepository subscriptions = new FakeSubscriptionRepository();
  private final UsageService service =
      new UsageService(
          usageRepository,
          subscriptions,
          Clock.fixed(Instant.parse("2026-06-20T00:00:00Z"), ZoneId.of("UTC")));

  @Test
  void createsAndUpdatesUsageClassification() {
    subscriptions.save(subscription(1L, 10L));

    Usage created = service.update(new UpdateUsageCommand(10L, 1L, UsageLevel.RARO));
    Usage updated = service.update(new UpdateUsageCommand(10L, 1L, UsageLevel.NAO_USADO));

    assertThat(created.id()).isEqualTo(1L);
    assertThat(updated.id()).isEqualTo(1L);
    assertThat(updated.level()).isEqualTo(UsageLevel.NAO_USADO);
    assertThat(service.listLowUsage(10L)).hasSize(1);
    assertThat(service.summarize(10L).notUsedCount()).isEqualTo(1);
  }

  @Test
  void rejectsUsageForSubscriptionFromAnotherUser() {
    subscriptions.save(subscription(1L, 20L));

    assertThatThrownBy(() -> service.update(new UpdateUsageCommand(10L, 1L, UsageLevel.RARO)))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Subscription not found for user.");
  }

  @Test
  void usageLevelIsRequired() {
    assertThatThrownBy(() -> service.update(new UpdateUsageCommand(10L, 1L, null)))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Usage level is required.");
  }

  private Subscription subscription(Long id, Long userId) {
    return new Subscription(
        id,
        userId,
        1L,
        BigDecimal.valueOf(29.90),
        BillingCycle.MENSAL,
        LocalDate.of(2026, 6, 20),
        SubscriptionStatus.ATIVA);
  }

  private static class FakeUsageRepository implements UsageRepository {

    private final List<Usage> entries = new ArrayList<>();
    private long sequence;

    @Override
    public Usage save(Usage usage) {
      Usage savedUsage =
          new Usage(
              resolveId(usage),
              usage.userId(),
              usage.subscriptionId(),
              usage.level(),
              usage.updatedAt());
      entries.removeIf(entry -> entry.id().equals(savedUsage.id()));
      entries.add(savedUsage);
      return savedUsage;
    }

    @Override
    public List<Usage> findByUserId(Long userId) {
      return entries.stream()
          .filter(usage -> usage.userId().equals(userId))
          .sorted(Comparator.comparing(Usage::subscriptionId))
          .toList();
    }

    @Override
    public Optional<Usage> findByUserIdAndSubscriptionId(Long userId, Long subscriptionId) {
      return entries.stream()
          .filter(usage -> usage.userId().equals(userId))
          .filter(usage -> usage.subscriptionId().equals(subscriptionId))
          .findFirst();
    }

    private Long resolveId(Usage usage) {
      if (usage.id() != null) {
        return usage.id();
      }
      return ++sequence;
    }
  }

  private static class FakeSubscriptionRepository implements SubscriptionRepository {

    private final List<Subscription> subscriptions = new ArrayList<>();

    @Override
    public Subscription save(Subscription subscription) {
      subscriptions.add(subscription);
      return subscription;
    }

    @Override
    public List<Subscription> findByUserId(Long userId) {
      return subscriptions.stream()
          .filter(subscription -> subscription.userId().equals(userId))
          .toList();
    }

    @Override
    public Optional<Subscription> findById(Long id) {
      return subscriptions.stream()
          .filter(subscription -> subscription.id().equals(id))
          .findFirst();
    }
  }
}
