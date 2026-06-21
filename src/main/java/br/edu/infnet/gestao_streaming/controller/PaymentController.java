package br.edu.infnet.gestao_streaming.controller;

import br.edu.infnet.gestao_streaming.dto.PaymentResponse;
import br.edu.infnet.gestao_streaming.dto.RegisterPaymentRequest;
import br.edu.infnet.gestao_streaming.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Pagamentos", description = "Historico manual ou simulado de pagamentos")
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class PaymentController {

  private final PaymentService service;

  @PostMapping("/users/{userId}/subscriptions/{subscriptionId}/payments")
  @ResponseStatus(HttpStatus.CREATED)
  @Operation(
      summary = "Registrar pagamento",
      description = "Registra um pagamento manual ou simulado para uma assinatura do usuario.")
  PaymentResponse register(
      @PathVariable Long userId,
      @PathVariable Long subscriptionId,
      @RequestBody RegisterPaymentRequest request) {
    return PaymentResponse.from(service.register(request.toCommand(userId, subscriptionId)));
  }

  @GetMapping("/users/{userId}/payments")
  @Operation(
      summary = "Listar pagamentos do usuario",
      description = "Retorna o historico de pagamentos de todas as assinaturas do usuario.")
  List<PaymentResponse> listByUser(@PathVariable Long userId) {
    return service.listByUser(userId).stream().map(PaymentResponse::from).toList();
  }

  @GetMapping("/users/{userId}/subscriptions/{subscriptionId}/payments")
  @Operation(
      summary = "Listar pagamentos da assinatura",
      description = "Retorna o historico de pagamentos de uma assinatura especifica.")
  List<PaymentResponse> listBySubscription(
      @PathVariable Long userId, @PathVariable Long subscriptionId) {
    return service.listBySubscription(userId, subscriptionId).stream()
        .map(PaymentResponse::from)
        .toList();
  }
}
