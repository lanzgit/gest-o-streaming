package br.edu.infnet.gestao_streaming.repository;

import br.edu.infnet.gestao_streaming.model.Payment;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

@Repository
@Profile("!test")
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class PostgresPaymentRepository implements PaymentRepository {

  private final JpaPaymentRepository jpaRepository;

  @Override
  public Payment save(Payment payment) {
    return jpaRepository.save(payment);
  }

  @Override
  public List<Payment> findByUserId(Long userId) {
    return jpaRepository.findByUserIdOrderByPaidAtAscIdAsc(userId);
  }

  @Override
  public List<Payment> findByUserIdAndSubscriptionId(Long userId, Long subscriptionId) {
    return jpaRepository.findByUserIdAndSubscriptionIdOrderByPaidAtAscIdAsc(userId, subscriptionId);
  }
}
