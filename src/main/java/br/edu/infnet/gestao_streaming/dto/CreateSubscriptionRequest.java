package br.edu.infnet.gestao_streaming.dto;

import br.edu.infnet.gestao_streaming.domain.command.CreateSubscriptionCommand;
import br.edu.infnet.gestao_streaming.domain.model.BillingCycle;
import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateSubscriptionRequest(
    Long streamingServiceId, BigDecimal amount, BillingCycle billingCycle, LocalDate billingDate) {

  public CreateSubscriptionCommand toCommand(Long userId) {
    return new CreateSubscriptionCommand(
        userId, streamingServiceId, amount, billingCycle, billingDate);
  }
}
