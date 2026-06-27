package br.edu.infnet.gestao_streaming;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import br.edu.infnet.gestao_streaming.domain.model.BillingCycle;
import br.edu.infnet.gestao_streaming.domain.model.Subscription;
import br.edu.infnet.gestao_streaming.domain.model.SubscriptionStatus;
import br.edu.infnet.gestao_streaming.domain.model.UpcomingBilling;
import br.edu.infnet.gestao_streaming.repository.SubscriptionRepository;
import br.edu.infnet.gestao_streaming.service.BillingService;
import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import org.junit.jupiter.api.Test;

class BillingServiceUnitTests {

  private final SubscriptionRepository subscriptions = RepositoryTestDoubles.subscriptions();
  private final BillingService service =
      new BillingService(
          subscriptions, Clock.fixed(Instant.parse("2026-06-20T00:00:00Z"), ZoneId.of("UTC")));

  @Test
  void listsMonthlyAndAnnualUpcomingBillingsInsideWindow() {
    subscriptions.save(
        subscription(1L, BillingCycle.MENSAL, LocalDate.of(2026, 5, 25), SubscriptionStatus.ATIVA));
    subscriptions.save(
        subscription(2L, BillingCycle.ANUAL, LocalDate.of(2025, 6, 22), SubscriptionStatus.ATIVA));
    subscriptions.save(
        subscription(
            3L, BillingCycle.MENSAL, LocalDate.of(2026, 6, 23), SubscriptionStatus.CANCELADA));

    List<UpcomingBilling> upcoming = service.listUpcoming(10L, 7);

    assertThat(upcoming).hasSize(2);
    assertThat(upcoming).extracting(UpcomingBilling::subscriptionId).containsExactly(2L, 1L);
    assertThat(upcoming)
        .extracting(UpcomingBilling::dueDate)
        .containsExactly(LocalDate.of(2026, 6, 22), LocalDate.of(2026, 6, 25));
  }

  @Test
  void rejectsNegativeWindow() {
    assertThatThrownBy(() -> service.listUpcoming(10L, -1))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("days must be zero or positive.");
  }

  private Subscription subscription(
      Long id, BillingCycle billingCycle, LocalDate billingDate, SubscriptionStatus status) {
    return new Subscription(
        id, 10L, 1L, BigDecimal.valueOf(29.90), billingCycle, billingDate, status);
  }
}
