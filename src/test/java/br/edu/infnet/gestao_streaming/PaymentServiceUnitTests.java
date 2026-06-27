package br.edu.infnet.gestao_streaming;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import br.edu.infnet.gestao_streaming.domain.command.RegisterPaymentCommand;
import br.edu.infnet.gestao_streaming.domain.model.BillingCycle;
import br.edu.infnet.gestao_streaming.domain.model.Payment;
import br.edu.infnet.gestao_streaming.domain.model.PaymentStatus;
import br.edu.infnet.gestao_streaming.domain.model.Subscription;
import br.edu.infnet.gestao_streaming.domain.model.SubscriptionStatus;
import br.edu.infnet.gestao_streaming.repository.PaymentRepository;
import br.edu.infnet.gestao_streaming.repository.SubscriptionRepository;
import br.edu.infnet.gestao_streaming.service.PaymentService;
import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import org.junit.jupiter.api.Test;

class PaymentServiceUnitTests {

  private final PaymentRepository payments = RepositoryTestDoubles.payments();
  private final SubscriptionRepository subscriptions = RepositoryTestDoubles.subscriptions();
  private final PaymentService service =
      new PaymentService(
          payments,
          subscriptions,
          Clock.fixed(Instant.parse("2026-06-20T00:00:00Z"), ZoneId.of("UTC")));

  @Test
  void registersConfirmedPaymentByDefault() {
    subscriptions.save(subscription(1L, 10L));

    Payment payment =
        service.register(
            new RegisterPaymentCommand(
                10L, 1L, BigDecimal.valueOf(29.90), LocalDate.of(2026, 6, 20), null));

    assertThat(payment.id()).isEqualTo(1L);
    assertThat(payment.status()).isEqualTo(PaymentStatus.CONFIRMADO);
    assertThat(service.listByUser(10L)).hasSize(1);
    assertThat(service.listBySubscription(10L, 1L)).hasSize(1);
  }

  @Test
  void rejectsFuturePaymentDate() {
    subscriptions.save(subscription(1L, 10L));

    assertThatThrownBy(
            () ->
                service.register(
                    new RegisterPaymentCommand(
                        10L, 1L, BigDecimal.TEN, LocalDate.of(2026, 6, 21), null)))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Payment date cannot be in the future.");
  }

  @Test
  void rejectsPaymentForSubscriptionFromAnotherUser() {
    subscriptions.save(subscription(1L, 20L));

    assertThatThrownBy(
            () ->
                service.register(
                    new RegisterPaymentCommand(
                        10L, 1L, BigDecimal.TEN, LocalDate.of(2026, 6, 20), null)))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Subscription not found for user.");
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
