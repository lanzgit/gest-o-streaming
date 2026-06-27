package br.edu.infnet.gestao_streaming.repository;

import br.edu.infnet.gestao_streaming.domain.model.Usage;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsageRepository extends JpaRepository<Usage, Long> {

  List<Usage> findByUserIdOrderBySubscriptionIdAsc(Long userId);

  Optional<Usage> findByUserIdAndSubscriptionId(Long userId, Long subscriptionId);
}
