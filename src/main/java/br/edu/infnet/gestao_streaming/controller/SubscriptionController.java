package br.edu.infnet.gestao_streaming.controller;

import br.edu.infnet.gestao_streaming.dto.CreateSubscriptionRequest;
import br.edu.infnet.gestao_streaming.dto.SubscriptionResponse;
import br.edu.infnet.gestao_streaming.service.SubscriptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Assinaturas", description = "Assinaturas de streaming vinculadas ao usuario")
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class SubscriptionController {

  private final SubscriptionService service;

  @PostMapping("/users/{userId}/subscriptions")
  @ResponseStatus(HttpStatus.CREATED)
  @Operation(
      summary = "Cadastrar assinatura",
      description = "Cria uma assinatura ativa para um usuario.")
  SubscriptionResponse create(
      @PathVariable Long userId, @RequestBody CreateSubscriptionRequest request) {
    return SubscriptionResponse.from(service.create(request.toCommand(userId)));
  }

  @GetMapping("/users/{userId}/subscriptions")
  @Operation(
      summary = "Listar assinaturas do usuario",
      description = "Retorna as assinaturas cadastradas para um usuario.")
  List<SubscriptionResponse> list(@PathVariable Long userId) {
    return service.listByUser(userId).stream().map(SubscriptionResponse::from).toList();
  }

  @PatchMapping("/users/{userId}/subscriptions/{subscriptionId}/cancel")
  @Operation(
      summary = "Cancelar assinatura",
      description = "Cancela uma assinatura ativa vinculada ao usuario.")
  SubscriptionResponse cancel(@PathVariable Long userId, @PathVariable Long subscriptionId) {
    return SubscriptionResponse.from(service.cancel(userId, subscriptionId));
  }
}
