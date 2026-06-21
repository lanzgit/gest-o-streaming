package br.edu.infnet.gestao_streaming.service;

import br.edu.infnet.gestao_streaming.model.PaymentStatus;
import java.math.BigDecimal;
import java.time.LocalDate;

public record RegisterPaymentCommand(
    Long userId, Long subscriptionId, BigDecimal amount, LocalDate paidAt, PaymentStatus status) {}
