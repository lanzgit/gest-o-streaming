package br.edu.infnet.gestao_streaming.finance.web;

import br.edu.infnet.gestao_streaming.finance.application.ExpenseSummaryResponse;
import br.edu.infnet.gestao_streaming.finance.application.FinanceFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Financeiro", description = "Resumo financeiro das assinaturas do usuario")
class FinanceController {

	private final FinanceFacade facade;

	FinanceController(FinanceFacade facade) {
		this.facade = facade;
	}

	@GetMapping("/users/{userId}/expenses/summary")
	@Operation(summary = "Consultar resumo financeiro", description = "Calcula totais mensal e anual das assinaturas ativas do usuario.")
	ExpenseSummaryResponse summarize(@PathVariable Long userId) {
		return facade.summarize(userId);
	}
}
