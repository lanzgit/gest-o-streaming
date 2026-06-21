package br.edu.infnet.gestao_streaming;

import br.edu.infnet.gestao_streaming.domain.model.Notification;
import br.edu.infnet.gestao_streaming.domain.model.Payment;
import br.edu.infnet.gestao_streaming.domain.model.StreamingService;
import br.edu.infnet.gestao_streaming.domain.model.Subscription;
import br.edu.infnet.gestao_streaming.domain.model.Usage;
import br.edu.infnet.gestao_streaming.repository.NotificationRepository;
import br.edu.infnet.gestao_streaming.repository.PaymentRepository;
import br.edu.infnet.gestao_streaming.repository.StreamingServiceRepository;
import br.edu.infnet.gestao_streaming.repository.SubscriptionRepository;
import br.edu.infnet.gestao_streaming.repository.UsageRepository;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
class TestRepositoryConfiguration {

  @Bean
  StreamingServiceRepository streamingServiceRepository() {
    return new FakeStreamingServiceRepository();
  }

  @Bean
  SubscriptionRepository subscriptionRepository() {
    return new FakeSubscriptionRepository();
  }

  @Bean
  NotificationRepository notificationRepository() {
    return new FakeNotificationRepository();
  }

  @Bean
  PaymentRepository paymentRepository() {
    return new FakePaymentRepository();
  }

  @Bean
  UsageRepository usageRepository() {
    return new FakeUsageRepository();
  }

  private static class FakeStreamingServiceRepository implements StreamingServiceRepository {

    private final AtomicLong sequence = new AtomicLong();
    private final ConcurrentHashMap<Long, StreamingService> services = new ConcurrentHashMap<>();

    @Override
    public StreamingService save(StreamingService streamingService) {
      Long id = nextId(streamingService.id());
      StreamingService savedService =
          new StreamingService(id, streamingService.name(), streamingService.category());
      services.put(id, savedService);
      return savedService;
    }

    @Override
    public List<StreamingService> findAll() {
      return services.values().stream().sorted(Comparator.comparing(StreamingService::id)).toList();
    }

    @Override
    public Optional<StreamingService> findById(Long id) {
      return Optional.ofNullable(services.get(id));
    }

    private Long nextId(Long currentId) {
      if (currentId != null) {
        return currentId;
      }
      return sequence.incrementAndGet();
    }
  }

  private static class FakeSubscriptionRepository implements SubscriptionRepository {

    private final AtomicLong sequence = new AtomicLong();
    private final ConcurrentHashMap<Long, Subscription> subscriptions = new ConcurrentHashMap<>();

    @Override
    public Subscription save(Subscription subscription) {
      Long id = nextId(subscription.id());
      Subscription savedSubscription =
          new Subscription(
              id,
              subscription.userId(),
              subscription.streamingServiceId(),
              subscription.amount(),
              subscription.billingCycle(),
              subscription.billingDate(),
              subscription.status());

      subscriptions.put(id, savedSubscription);
      return savedSubscription;
    }

    @Override
    public List<Subscription> findByUserId(Long userId) {
      return subscriptions.values().stream()
          .filter(subscription -> subscription.userId().equals(userId))
          .sorted(Comparator.comparing(Subscription::id))
          .toList();
    }

    @Override
    public Optional<Subscription> findById(Long id) {
      return Optional.ofNullable(subscriptions.get(id));
    }

    private Long nextId(Long currentId) {
      if (currentId != null) {
        return currentId;
      }
      return sequence.incrementAndGet();
    }
  }

  private static class FakeNotificationRepository implements NotificationRepository {

    private final AtomicLong sequence = new AtomicLong();
    private final ConcurrentHashMap<Long, Notification> notifications = new ConcurrentHashMap<>();

    @Override
    public Notification save(Notification notification) {
      Long id = nextId(notification.id());
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

    private Long nextId(Long currentId) {
      if (currentId != null) {
        return currentId;
      }
      return sequence.incrementAndGet();
    }
  }

  private static class FakePaymentRepository implements PaymentRepository {

    private final AtomicLong sequence = new AtomicLong();
    private final ConcurrentHashMap<Long, Payment> payments = new ConcurrentHashMap<>();

    @Override
    public Payment save(Payment payment) {
      Long id = nextId(payment.id());
      Payment savedPayment =
          new Payment(
              id,
              payment.userId(),
              payment.subscriptionId(),
              payment.amount(),
              payment.paidAt(),
              payment.status(),
              payment.createdAt());

      payments.put(id, savedPayment);
      return savedPayment;
    }

    @Override
    public List<Payment> findByUserId(Long userId) {
      return payments.values().stream()
          .filter(payment -> payment.userId().equals(userId))
          .sorted(Comparator.comparing(Payment::paidAt).thenComparing(Payment::id))
          .toList();
    }

    @Override
    public List<Payment> findByUserIdAndSubscriptionId(Long userId, Long subscriptionId) {
      return payments.values().stream()
          .filter(payment -> payment.userId().equals(userId))
          .filter(payment -> payment.subscriptionId().equals(subscriptionId))
          .sorted(Comparator.comparing(Payment::paidAt).thenComparing(Payment::id))
          .toList();
    }

    private Long nextId(Long currentId) {
      if (currentId != null) {
        return currentId;
      }
      return sequence.incrementAndGet();
    }
  }

  private static class FakeUsageRepository implements UsageRepository {

    private final AtomicLong sequence = new AtomicLong();
    private final ConcurrentHashMap<Long, Usage> entries = new ConcurrentHashMap<>();

    @Override
    public Usage save(Usage usage) {
      Long id = nextId(usage.id());
      Usage savedUsage =
          new Usage(id, usage.userId(), usage.subscriptionId(), usage.level(), usage.updatedAt());
      entries.put(id, savedUsage);
      return savedUsage;
    }

    @Override
    public List<Usage> findByUserId(Long userId) {
      return entries.values().stream()
          .filter(usage -> usage.userId().equals(userId))
          .sorted(Comparator.comparing(Usage::subscriptionId))
          .toList();
    }

    @Override
    public Optional<Usage> findByUserIdAndSubscriptionId(Long userId, Long subscriptionId) {
      return entries.values().stream()
          .filter(usage -> usage.userId().equals(userId))
          .filter(usage -> usage.subscriptionId().equals(subscriptionId))
          .findFirst();
    }

    private Long nextId(Long currentId) {
      if (currentId != null) {
        return currentId;
      }
      return sequence.incrementAndGet();
    }
  }
}
