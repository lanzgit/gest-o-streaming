package br.edu.infnet.gestao_streaming.service;

import br.edu.infnet.gestao_streaming.model.UsageLevel;

public record UpdateUsageCommand(Long userId, Long subscriptionId, UsageLevel level) {}
