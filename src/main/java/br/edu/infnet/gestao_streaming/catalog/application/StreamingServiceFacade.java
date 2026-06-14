package br.edu.infnet.gestao_streaming.catalog.application;

import br.edu.infnet.gestao_streaming.catalog.domain.StreamingService;
import br.edu.infnet.gestao_streaming.catalog.domain.StreamingServiceFactory;
import br.edu.infnet.gestao_streaming.catalog.domain.StreamingServiceRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StreamingServiceFacade {

	private final StreamingServiceFactory factory;
	private final StreamingServiceRepository repository;

	public StreamingServiceFacade(StreamingServiceFactory factory, StreamingServiceRepository repository) {
		this.factory = factory;
		this.repository = repository;
	}

	public StreamingServiceResponse create(CreateStreamingServiceCommand command) {
		StreamingService streamingService = factory.create(command.name(), command.category());
		return StreamingServiceResponse.from(repository.save(streamingService));
	}

	public List<StreamingServiceResponse> list() {
		return repository.findAll().stream()
				.map(StreamingServiceResponse::from)
				.toList();
	}
}
