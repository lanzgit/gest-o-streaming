package br.edu.infnet.gestao_streaming.service;

import br.edu.infnet.gestao_streaming.domain.model.BillingCycle;
import br.edu.infnet.gestao_streaming.domain.model.Subscription;
import br.edu.infnet.gestao_streaming.domain.model.UpcomingBilling;
import br.edu.infnet.gestao_streaming.repository.SubscriptionRepository;
import java.time.Clock;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BillingService {

  private static final int DEFAULT_WINDOW_DAYS = 7;

  private final SubscriptionRepository subscriptions;
  private final Clock clock;

  public List<UpcomingBilling> listUpcoming(Long userId, Integer days) {
    LocalDate today = LocalDate.now(clock);
    LocalDate limit = today.plusDays(resolveWindow(days));

    return subscriptions.findByUserId(userId).stream()
        .filter(Subscription::isActive)
        .map(subscription -> toUpcomingBilling(subscription, today))
        .filter(upcomingBilling -> !upcomingBilling.dueDate().isAfter(limit))
        .sorted(Comparator.comparing(UpcomingBilling::dueDate))
        .toList();
  }

  private UpcomingBilling toUpcomingBilling(Subscription subscription, LocalDate today) {
    LocalDate dueDate = nextDueDate(subscription, today);
    return new UpcomingBilling(
        subscription.id(),
        subscription.userId(),
        subscription.streamingServiceId(),
        subscription.amount(),
        subscription.billingCycle(),
        dueDate,
        ChronoUnit.DAYS.between(today, dueDate));
  }

  private LocalDate nextDueDate(Subscription subscription, LocalDate today) {
    if (subscription.billingCycle() == BillingCycle.MENSAL) {
      return nextMonthlyDueDate(subscription.billingDate(), today);
    }

    if (subscription.billingCycle() == BillingCycle.ANUAL) {
      return nextAnnualDueDate(subscription.billingDate(), today);
    }

    throw new IllegalArgumentException("Unsupported billing cycle.");
  }

  private LocalDate nextMonthlyDueDate(LocalDate billingDate, LocalDate today) {
    LocalDate dueDate = billingDate;
    while (dueDate.isBefore(today)) {
      dueDate = dueDate.plusMonths(1);
    }
    return dueDate;
  }

  private LocalDate nextAnnualDueDate(LocalDate billingDate, LocalDate today) {
    LocalDate dueDate = billingDate;
    while (dueDate.isBefore(today)) {
      dueDate = dueDate.plusYears(1);
    }
    return dueDate;
  }

  private int resolveWindow(Integer days) {
    if (days == null) {
      return DEFAULT_WINDOW_DAYS;
    }

    if (days < 0) {
      throw new IllegalArgumentException("days must be zero or positive.");
    }

    return days;
  }
}
