package br.edu.infnet.gestao_streaming.dto;

import br.edu.infnet.gestao_streaming.domain.command.UpdateUsageCommand;
import br.edu.infnet.gestao_streaming.domain.model.UsageLevel;

public record UpdateUsageRequest(UsageLevel level) {

  public UpdateUsageCommand toCommand(Long userId, Long subscriptionId) {
    return new UpdateUsageCommand(userId, subscriptionId, level);
  }
}
