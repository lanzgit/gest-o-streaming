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
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

final class RepositoryTestDoubles {

  private RepositoryTestDoubles() {}

  static StreamingServiceRepository streamingServices() {
    AtomicLong sequence = new AtomicLong();
    ConcurrentHashMap<Long, StreamingService> entries = new ConcurrentHashMap<>();

    return proxy(
        StreamingServiceRepository.class,
        (proxy, method, args) ->
            switch (method.getName()) {
              case "save" -> {
                StreamingService streamingService = (StreamingService) args[0];
                Long id = nextId(sequence, streamingService.id());
                StreamingService saved =
                    new StreamingService(id, streamingService.name(), streamingService.category());
                entries.put(id, saved);
                yield saved;
              }
              case "findAll" ->
                  entries.values().stream()
                      .sorted(Comparator.comparing(StreamingService::id))
                      .toList();
              case "findById" -> Optional.ofNullable(entries.get((Long) args[0]));
              default -> unsupported(method);
            });
  }

  static SubscriptionRepository subscriptions() {
    AtomicLong sequence = new AtomicLong();
    ConcurrentHashMap<Long, Subscription> entries = new ConcurrentHashMap<>();

    return proxy(
        SubscriptionRepository.class,
        (proxy, method, args) ->
            switch (method.getName()) {
              case "save" -> {
                Subscription subscription = (Subscription) args[0];
                Long id = nextId(sequence, subscription.id());
                Subscription saved =
                    new Subscription(
                        id,
                        subscription.userId(),
                        subscription.streamingServiceId(),
                        subscription.amount(),
                        subscription.billingCycle(),
                        subscription.billingDate(),
                        subscription.status());
                entries.put(id, saved);
                yield saved;
              }
              case "findById" -> Optional.ofNullable(entries.get((Long) args[0]));
              case "findByUserIdOrderById" ->
                  entries.values().stream()
                      .filter(subscription -> subscription.userId().equals(args[0]))
                      .sorted(Comparator.comparing(Subscription::id))
                      .toList();
              default -> unsupported(method);
            });
  }

  static PaymentRepository payments() {
    AtomicLong sequence = new AtomicLong();
    ConcurrentHashMap<Long, Payment> entries = new ConcurrentHashMap<>();

    return proxy(
        PaymentRepository.class,
        (proxy, method, args) ->
            switch (method.getName()) {
              case "save" -> {
                Payment payment = (Payment) args[0];
                Long id = nextId(sequence, payment.id());
                Payment saved =
                    new Payment(
                        id,
                        payment.userId(),
                        payment.subscriptionId(),
                        payment.amount(),
                        payment.paidAt(),
                        payment.status(),
                        payment.createdAt());
                entries.put(id, saved);
                yield saved;
              }
              case "findByUserIdOrderByPaidAtAscIdAsc" ->
                  entries.values().stream()
                      .filter(payment -> payment.userId().equals(args[0]))
                      .sorted(Comparator.comparing(Payment::paidAt).thenComparing(Payment::id))
                      .toList();
              case "findByUserIdAndSubscriptionIdOrderByPaidAtAscIdAsc" ->
                  entries.values().stream()
                      .filter(payment -> payment.userId().equals(args[0]))
                      .filter(payment -> payment.subscriptionId().equals(args[1]))
                      .sorted(Comparator.comparing(Payment::paidAt).thenComparing(Payment::id))
                      .toList();
              default -> unsupported(method);
            });
  }

  static NotificationRepository notifications() {
    AtomicLong sequence = new AtomicLong();
    ConcurrentHashMap<Long, Notification> entries = new ConcurrentHashMap<>();

    return proxy(
        NotificationRepository.class,
        (proxy, method, args) ->
            switch (method.getName()) {
              case "save" -> {
                Notification notification = (Notification) args[0];
                Long id = nextId(sequence, notification.id());
                Notification saved =
                    new Notification(
                        id,
                        notification.userId(),
                        notification.subscriptionId(),
                        notification.dueDate(),
                        notification.message(),
                        notification.status(),
                        notification.createdAt());
                entries.put(id, saved);
                yield saved;
              }
              case "findByUserIdOrderByCreatedAtAscIdAsc" ->
                  entries.values().stream()
                      .filter(notification -> notification.userId().equals(args[0]))
                      .sorted(
                          Comparator.comparing(Notification::createdAt)
                              .thenComparing(Notification::id))
                      .toList();
              case "findBySubscriptionIdAndDueDate" ->
                  entries.values().stream()
                      .filter(notification -> notification.subscriptionId().equals(args[0]))
                      .filter(notification -> notification.dueDate().equals((LocalDate) args[1]))
                      .findFirst();
              default -> unsupported(method);
            });
  }

  static UsageRepository usage() {
    AtomicLong sequence = new AtomicLong();
    ConcurrentHashMap<Long, Usage> entries = new ConcurrentHashMap<>();

    return proxy(
        UsageRepository.class,
        (proxy, method, args) ->
            switch (method.getName()) {
              case "save" -> {
                Usage usage = (Usage) args[0];
                Long id = nextId(sequence, usage.id());
                Usage saved =
                    new Usage(
                        id,
                        usage.userId(),
                        usage.subscriptionId(),
                        usage.level(),
                        usage.updatedAt());
                entries.put(id, saved);
                yield saved;
              }
              case "findByUserIdOrderBySubscriptionIdAsc" ->
                  entries.values().stream()
                      .filter(usage -> usage.userId().equals(args[0]))
                      .sorted(Comparator.comparing(Usage::subscriptionId))
                      .toList();
              case "findByUserIdAndSubscriptionId" ->
                  entries.values().stream()
                      .filter(usage -> usage.userId().equals(args[0]))
                      .filter(usage -> usage.subscriptionId().equals(args[1]))
                      .findFirst();
              default -> unsupported(method);
            });
  }

  @SuppressWarnings("unchecked")
  private static <T> T proxy(Class<T> type, InvocationHandler handler) {
    return (T) Proxy.newProxyInstance(type.getClassLoader(), new Class<?>[] {type}, handler);
  }

  private static Long nextId(AtomicLong sequence, Long currentId) {
    if (currentId != null) {
      return currentId;
    }
    return sequence.incrementAndGet();
  }

  private static Object unsupported(Method method) {
    throw new UnsupportedOperationException(
        method.getName() + " is not supported by this test double.");
  }
}
