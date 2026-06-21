package br.edu.infnet.gestao_streaming.repository;

import br.edu.infnet.gestao_streaming.domain.model.Payment;
import java.util.List;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

@Profile("!test")
interface JpaPaymentRepository extends JpaRepository<Payment, Long> {

  List<Payment> findByUserIdOrderByPaidAtAscIdAsc(Long userId);

  List<Payment> findByUserIdAndSubscriptionIdOrderByPaidAtAscIdAsc(
      Long userId, Long subscriptionId);
}
