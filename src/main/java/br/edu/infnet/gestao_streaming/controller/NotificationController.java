package br.edu.infnet.gestao_streaming.controller;

import br.edu.infnet.gestao_streaming.dto.NotificationResponse;
import br.edu.infnet.gestao_streaming.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Notificacoes", description = "Alertas internos simulados")
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class NotificationController {

  private final NotificationService service;

  @GetMapping("/users/{userId}/notifications")
  @Operation(
      summary = "Listar notificacoes",
      description = "Retorna notificacoes internas geradas para o usuario.")
  List<NotificationResponse> listByUser(@PathVariable Long userId) {
    return service.listByUser(userId).stream().map(NotificationResponse::from).toList();
  }

  @PostMapping("/users/{userId}/notifications/generate")
  @Operation(
      summary = "Gerar notificacoes de vencimento",
      description = "Cria notificacoes internas para assinaturas com vencimento proximo.")
  List<NotificationResponse> generate(
      @PathVariable Long userId, @RequestParam(required = false) Integer days) {
    return service.generate(userId, days).stream().map(NotificationResponse::from).toList();
  }
}
