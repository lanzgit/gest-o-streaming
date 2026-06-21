package br.edu.infnet.gestao_streaming.repository;

import br.edu.infnet.gestao_streaming.model.Usage;
import java.util.List;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

@Repository
@Profile("!test")
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class PostgresUsageRepository implements UsageRepository {

  private final JpaUsageRepository jpaRepository;

  @Override
  public Usage save(Usage usage) {
    return jpaRepository.save(usage);
  }

  @Override
  public List<Usage> findByUserId(Long userId) {
    return jpaRepository.findByUserIdOrderBySubscriptionIdAsc(userId);
  }

  @Override
  public Optional<Usage> findByUserIdAndSubscriptionId(Long userId, Long subscriptionId) {
    return jpaRepository.findByUserIdAndSubscriptionId(userId, subscriptionId);
  }
}
