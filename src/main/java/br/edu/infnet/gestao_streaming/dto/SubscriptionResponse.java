package br.edu.infnet.gestao_streaming.dto;

import br.edu.infnet.gestao_streaming.model.BillingCycle;
import br.edu.infnet.gestao_streaming.model.Subscription;
import br.edu.infnet.gestao_streaming.model.SubscriptionStatus;
import java.math.BigDecimal;
import java.time.LocalDate;

public record SubscriptionResponse(
    Long id,
    Long userId,
    Long streamingServiceId,
    BigDecimal amount,
    BillingCycle billingCycle,
    LocalDate billingDate,
    SubscriptionStatus status) {

  public static SubscriptionResponse from(Subscription subscription) {
    return new SubscriptionResponse(
        subscription.id(),
        subscription.userId(),
        subscription.streamingServiceId(),
        subscription.amount(),
        subscription.billingCycle(),
        subscription.billingDate(),
        subscription.status());
  }
}
