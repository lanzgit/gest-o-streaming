package br.edu.infnet.gestao_streaming.repository;

import br.edu.infnet.gestao_streaming.model.Subscription;
import java.util.List;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

@Repository
@Profile("!test")
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class PostgresSubscriptionRepository implements SubscriptionRepository {

  private final JpaSubscriptionRepository jpaRepository;

  @Override
  public Subscription save(Subscription subscription) {
    return jpaRepository.save(subscription);
  }

  @Override
  public List<Subscription> findByUserId(Long userId) {
    return jpaRepository.findByUserIdOrderById(userId);
  }

  @Override
  public Optional<Subscription> findById(Long id) {
    return jpaRepository.findById(id);
  }
}
