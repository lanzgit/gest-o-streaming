package br.edu.infnet.gestao_streaming.controller;

import br.edu.infnet.gestao_streaming.dto.CreateStreamingServiceRequest;
import br.edu.infnet.gestao_streaming.dto.StreamingServiceResponse;
import br.edu.infnet.gestao_streaming.service.StreamingServiceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Catalogo", description = "Servicos de streaming disponiveis para assinatura")
class StreamingServiceController {

  private final StreamingServiceService service;

  StreamingServiceController(StreamingServiceService service) {
    this.service = service;
  }

  @PostMapping("/streaming-services")
  @ResponseStatus(HttpStatus.CREATED)
  @Operation(
      summary = "Cadastrar servico de streaming",
      description = "Cria um servico de streaming no catalogo do sistema.")
  StreamingServiceResponse create(@RequestBody CreateStreamingServiceRequest request) {
    return StreamingServiceResponse.from(service.create(request.toCommand()));
  }

  @GetMapping("/streaming-services")
  @Operation(
      summary = "Listar servicos de streaming",
      description = "Retorna todos os servicos cadastrados no catalogo.")
  List<StreamingServiceResponse> list() {
    return service.list().stream().map(StreamingServiceResponse::from).toList();
  }
}
