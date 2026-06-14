package br.edu.infnet.gestao_streaming.catalog.infrastructure;

import br.edu.infnet.gestao_streaming.catalog.domain.StreamingService;
import br.edu.infnet.gestao_streaming.catalog.domain.StreamingServiceRepository;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
class InMemoryStreamingServiceRepository implements StreamingServiceRepository {

	private final AtomicLong sequence = new AtomicLong();
	private final ConcurrentHashMap<Long, StreamingService> services = new ConcurrentHashMap<>();

	@Override
	public StreamingService save(StreamingService streamingService) {
		Long id = nextId(streamingService);
		StreamingService savedService = new StreamingService(id, streamingService.name(), streamingService.category());
		services.put(id, savedService);
		return savedService;
	}

	@Override
	public List<StreamingService> findAll() {
		return services.values().stream()
				.sorted(Comparator.comparing(StreamingService::id))
				.toList();
	}

	@Override
	public Optional<StreamingService> findById(Long id) {
		return Optional.ofNullable(services.get(id));
	}

	private Long nextId(StreamingService streamingService) {
		if (streamingService.id() != null) {
			return streamingService.id();
		}

		return sequence.incrementAndGet();
	}
}
