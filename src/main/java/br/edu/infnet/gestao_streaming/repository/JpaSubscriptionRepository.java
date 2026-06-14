package br.edu.infnet.gestao_streaming.repository;

import br.edu.infnet.gestao_streaming.model.Subscription;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Profile("!test")
interface JpaSubscriptionRepository extends JpaRepository<Subscription, Long> {

	List<Subscription> findByUserIdOrderById(Long userId);
}
