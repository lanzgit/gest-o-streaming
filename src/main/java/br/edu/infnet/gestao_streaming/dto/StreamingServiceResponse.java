package br.edu.infnet.gestao_streaming.dto;

import br.edu.infnet.gestao_streaming.model.StreamingService;

public record StreamingServiceResponse(Long id, String name, String category) {

	public static StreamingServiceResponse from(StreamingService streamingService) {
		return new StreamingServiceResponse(
				streamingService.id(),
				streamingService.name(),
				streamingService.category());
	}
}
