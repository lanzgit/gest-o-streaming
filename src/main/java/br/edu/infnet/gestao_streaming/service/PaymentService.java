package br.edu.infnet.gestao_streaming.service;

import br.edu.infnet.gestao_streaming.domain.command.RegisterPaymentCommand;
import br.edu.infnet.gestao_streaming.domain.model.Payment;
import br.edu.infnet.gestao_streaming.domain.model.PaymentStatus;
import br.edu.infnet.gestao_streaming.domain.model.Subscription;
import br.edu.infnet.gestao_streaming.repository.PaymentRepository;
import br.edu.infnet.gestao_streaming.repository.SubscriptionRepository;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {

  private final PaymentRepository payments;
  private final SubscriptionRepository subscriptions;
  private final Clock clock;

  public Payment register(RegisterPaymentCommand command) {
    validate(command);
    Subscription subscription = findUserSubscription(command.userId(), command.subscriptionId());

    Payment payment =
        new Payment(
            null,
            command.userId(),
            subscription.id(),
            command.amount(),
            command.paidAt(),
            resolveStatus(command.status()),
            LocalDateTime.now(clock));

    return payments.save(payment);
  }

  public List<Payment> listByUser(Long userId) {
    return payments.findByUserId(userId);
  }

  public List<Payment> listBySubscription(Long userId, Long subscriptionId) {
    findUserSubscription(userId, subscriptionId);
    return payments.findByUserIdAndSubscriptionId(userId, subscriptionId);
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

  private void validate(RegisterPaymentCommand command) {
    if (command.userId() == null) {
      throw new IllegalArgumentException("User id is required.");
    }

    if (command.subscriptionId() == null) {
      throw new IllegalArgumentException("Subscription id is required.");
    }

    if (command.amount() == null || command.amount().signum() <= 0) {
      throw new IllegalArgumentException("Payment amount must be positive.");
    }

    if (command.paidAt() == null) {
      throw new IllegalArgumentException("Payment date is required.");
    }

    if (command.paidAt().isAfter(LocalDate.now(clock))) {
      throw new IllegalArgumentException("Payment date cannot be in the future.");
    }
  }

  private PaymentStatus resolveStatus(PaymentStatus status) {
    if (status == null) {
      return PaymentStatus.CONFIRMADO;
    }
    return status;
  }
}
