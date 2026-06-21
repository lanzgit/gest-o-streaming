package br.edu.infnet.gestao_streaming.dto;

import br.edu.infnet.gestao_streaming.model.UsageLevel;
import br.edu.infnet.gestao_streaming.service.UpdateUsageCommand;

public record UpdateUsageRequest(UsageLevel level) {

  public UpdateUsageCommand toCommand(Long userId, Long subscriptionId) {
    return new UpdateUsageCommand(userId, subscriptionId, level);
  }
}
