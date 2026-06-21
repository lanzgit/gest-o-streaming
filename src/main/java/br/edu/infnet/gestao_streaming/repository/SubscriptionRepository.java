package br.edu.infnet.gestao_streaming.repository;

import br.edu.infnet.gestao_streaming.domain.model.Subscription;
import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository {

  Subscription save(Subscription subscription);

  List<Subscription> findByUserId(Long userId);

  Optional<Subscription> findById(Long id);
}
