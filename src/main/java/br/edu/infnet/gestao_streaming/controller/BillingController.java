package br.edu.infnet.gestao_streaming.controller;

import br.edu.infnet.gestao_streaming.dto.UpcomingBillingResponse;
import br.edu.infnet.gestao_streaming.service.BillingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Cobrancas", description = "Vencimentos proximos de assinaturas")
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class BillingController {

  private final BillingService service;

  @GetMapping("/users/{userId}/billing/upcoming")
  @Operation(
      summary = "Listar proximos vencimentos",
      description = "Retorna assinaturas ativas com cobranca dentro da janela informada.")
  List<UpcomingBillingResponse> listUpcoming(
      @PathVariable Long userId, @RequestParam(required = false) Integer days) {
    return service.listUpcoming(userId, days).stream().map(UpcomingBillingResponse::from).toList();
  }
}
