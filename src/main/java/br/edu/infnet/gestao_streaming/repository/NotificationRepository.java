package br.edu.infnet.gestao_streaming.repository;

import br.edu.infnet.gestao_streaming.domain.model.Notification;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

  List<Notification> findByUserIdOrderByCreatedAtAscIdAsc(Long userId);

  Optional<Notification> findBySubscriptionIdAndDueDate(Long subscriptionId, LocalDate dueDate);
}
