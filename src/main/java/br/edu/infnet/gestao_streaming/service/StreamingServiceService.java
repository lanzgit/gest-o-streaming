package br.edu.infnet.gestao_streaming.service;

import br.edu.infnet.gestao_streaming.model.StreamingService;
import br.edu.infnet.gestao_streaming.repository.StreamingServiceRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StreamingServiceService {

	private final StreamingServiceFactory factory;
	private final StreamingServiceRepository repository;

	public StreamingServiceService(StreamingServiceFactory factory, StreamingServiceRepository repository) {
		this.factory = factory;
		this.repository = repository;
	}

	public StreamingService create(CreateStreamingServiceCommand command) {
		StreamingService streamingService = factory.create(command.name(), command.category());
		return repository.save(streamingService);
	}

	public List<StreamingService> list() {
		return repository.findAll();
	}
}
