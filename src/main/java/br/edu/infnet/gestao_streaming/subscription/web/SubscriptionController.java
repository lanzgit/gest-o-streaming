package br.edu.infnet.gestao_streaming.subscription.web;

import br.edu.infnet.gestao_streaming.subscription.application.SubscriptionFacade;
import br.edu.infnet.gestao_streaming.subscription.application.SubscriptionResponse;
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

	private final SubscriptionFacade facade;

	SubscriptionController(SubscriptionFacade facade) {
		this.facade = facade;
	}

	@PostMapping("/users/{userId}/subscriptions")
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(summary = "Cadastrar assinatura", description = "Cria uma assinatura ativa para um usuario.")
	SubscriptionResponse create(
			@PathVariable Long userId,
			@RequestBody CreateSubscriptionRequest request) {
		return facade.create(request.toCommand(userId));
	}

	@GetMapping("/users/{userId}/subscriptions")
	@Operation(summary = "Listar assinaturas do usuario", description = "Retorna as assinaturas cadastradas para um usuario.")
	List<SubscriptionResponse> list(@PathVariable Long userId) {
		return facade.listByUser(userId);
	}
}
