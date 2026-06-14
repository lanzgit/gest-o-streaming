package br.edu.infnet.gestao_streaming.catalog.application;

import br.edu.infnet.gestao_streaming.catalog.domain.StreamingService;

public record StreamingServiceResponse(Long id, String name, String category) {

	static StreamingServiceResponse from(StreamingService streamingService) {
		return new StreamingServiceResponse(
				streamingService.id(),
				streamingService.name(),
				streamingService.category());
	}
}
