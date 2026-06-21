package br.edu.infnet.gestao_streaming.dto;

import br.edu.infnet.gestao_streaming.domain.model.BillingCycle;
import br.edu.infnet.gestao_streaming.domain.model.UpcomingBilling;
import java.math.BigDecimal;
import java.time.LocalDate;

public record UpcomingBillingResponse(
    Long subscriptionId,
    Long userId,
    Long streamingServiceId,
    BigDecimal amount,
    BillingCycle billingCycle,
    LocalDate dueDate,
    long daysUntilDue) {

  public static UpcomingBillingResponse from(UpcomingBilling billing) {
    return new UpcomingBillingResponse(
        billing.subscriptionId(),
        billing.userId(),
        billing.streamingServiceId(),
        billing.amount(),
        billing.billingCycle(),
        billing.dueDate(),
        billing.daysUntilDue());
  }
}
