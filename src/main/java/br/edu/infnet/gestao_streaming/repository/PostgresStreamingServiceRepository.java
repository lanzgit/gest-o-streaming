package br.edu.infnet.gestao_streaming.repository;

import br.edu.infnet.gestao_streaming.model.StreamingService;
import java.util.List;
import java.util.Optional;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

@Repository
@Profile("!test")
class PostgresStreamingServiceRepository implements StreamingServiceRepository {

  private final JpaStreamingServiceRepository jpaRepository;

  PostgresStreamingServiceRepository(JpaStreamingServiceRepository jpaRepository) {
    this.jpaRepository = jpaRepository;
  }

  @Override
  public StreamingService save(StreamingService streamingService) {
    return jpaRepository.save(streamingService);
  }

  @Override
  public List<StreamingService> findAll() {
    return jpaRepository.findAll();
  }

  @Override
  public Optional<StreamingService> findById(Long id) {
    return jpaRepository.findById(id);
  }
}
