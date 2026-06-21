package br.edu.infnet.gestao_streaming.domain.factory;

import br.edu.infnet.gestao_streaming.domain.model.StreamingService;
import org.springframework.stereotype.Component;

@Component
public class StreamingServiceFactory {

  public StreamingService create(String name, String category) {
    if (isBlank(name)) {
      throw new IllegalArgumentException("Service name is required.");
    }

    if (isBlank(category)) {
      throw new IllegalArgumentException("Service category is required.");
    }

    return new StreamingService(null, name.trim(), category.trim());
  }

  private boolean isBlank(String value) {
    return value == null || value.isBlank();
  }
}
