package br.edu.infnet.gestao_streaming.repository;

import br.edu.infnet.gestao_streaming.model.Notification;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface NotificationRepository {

  Notification save(Notification notification);

  List<Notification> findByUserId(Long userId);

  Optional<Notification> findBySubscriptionIdAndDueDate(Long subscriptionId, LocalDate dueDate);
}
