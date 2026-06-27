package br.edu.infnet.gestao_streaming.service;

import br.edu.infnet.gestao_streaming.domain.command.CreateStreamingServiceCommand;
import br.edu.infnet.gestao_streaming.domain.factory.StreamingServiceCreator;
import br.edu.infnet.gestao_streaming.domain.model.StreamingService;
import br.edu.infnet.gestao_streaming.repository.StreamingServiceRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StreamingServiceService {

  private final StreamingServiceCreator factory;
  private final StreamingServiceRepository repository;

  public StreamingService create(CreateStreamingServiceCommand command) {
    StreamingService streamingService = factory.create(command);
    return repository.save(streamingService);
  }

  public List<StreamingService> list() {
    return repository.findAll();
  }
}
