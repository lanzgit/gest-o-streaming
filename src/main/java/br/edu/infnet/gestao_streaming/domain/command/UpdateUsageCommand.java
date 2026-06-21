package br.edu.infnet.gestao_streaming.domain.command;

import br.edu.infnet.gestao_streaming.domain.model.UsageLevel;

public record UpdateUsageCommand(Long userId, Long subscriptionId, UsageLevel level) {}
