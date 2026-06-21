package br.edu.infnet.gestao_streaming.dto;

import br.edu.infnet.gestao_streaming.model.PaymentStatus;
import br.edu.infnet.gestao_streaming.service.RegisterPaymentCommand;
import java.math.BigDecimal;
import java.time.LocalDate;

public record RegisterPaymentRequest(BigDecimal amount, LocalDate paidAt, PaymentStatus status) {

  public RegisterPaymentCommand toCommand(Long userId, Long subscriptionId) {
    return new RegisterPaymentCommand(userId, subscriptionId, amount, paidAt, status);
  }
}
