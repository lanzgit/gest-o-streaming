package br.edu.infnet.gestao_streaming.domain.command;

import br.edu.infnet.gestao_streaming.domain.model.PaymentStatus;
import java.math.BigDecimal;
import java.time.LocalDate;

public record RegisterPaymentCommand(
    Long userId, Long subscriptionId, BigDecimal amount, LocalDate paidAt, PaymentStatus status) {}
