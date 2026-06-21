package br.edu.infnet.gestao_streaming.domain.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public record SubscriptionDraft(
    Long userId,
    Long streamingServiceId,
    BigDecimal amount,
    BillingCycle billingCycle,
    LocalDate billingDate) {}
