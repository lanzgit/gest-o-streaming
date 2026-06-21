package br.edu.infnet.gestao_streaming.repository;

import br.edu.infnet.gestao_streaming.domain.model.Notification;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

@Repository
@Profile("!test")
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class PostgresNotificationRepository implements NotificationRepository {

  private final JpaNotificationRepository jpaRepository;

  @Override
  public Notification save(Notification notification) {
    return jpaRepository.save(notification);
  }

  @Override
  public List<Notification> findByUserId(Long userId) {
    return jpaRepository.findByUserIdOrderByCreatedAtAscIdAsc(userId);
  }

  @Override
  public Optional<Notification> findBySubscriptionIdAndDueDate(
      Long subscriptionId, LocalDate dueDate) {
    return jpaRepository.findBySubscriptionIdAndDueDate(subscriptionId, dueDate);
  }
}
