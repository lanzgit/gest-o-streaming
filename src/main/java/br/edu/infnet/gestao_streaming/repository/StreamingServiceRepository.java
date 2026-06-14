package br.edu.infnet.gestao_streaming.repository;

import br.edu.infnet.gestao_streaming.model.StreamingService;
import java.util.List;
import java.util.Optional;

public interface StreamingServiceRepository {

  StreamingService save(StreamingService streamingService);

  List<StreamingService> findAll();

  Optional<StreamingService> findById(Long id);
}
