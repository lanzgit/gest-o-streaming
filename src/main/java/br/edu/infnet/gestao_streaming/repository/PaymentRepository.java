package br.edu.infnet.gestao_streaming.repository;

import br.edu.infnet.gestao_streaming.model.Payment;
import java.util.List;

public interface PaymentRepository {

  Payment save(Payment payment);

  List<Payment> findByUserId(Long userId);

  List<Payment> findByUserIdAndSubscriptionId(Long userId, Long subscriptionId);
}
