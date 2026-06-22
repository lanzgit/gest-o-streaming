package br.edu.infnet.gestao_streaming.service;

import br.edu.infnet.gestao_streaming.domain.model.DashboardSummary;
import br.edu.infnet.gestao_streaming.domain.model.NotificationStatus;
import br.edu.infnet.gestao_streaming.domain.model.Payment;
import br.edu.infnet.gestao_streaming.domain.model.Subscription;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardService {

  private static final int DEFAULT_BILLING_WINDOW_DAYS = 7;
  private static final int DEFAULT_RECENT_PAYMENTS_LIMIT = 5;

  private final FinanceService financeService;
  private final SubscriptionService subscriptionService;
  private final BillingService billingService;
  private final NotificationService notificationService;
  private final PaymentService paymentService;
  private final UsageService usageService;

  public DashboardSummary summarize(
      Long userId, Integer billingWindowDays, Integer recentPaymentsLimit) {
    List<Subscription> subscriptions = subscriptionService.listByUser(userId);

    return new DashboardSummary(
        userId,
        financeService.summarize(userId),
        usageService.summarize(userId),
        countActive(subscriptions),
        countCancelled(subscriptions),
        usageService.listLowUsage(userId).size(),
        countUnreadNotifications(userId),
        billingService.listUpcoming(userId, resolveBillingWindow(billingWindowDays)),
        recentPayments(userId, resolveRecentPaymentsLimit(recentPaymentsLimit)));
  }

  private long countActive(List<Subscription> subscriptions) {
    return subscriptions.stream().filter(Subscription::isActive).count();
  }

  private long countCancelled(List<Subscription> subscriptions) {
    return subscriptions.size() - countActive(subscriptions);
  }

  private long countUnreadNotifications(Long userId) {
    return notificationService.listByUser(userId).stream()
        .filter(notification -> notification.status() == NotificationStatus.NAO_LIDA)
        .count();
  }

  private List<Payment> recentPayments(Long userId, int limit) {
    return paymentService.listByUser(userId).stream()
        .sorted(Comparator.comparing(Payment::paidAt).thenComparing(Payment::id).reversed())
        .limit(limit)
        .toList();
  }

  private int resolveBillingWindow(Integer days) {
    if (days == null) {
      return DEFAULT_BILLING_WINDOW_DAYS;
    }

    if (days < 0) {
      throw new IllegalArgumentException("billingWindowDays must be zero or positive.");
    }

    return days;
  }

  private int resolveRecentPaymentsLimit(Integer limit) {
    if (limit == null) {
      return DEFAULT_RECENT_PAYMENTS_LIMIT;
    }

    if (limit < 1) {
      throw new IllegalArgumentException("recentPaymentsLimit must be positive.");
    }

    return limit;
  }
}
