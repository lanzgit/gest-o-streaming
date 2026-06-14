package br.edu.infnet.gestao_streaming.repository;

import br.edu.infnet.gestao_streaming.model.Subscription;
import java.util.List;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

@Repository
@Profile("!test")
class PostgresSubscriptionRepository implements SubscriptionRepository {

  private final JpaSubscriptionRepository jpaRepository;

  PostgresSubscriptionRepository(JpaSubscriptionRepository jpaRepository) {
    this.jpaRepository = jpaRepository;
  }

  @Override
  public Subscription save(Subscription subscription) {
    return jpaRepository.save(subscription);
  }

  @Override
  public List<Subscription> findByUserId(Long userId) {
    return jpaRepository.findByUserIdOrderById(userId);
  }
}
