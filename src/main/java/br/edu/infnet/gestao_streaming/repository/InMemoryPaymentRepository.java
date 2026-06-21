package br.edu.infnet.gestao_streaming.repository;

import br.edu.infnet.gestao_streaming.model.Payment;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

@Repository
@Profile("test")
class InMemoryPaymentRepository implements PaymentRepository {

  private final AtomicLong sequence = new AtomicLong();
  private final ConcurrentHashMap<Long, Payment> payments = new ConcurrentHashMap<>();

  @Override
  public Payment save(Payment payment) {
    Long id = nextId(payment);
    Payment savedPayment =
        new Payment(
            id,
            payment.userId(),
            payment.subscriptionId(),
            payment.amount(),
            payment.paidAt(),
            payment.status(),
            payment.createdAt());

    payments.put(id, savedPayment);
    return savedPayment;
  }

  @Override
  public List<Payment> findByUserId(Long userId) {
    return payments.values().stream()
        .filter(payment -> payment.userId().equals(userId))
        .sorted(Comparator.comparing(Payment::paidAt).thenComparing(Payment::id))
        .toList();
  }

  @Override
  public List<Payment> findByUserIdAndSubscriptionId(Long userId, Long subscriptionId) {
    return payments.values().stream()
        .filter(payment -> payment.userId().equals(userId))
        .filter(payment -> payment.subscriptionId().equals(subscriptionId))
        .sorted(Comparator.comparing(Payment::paidAt).thenComparing(Payment::id))
        .toList();
  }

  private Long nextId(Payment payment) {
    if (payment.id() != null) {
      return payment.id();
    }

    return sequence.incrementAndGet();
  }
}
