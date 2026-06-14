package br.edu.infnet.gestao_streaming.catalog.web;

import br.edu.infnet.gestao_streaming.catalog.application.StreamingServiceFacade;
import br.edu.infnet.gestao_streaming.catalog.application.StreamingServiceResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Tag(name = "Catalogo", description = "Servicos de streaming disponiveis para assinatura")
class StreamingServiceController {

	private final StreamingServiceFacade facade;

	StreamingServiceController(StreamingServiceFacade facade) {
		this.facade = facade;
	}

	@PostMapping("/streaming-services")
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(summary = "Cadastrar servico de streaming", description = "Cria um servico de streaming no catalogo do sistema.")
	StreamingServiceResponse create(@RequestBody CreateStreamingServiceRequest request) {
		return facade.create(request.toCommand());
	}

	@GetMapping("/streaming-services")
	@Operation(summary = "Listar servicos de streaming", description = "Retorna todos os servicos cadastrados no catalogo.")
	List<StreamingServiceResponse> list() {
		return facade.list();
	}
}
