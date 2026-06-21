package br.edu.infnet.gestao_streaming.repository;

import br.edu.infnet.gestao_streaming.domain.model.Subscription;
import java.util.List;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

@Profile("!test")
interface JpaSubscriptionRepository extends JpaRepository<Subscription, Long> {

  List<Subscription> findByUserIdOrderById(Long userId);
}
