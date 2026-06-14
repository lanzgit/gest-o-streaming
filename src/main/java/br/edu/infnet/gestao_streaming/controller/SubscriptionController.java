package br.edu.infnet.gestao_streaming.controller;

import br.edu.infnet.gestao_streaming.dto.CreateSubscriptionRequest;
import br.edu.infnet.gestao_streaming.dto.SubscriptionResponse;
import br.edu.infnet.gestao_streaming.service.SubscriptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Tag(name = "Assinaturas", description = "Assinaturas de streaming vinculadas ao usuario")
class SubscriptionController {

	private final SubscriptionService service;

	SubscriptionController(SubscriptionService service) {
		this.service = service;
	}

	@PostMapping("/users/{userId}/subscriptions")
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(summary = "Cadastrar assinatura", description = "Cria uma assinatura ativa para um usuario.")
	SubscriptionResponse create(
			@PathVariable Long userId,
			@RequestBody CreateSubscriptionRequest request) {
		return SubscriptionResponse.from(service.create(request.toCommand(userId)));
	}

	@GetMapping("/users/{userId}/subscriptions")
	@Operation(summary = "Listar assinaturas do usuario", description = "Retorna as assinaturas cadastradas para um usuario.")
	List<SubscriptionResponse> list(@PathVariable Long userId) {
		return service.listByUser(userId).stream()
				.map(SubscriptionResponse::from)
				.toList();
	}
}
