package br.edu.infnet.gestao_streaming.dto;

import br.edu.infnet.gestao_streaming.domain.model.Usage;
import br.edu.infnet.gestao_streaming.domain.model.UsageLevel;
import java.time.LocalDateTime;

public record UsageResponse(
    Long id, Long userId, Long subscriptionId, UsageLevel level, LocalDateTime updatedAt) {

  public static UsageResponse from(Usage usage) {
    return new UsageResponse(
        usage.id(), usage.userId(), usage.subscriptionId(), usage.level(), usage.updatedAt());
  }
}
