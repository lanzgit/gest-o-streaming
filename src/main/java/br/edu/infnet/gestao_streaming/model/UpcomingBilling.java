package br.edu.infnet.gestao_streaming.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public record UpcomingBilling(
    Long subscriptionId,
    Long userId,
    Long streamingServiceId,
    BigDecimal amount,
    BillingCycle billingCycle,
    LocalDate dueDate,
    long daysUntilDue) {}
