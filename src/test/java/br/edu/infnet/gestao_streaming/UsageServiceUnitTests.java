package br.edu.infnet.gestao_streaming;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import br.edu.infnet.gestao_streaming.domain.command.UpdateUsageCommand;
import br.edu.infnet.gestao_streaming.domain.model.BillingCycle;
import br.edu.infnet.gestao_streaming.domain.model.Subscription;
import br.edu.infnet.gestao_streaming.domain.model.SubscriptionStatus;
import br.edu.infnet.gestao_streaming.domain.model.Usage;
import br.edu.infnet.gestao_streaming.domain.model.UsageLevel;
import br.edu.infnet.gestao_streaming.repository.SubscriptionRepository;
import br.edu.infnet.gestao_streaming.repository.UsageRepository;
import br.edu.infnet.gestao_streaming.service.UsageService;
import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import org.junit.jupiter.api.Test;

class UsageServiceUnitTests {

  private final UsageRepository usageRepository = RepositoryTestDoubles.usage();
  private final SubscriptionRepository subscriptions = RepositoryTestDoubles.subscriptions();
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
}
