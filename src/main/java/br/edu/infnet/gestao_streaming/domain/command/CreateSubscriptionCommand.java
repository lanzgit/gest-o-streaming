package br.edu.infnet.gestao_streaming.domain.command;

import br.edu.infnet.gestao_streaming.domain.model.BillingCycle;
import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateSubscriptionCommand(
    Long userId,
    Long streamingServiceId,
    BigDecimal amount,
    BillingCycle billingCycle,
    LocalDate billingDate) {}
