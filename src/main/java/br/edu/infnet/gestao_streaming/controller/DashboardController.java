package br.edu.infnet.gestao_streaming.controller;

import br.edu.infnet.gestao_streaming.dto.DashboardResponse;
import br.edu.infnet.gestao_streaming.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Dashboard", description = "Resumo consolidado de assinaturas, gastos e consumo")
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class DashboardController {

  private final DashboardService service;

  @GetMapping("/users/{userId}/dashboard")
  @Operation(
      summary = "Consultar dashboard do usuario",
      description = "Retorna gastos, uso, proximas cobrancas, notificacoes e pagamentos recentes.")
  DashboardResponse summarize(
      @PathVariable Long userId,
      @RequestParam(required = false) Integer billingWindowDays,
      @RequestParam(required = false) Integer recentPaymentsLimit) {
    return DashboardResponse.from(
        service.summarize(userId, billingWindowDays, recentPaymentsLimit));
  }
}
