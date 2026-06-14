package br.edu.infnet.gestao_streaming.controller;

import br.edu.infnet.gestao_streaming.dto.ExpenseSummaryResponse;
import br.edu.infnet.gestao_streaming.service.FinanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Financeiro", description = "Resumo financeiro das assinaturas do usuario")
class FinanceController {

  private final FinanceService service;

  FinanceController(FinanceService service) {
    this.service = service;
  }

  @GetMapping("/users/{userId}/expenses/summary")
  @Operation(
      summary = "Consultar resumo financeiro",
      description = "Calcula totais mensal e anual das assinaturas ativas do usuario.")
  ExpenseSummaryResponse summarize(@PathVariable Long userId) {
    return ExpenseSummaryResponse.from(service.summarize(userId));
  }
}
