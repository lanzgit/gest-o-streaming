package br.edu.infnet.gestao_streaming.domain.factory;

import br.edu.infnet.gestao_streaming.domain.command.CreateStreamingServiceCommand;
import br.edu.infnet.gestao_streaming.domain.model.StreamingService;
import org.springframework.stereotype.Component;

@Component
public class StreamingServiceFactory implements StreamingServiceCreator {

  @Override
  public StreamingService create(CreateStreamingServiceCommand command) {
    if (command == null) {
      throw new IllegalArgumentException("Create streaming service command is required.");
    }

    if (isBlank(command.name())) {
      throw new IllegalArgumentException("Service name is required.");
    }

    if (isBlank(command.category())) {
      throw new IllegalArgumentException("Service category is required.");
    }

    return new StreamingService(null, command.name().trim(), command.category().trim());
  }

  private boolean isBlank(String value) {
    return value == null || value.isBlank();
  }
}
