package br.edu.infnet.gestao_streaming;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import br.edu.infnet.gestao_streaming.model.BillingCycle;
import br.edu.infnet.gestao_streaming.model.Payment;
import br.edu.infnet.gestao_streaming.model.PaymentStatus;
import br.edu.infnet.gestao_streaming.model.Subscription;
import br.edu.infnet.gestao_streaming.model.SubscriptionStatus;
import br.edu.infnet.gestao_streaming.repository.PaymentRepository;
import br.edu.infnet.gestao_streaming.repository.SubscriptionRepository;
import br.edu.infnet.gestao_streaming.service.PaymentService;
import br.edu.infnet.gestao_streaming.service.RegisterPaymentCommand;
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

class PaymentServiceUnitTests {

  private final FakePaymentRepository payments = new FakePaymentRepository();
  private final FakeSubscriptionRepository subscriptions = new FakeSubscriptionRepository();
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

  private static class FakePaymentRepository implements PaymentRepository {

    private final List<Payment> payments = new ArrayList<>();
    private long sequence;

    @Override
    public Payment save(Payment payment) {
      Payment savedPayment =
          new Payment(
              ++sequence,
              payment.userId(),
              payment.subscriptionId(),
              payment.amount(),
              payment.paidAt(),
              payment.status(),
              payment.createdAt());
      payments.add(savedPayment);
      return savedPayment;
    }

    @Override
    public List<Payment> findByUserId(Long userId) {
      return payments.stream()
          .filter(payment -> payment.userId().equals(userId))
          .sorted(Comparator.comparing(Payment::paidAt).thenComparing(Payment::id))
          .toList();
    }

    @Override
    public List<Payment> findByUserIdAndSubscriptionId(Long userId, Long subscriptionId) {
      return payments.stream()
          .filter(payment -> payment.userId().equals(userId))
          .filter(payment -> payment.subscriptionId().equals(subscriptionId))
          .sorted(Comparator.comparing(Payment::paidAt).thenComparing(Payment::id))
          .toList();
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
