package br.edu.infnet.gestao_streaming.controller;

import br.edu.infnet.gestao_streaming.dto.UpdateUsageRequest;
import br.edu.infnet.gestao_streaming.dto.UsageResponse;
import br.edu.infnet.gestao_streaming.dto.UsageSummaryResponse;
import br.edu.infnet.gestao_streaming.service.UsageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Uso", description = "Classificacao de uso das assinaturas")
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class UsageController {

  private final UsageService service;

  @PatchMapping("/users/{userId}/subscriptions/{subscriptionId}/usage")
  @Operation(
      summary = "Classificar uso da assinatura",
      description = "Define a assinatura como frequente, rara ou nao usada.")
  UsageResponse update(
      @PathVariable Long userId,
      @PathVariable Long subscriptionId,
      @RequestBody UpdateUsageRequest request) {
    return UsageResponse.from(service.update(request.toCommand(userId, subscriptionId)));
  }

  @GetMapping("/users/{userId}/subscriptions/usage")
  @Operation(
      summary = "Listar classificacoes de uso",
      description = "Retorna as classificacoes de uso das assinaturas do usuario.")
  List<UsageResponse> listByUser(@PathVariable Long userId) {
    return service.listByUser(userId).stream().map(UsageResponse::from).toList();
  }

  @GetMapping("/users/{userId}/subscriptions/usage/low")
  @Operation(
      summary = "Listar assinaturas pouco usadas",
      description = "Retorna assinaturas classificadas como raras ou nao usadas.")
  List<UsageResponse> listLowUsage(@PathVariable Long userId) {
    return service.listLowUsage(userId).stream().map(UsageResponse::from).toList();
  }

  @GetMapping("/users/{userId}/subscriptions/usage-summary")
  @Operation(
      summary = "Resumo de uso das assinaturas",
      description = "Retorna a contagem de assinaturas por nivel de uso.")
  UsageSummaryResponse summarize(@PathVariable Long userId) {
    return UsageSummaryResponse.from(service.summarize(userId));
  }
}
