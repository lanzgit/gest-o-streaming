package br.edu.infnet.gestao_streaming.controller;

import br.edu.infnet.gestao_streaming.dto.ExternalMovieSearchResponse;
import br.edu.infnet.gestao_streaming.dto.ExternalStreamingProviderResponse;
import br.edu.infnet.gestao_streaming.service.ExternalCatalogService;
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
@Tag(name = "Catalogo externo", description = "Consulta catalogos externos pela API do TMDB")
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class ExternalCatalogController {

  private final ExternalCatalogService service;

  @GetMapping("/external/tmdb/movie-providers")
  @Operation(
      summary = "Listar provedores de filmes",
      description = "Retorna provedores de streaming de filmes disponiveis na regiao informada.")
  List<ExternalStreamingProviderResponse> listMovieProviders(
      @RequestParam(required = false) String region) {
    return service.listMovieProviders(region);
  }

  @GetMapping("/external/tmdb/movie-providers/{providerId}/movies")
  @Operation(
      summary = "Listar filmes por provedor",
      description =
          "Retorna filmes populares disponiveis por assinatura no provedor e regiao informados.")
  ExternalMovieSearchResponse discoverMoviesByProvider(
      @PathVariable Integer providerId, @RequestParam(required = false) String region) {
    return service.discoverMoviesByProvider(providerId, region);
  }
}
