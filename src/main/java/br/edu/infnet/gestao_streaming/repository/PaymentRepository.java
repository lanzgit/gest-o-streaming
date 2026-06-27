package br.edu.infnet.gestao_streaming.repository;

import br.edu.infnet.gestao_streaming.domain.model.Payment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

  List<Payment> findByUserIdOrderByPaidAtAscIdAsc(Long userId);

  List<Payment> findByUserIdAndSubscriptionIdOrderByPaidAtAscIdAsc(
      Long userId, Long subscriptionId);
}
