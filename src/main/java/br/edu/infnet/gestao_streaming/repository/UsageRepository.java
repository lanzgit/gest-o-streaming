package br.edu.infnet.gestao_streaming.repository;

import br.edu.infnet.gestao_streaming.domain.model.Usage;
import java.util.List;
import java.util.Optional;

public interface UsageRepository {

  Usage save(Usage usage);

  List<Usage> findByUserId(Long userId);

  Optional<Usage> findByUserIdAndSubscriptionId(Long userId, Long subscriptionId);
}
