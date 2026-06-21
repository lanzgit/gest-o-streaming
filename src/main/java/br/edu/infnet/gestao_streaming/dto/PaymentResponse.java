package br.edu.infnet.gestao_streaming.dto;

import br.edu.infnet.gestao_streaming.domain.model.Payment;
import br.edu.infnet.gestao_streaming.domain.model.PaymentStatus;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record PaymentResponse(
    Long id,
    Long userId,
    Long subscriptionId,
    BigDecimal amount,
    LocalDate paidAt,
    PaymentStatus status,
    LocalDateTime createdAt) {

  public static PaymentResponse from(Payment payment) {
    return new PaymentResponse(
        payment.id(),
        payment.userId(),
        payment.subscriptionId(),
        payment.amount(),
        payment.paidAt(),
        payment.status(),
        payment.createdAt());
  }
}
