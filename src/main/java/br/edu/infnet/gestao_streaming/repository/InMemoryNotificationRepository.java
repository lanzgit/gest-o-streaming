package br.edu.infnet.gestao_streaming.repository;

import br.edu.infnet.gestao_streaming.model.Notification;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

@Repository
@Profile("test")
class InMemoryNotificationRepository implements NotificationRepository {

  private final AtomicLong sequence = new AtomicLong();
  private final ConcurrentHashMap<Long, Notification> notifications = new ConcurrentHashMap<>();

  @Override
  public Notification save(Notification notification) {
    Long id = nextId(notification);
    Notification savedNotification =
        new Notification(
            id,
            notification.userId(),
            notification.subscriptionId(),
            notification.dueDate(),
            notification.message(),
            notification.status(),
            notification.createdAt());

    notifications.put(id, savedNotification);
    return savedNotification;
  }

  @Override
  public List<Notification> findByUserId(Long userId) {
    return notifications.values().stream()
        .filter(notification -> notification.userId().equals(userId))
        .sorted(Comparator.comparing(Notification::createdAt).thenComparing(Notification::id))
        .toList();
  }

  @Override
  public Optional<Notification> findBySubscriptionIdAndDueDate(
      Long subscriptionId, LocalDate dueDate) {
    return notifications.values().stream()
        .filter(notification -> notification.subscriptionId().equals(subscriptionId))
        .filter(notification -> notification.dueDate().equals(dueDate))
        .findFirst();
  }

  private Long nextId(Notification notification) {
    if (notification.id() != null) {
      return notification.id();
    }

    return sequence.incrementAndGet();
  }
}
